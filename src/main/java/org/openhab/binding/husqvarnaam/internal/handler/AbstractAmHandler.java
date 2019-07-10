/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.husqvarnaam.internal.handler;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.measure.quantity.Dimensionless;
import javax.measure.quantity.ElectricCurrent;
import javax.measure.quantity.ElectricPotential;
import javax.measure.quantity.Energy;
import javax.measure.quantity.Frequency;
import javax.measure.quantity.Speed;
import javax.measure.quantity.Temperature;
import javax.measure.quantity.Time;

import org.eclipse.smarthome.core.library.types.DateTimeType;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.QuantityType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.library.unit.SIUnits;
import org.eclipse.smarthome.core.library.unit.SmartHomeUnits;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.husqvarnaam.HusqvarnaAmBindingConstants;
import org.openhab.binding.husqvarnaam.HusqvarnaAmBindingUnitsOfMeasurement;
import org.openhab.binding.husqvarnaam.internal.protocol.RequestResponseFactory;
import org.openhab.binding.husqvarnaam.internal.protocol.Response;
import org.openhab.binding.husqvarnaam.protocol.AmConnection;
import org.openhab.binding.husqvarnaam.protocol.AmConnectionException;
import org.openhab.binding.husqvarnaam.protocol.AmResponse;
import org.openhab.binding.husqvarnaam.protocol.CommandTypeNotSupportedException;
import org.openhab.binding.husqvarnaam.protocol.event.AmDisconnectionEvent;
import org.openhab.binding.husqvarnaam.protocol.event.AmDisconnectionListener;
import org.openhab.binding.husqvarnaam.protocol.event.AmStatusUpdateEvent;
import org.openhab.binding.husqvarnaam.protocol.event.AmUpdateListener;
import org.openhab.binding.husqvarnaam.protocol.utils.AmInformationConverter;
import org.openhab.binding.husqvarnaam.protocol.utils.DateTimeConverter;
import org.openhab.binding.husqvarnaam.protocol.utils.HexConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tec.uom.se.unit.MetricPrefix;

/**
 * The {@link AbstractAmHandler} is responsible for handling commands, which are
 * sent to one of the channels through an AM connection.
 *
 * Based on the Pioneer binding by Antoine Besnard and authors mentioned there
 * 
 * @author Nikolaus Chorherr - Initial Contribution
 */
public abstract class AbstractAmHandler extends BaseThingHandler
        implements
            AmUpdateListener,
            AmDisconnectionListener {

    private Logger logger = LoggerFactory.getLogger(AbstractAmHandler.class);

    private AmConnection connection;
    private ScheduledFuture<?> statusCheckerFuture;
    //
    private Calendar currentAmDateTime;

    private Boolean expertMode;

    public AbstractAmHandler(Thing thing) {
        super(thing);
        this.connection = createConnection();

        this.connection.addUpdateListener(this);
        this.connection.addDisconnectionListener(this);
        this.currentAmDateTime = Calendar.getInstance();
        this.currentAmDateTime.clear();
    }

    /**
     * Create a new connection to the AM.
     *
     * @return
     */
    protected abstract AmConnection createConnection();

    /**
     * Initialize the state of the AM.
     */
    @Override
    public void initialize() {
        logger.debug("Initializing handler for Husqvarna AM @{}",
                connection.getConnectionName());
        updateStatus(ThingStatus.ONLINE);
        this.setExpertMode((Boolean) this.getConfig()
                .get(HusqvarnaAmBindingConstants.EXPERT_MODE));

        // Start the status checker
        Runnable statusChecker = new Runnable() {
            @Override
            public void run() {
                try {
                    logger.debug("Checking status of AM @{}",
                            connection.getConnectionName());
                    checkStatus();
                } catch (LinkageError e) {
                    logger.warn(
                            "Failed to check the status for AM @{}. If a Serial link is used to connect to the A, please check that the Bundle org.openhab.io.transport.serial is available. Cause: {}",
                            connection.getConnectionName(), e.getMessage());
                    // Stop to check the status of this AM.
                    if (statusCheckerFuture != null) {
                        statusCheckerFuture.cancel(false);
                    }
                }
            }
        };
        Integer interval = ((Number) this.getConfig()
                .get(HusqvarnaAmBindingConstants.POLLING_INTERVAL_IN_SEC))
                        .intValue();

        statusCheckerFuture = scheduler.scheduleWithFixedDelay(statusChecker, 1,
                interval, TimeUnit.SECONDS);
    }

    /**
     * Close the connection and stop the status checker.
     */
    @Override
    public void dispose() {
        super.dispose();
        if (statusCheckerFuture != null) {
            statusCheckerFuture.cancel(true);
        }
        if (connection != null) {
            connection.close();
        }
    }

    public boolean isExpertMode() {
        return this.expertMode != null ? this.expertMode.booleanValue() : false;
    }

    public void setExpertMode(Boolean expertMode) {
        this.expertMode = expertMode != null ? expertMode : Boolean.FALSE;
    }

    /*
     * sets all data initially Timers, timerflags
     */
//    private void doInitialization() {
//        // Timers
//        connection.sendTimerActiveQuery();
//        connection.sendTimerQuery();
//    }

    public void onStartup() {
        connection.sendStatusQuery();
        connection.sendChargeTimeQuery();
        connection.sendMowTimeQuery();
        connection.sendOperationalTimeQuery();
        connection.sendCurrentDateTimeQuery();
        this.manageExpertMode();
        this.manageLatestUpdateTime();
    }

    /**
     * Check the status of the AM. Return true if the AM is online, else return
     * false.
     *
     * @return
     */
    private void checkStatus() {
        // If the status query request has not been sent, the connection to the
        // AM has failed. So update its status to OFFLINE.
        if (!connection.sendStatusQuery()) {
            updateStatus(ThingStatus.OFFLINE);
        } else {
            // If the status query has succeeded, the AM status is ONLINE.
            updateStatus(ThingStatus.ONLINE);
            connection.sendChargeTimeQuery();
            connection.sendMowTimeQuery();
            connection.sendOperationalTimeQuery();
            connection.sendCurrentDateTimeQuery();
            connection.sendCurrentModeQuery();
            // query timers
            connection.sendTimerActiveQuery();
            connection.sendTimerQuery();
            if (isExpertMode()) {
                // Details
                connection.sendDetailsQuery();
            }
            this.manageLatestUpdateTime();
        }
    }

    /**
     * Send a command to the AM based on the openHAB command received.
     */
    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        try {
            boolean commandSent = false;
            boolean unknownCommand = false;
            logger.debug("Receiving command {} for channel uid {}",
                    command.toFullString(), channelUID.getId());
            if (channelUID.getId()
                    .contains(HusqvarnaAmBindingConstants.MODE_CHANNEL)) {
                commandSent = connection.sendModeCommand(command);
            } else if (channelUID.getId().contains(
                    HusqvarnaAmBindingConstants.TIMER_ACTIVE_CHANNEL)) {
                commandSent = connection.sendTimerActiveCommand(command);
            } else if (channelUID.getId().contains(
                    HusqvarnaAmBindingConstants.TIMER_WEEKDAYS_ACTIVE_CHANNEL)) {
                commandSent = connection
                        .sendTimerWeekdaysActiveCommand(command);
            } else if (channelUID.getId()
                    .contains(HusqvarnaAmBindingConstants.TIMER_TIMER1_START)) {
                commandSent = connection.sendTimer1StartCommand(command);
            } else if (channelUID.getId()
                    .contains(HusqvarnaAmBindingConstants.TIMER_TIMER1_STOP)) {
                commandSent = connection.sendTimer1StopCommand(command);
            } else if (channelUID.getId()
                    .contains(HusqvarnaAmBindingConstants.TIMER_TIMER2_START)) {
                commandSent = connection.sendTimer2StartCommand(command);
            } else if (channelUID.getId()
                    .contains(HusqvarnaAmBindingConstants.TIMER_TIMER2_STOP)) {
                commandSent = connection.sendTimer2StopCommand(command);
            } else if (channelUID.getId().contains(
                    HusqvarnaAmBindingConstants.TIMER_WETIMER1_START)) {
                commandSent = connection.sendWeTimer1StartCommand(command);
            } else if (channelUID.getId().contains(
                    HusqvarnaAmBindingConstants.TIMER_WETIMER1_STOP)) {
                commandSent = connection.sendWeTimer1StopCommand(command);
            } else if (channelUID.getId().contains(
                    HusqvarnaAmBindingConstants.TIMER_WETIMER2_START)) {
                commandSent = connection.sendWeTimer2StartCommand(command);
            } else if (channelUID.getId().contains(
                    HusqvarnaAmBindingConstants.TIMER_WETIMER2_STOP)) {
                commandSent = connection.sendWeTimer2StopCommand(command);
            } else if (channelUID.getId().contains(
                    HusqvarnaAmBindingConstants.CHANNEL_GROUP_DETAILS)) {
                // we do not update details information on AM, so we ignore
                // "REFRESH commands", updates are done with StatusQuery
                logger.debug(
                        "Ignoring command '{}' for details channel group ({})",
                        command.toFullString(), channelUID.getId());
            } else {
                unknownCommand = true;
            }

            // If the command is not unknown and has not been sent, the AM is
            // Offline
            if (!commandSent && !unknownCommand) {
                onDisconnection();
            }
        } catch (CommandTypeNotSupportedException e) {
            logger.warn("Unsupported command type {} received for channel {}.",
                    command.toFullString(), channelUID.getId());
        }
    }

    /**
     * Called when a status update is received from the AM.
     */
    @Override
    public void statusUpdateReceived(AmStatusUpdateEvent event) {
        try {
            AmResponse response = RequestResponseFactory
                    .getIpControlResponse(event.getData());
            logger.debug("Response Type is {}",
                    response.getResponseType() != null
                            ? response.getResponseType()
                            : "unknown");

            switch (((Response) response).getResponseType()) {
                case STATUS_UPDATE :
                    this.manageStatusUpdate(response);
                    break;
                case MOW_TIME_UPDATE :
                    this.manageMowTimeUpdate(response);
                    break;
                case CHARGE_TIME_UPDATE :
                    this.manageChargeTimeUpdate(response);
                    break;
                case OPERATION_TIME_UPDATE :
                    this.manageOperationTimeUpdate(response);
                    break;
                case CURRENT_DT_SECOND_UPDATE :
                    this.manageCurrentSecondUpdate(response);
                    break;
                case CURRENT_DT_MINUTE_UPDATE :
                    this.manageCurrentMinuteUpdate(response);
                    break;
                case CURRENT_DT_HOUR_UPDATE :
                    this.manageCurrentHourUpdate(response);
                    break;
                case CURRENT_DT_DAY_UPDATE :
                    this.manageCurrentDayUpdate(response);
                    break;
                case CURRENT_DT_MONTH_UPDATE :
                    this.manageCurrentMonthUpdate(response);
                    break;
                case CURRENT_DT_YEAR_UPDATE :
                    this.manageCurrentYearUpdate(response);
                    break;
                case CURRENT_MODE_UPDATE :
                    this.manageCurrentModeUpdate(response);
                    break;
                // Timers
                case TIMER_ACTIVE_UPDATE :
                case SET_TIMER_ACTIVE_UPDATE :
                    this.manageTimerActiveUpdate(response);
                    break;
                case TIMER_WEEK_UPDATE :
                case SET_TIMER_WEEK_UPDATE :
                    this.manageTimerWeekdaysActiveUpdate(response);
                    break;
                case TIMER1_START_UPDATE :
                case SET_TIMER1_START_UPDATE :
                    this.manageTime1StartUpdate(response);
                    break;
                case TIMER1_STOP_UPDATE :
                case SET_TIMER1_STOP_UPDATE :
                    this.manageTime1StopUpdate(response);
                    break;
                case TIMER2_START_UPDATE :
                case SET_TIMER2_START_UPDATE :
                    this.manageTime2StartUpdate(response);
                    break;
                case TIMER2_STOP_UPDATE :
                case SET_TIMER2_STOP_UPDATE :
                    this.manageTime2StopUpdate(response);
                    break;
                case WETIMER1_START_UPDATE :
                case SET_WETIMER1_START_UPDATE :
                    this.manageWeTime1StartUpdate(response);
                    break;
                case WETIMER1_STOP_UPDATE :
                case SET_WETIMER1_STOP_UPDATE :
                    this.manageWeTime1StopUpdate(response);
                    break;
                case WETIMER2_START_UPDATE :
                case SET_WETIMER2_START_UPDATE :
                    this.manageWeTime2StartUpdate(response);
                    break;
                case WETIMER2_STOP_UPDATE :
                case SET_WETIMER2_STOP_UPDATE :
                    this.manageWeTime2StopUpdate(response);
                    break;
                // details
                case CURRENT_BATTERY_CAPACITY_USED_MAH_UPDATE :
                    this.manageBatteryCapacityUsed(response);
                    break;
                case RECTANGLE_MODE_STATE_UPDATE :
                    this.manageRectangleModeState(response);
                    break;
                case RECTANGLE_MODE_PERCENT_UPDATE :
                    this.manageRectangleModePercent(response);
                    break;
                case RECTANGLE_MODE_REFERENCE_UPDATE :
                    this.manageRectangleModeReference(response);
                    break;
                case CURRENT_BATTERY_CAPACITY_MA_UPDATE :
                    this.manageBatteryCurrentMa(response);
                    break;
                case CURRENT_BATTERY_CAPACITY_MAH_UPDATE :
                    this.manageBatteryCapacityMah(response);
                    break;
                case CURRENT_BATTERY_CAPACITY_SEARCH_START_MAH_UPDATE :
                    this.manageBatteryCapacitySearchStartMah(response);
                    break;
                case CURRENT_BATTERY_VOLTAGE_MV_UPDATE :
                    this.manageBatteryVoltage(response);
                    break;
                case CURRENT_BATTERY_TEMPERATURE_UPDATE :
                    this.manageBatteryTemperature(response);
                    break;
                case CURRENT_BATTERY_TEMPERATURE_CHARGE_UPDATE :
                    this.manageBatteryTemperatureCharge(response);
                    break;
                case CURRENT_BATTERY_LAST_CHARGE_MIN_UPDATE :
                    this.manageBatteryLastChargeMin(response);
                    break;
                case CURRENT_BATTERY_NEXT_TEMPERATURE_MEASUREMENT_SEC_UPDATE :
                    this.manageBatteryTemperatureNextMeasurement(response);
                    break;
                case CURRENT_VELOCITY_MOTOR_UPDATE :
                    this.manageVelocityMotor(response);
                    break;
                case CURRENT_VELOCITY_RIGHT_UPDATE :
                    this.manageVelocityRightWheel(response);
                    break;
                case CURRENT_VELOCITY_LEFT_UPDATE :
                    this.manageVelocityLeftWheel(response);
                    break;
                case FIRMWARE_VERSION_UPDATE :
                    this.manageFirmwareVersion(response);
                    break;
                case LANGUAGE_FILE_VERSION_UPDATE :
                    this.manageLanguageFileVersion(response);
                    break;
                case IAMALIVE :
                    break;
                default :
                    logger.debug(
                            "Not yet supported response type from AM @{}. Response discarded: {}",
                            event.getConnection().getConnectionName(),
                            HexConverter.toString(event.getData()));
            }
        } catch (AmConnectionException e) {
            logger.debug(
                    "Unknown response type from AM @{}. Response discarded: {} on exception {}",
                    event.getConnection().getConnectionName(),
                    HexConverter.toString(event.getData()), e.getMessage());
        }
    }

    /**
     * Called when the AM is disconnected
     */
    @Override
    public void onDisconnection(AmDisconnectionEvent event) {
        onDisconnection();
    }

    /**
     * Process the AM disconnection.
     */
    private void onDisconnection() {
        updateStatus(ThingStatus.OFFLINE);
        // reset connections !!!
        this.connection.close();
    }

    /**
     * Notify an AM status update to openHAB
     *
     * @param response
     */
    private void manageStatusUpdate(AmResponse response) {
        updateState(HusqvarnaAmBindingConstants.CURRENT_STATE_CHANNEL,
                new DecimalType(
                        AmInformationConverter.convertStateResponse(
                                response.getParameterValue())));
    }

    /**
     * Notify an AM mow time update to openHAB
     *
     * @param response
     */
    private void manageMowTimeUpdate(AmResponse response) {
        updateState(HusqvarnaAmBindingConstants.MOWTIME_CHANNEL,
                new QuantityType<Time>(AmInformationConverter
                        .convertMowTimeMessage(response.getParameterValue()),SmartHomeUnits.MINUTE));

    }

    /**
     * Notify an AM charge time update to openHAB
     *
     * @param response
     */
    private void manageChargeTimeUpdate(AmResponse response) {
        updateState(HusqvarnaAmBindingConstants.CHARGETIME_CHANNEL,
                new QuantityType<Time>(
                        AmInformationConverter.convertChargeTimeMessage(
                                response.getParameterValue()),SmartHomeUnits.MINUTE));

    }

    /**
     * Notify an AM mode update to openHAB
     *
     * @param response
     */
    private void manageCurrentModeUpdate(AmResponse response) {
        updateState(HusqvarnaAmBindingConstants.MODE_CHANNEL,
                new DecimalType(AmInformationConverter
                        .convertModeMessage(response.getParameterValue())));

    }

    /**
     * Notify an AM operational time update to openHAB
     *
     * @param response
     */
    private void manageOperationTimeUpdate(AmResponse response) {
        updateState(HusqvarnaAmBindingConstants.OPERATIONTIME_CHANNEL,
                new QuantityType<Time>(
                        AmInformationConverter.convertOperationTimeMessage(
                                response.getParameterValue()),SmartHomeUnits.HOUR));
    }

    private void manageCurrentSecondUpdate(AmResponse response) {
        int second = DateTimeConverter
                .convertSecond(response.getParameterValue());
        this.getCalendar().set(Calendar.SECOND, second);
        manageCurrentDateTimeUpdate();
    }

    private void manageCurrentMinuteUpdate(AmResponse response) {
        int minute = DateTimeConverter
                .convertMinute(response.getParameterValue());
        this.getCalendar().set(Calendar.MINUTE, minute);
        manageCurrentDateTimeUpdate();
    }

    private void manageCurrentHourUpdate(AmResponse response) {
        int hour = DateTimeConverter.convertHour(response.getParameterValue());
        this.getCalendar().set(Calendar.HOUR, hour);
        manageCurrentDateTimeUpdate();
    }

    private void manageCurrentDayUpdate(AmResponse response) {
        int day = DateTimeConverter.convertDay(response.getParameterValue());
        this.getCalendar().set(Calendar.DAY_OF_MONTH, day);
        manageCurrentDateTimeUpdate();
    }

    private void manageCurrentMonthUpdate(AmResponse response) {
        int month = DateTimeConverter
                .convertMonth(response.getParameterValue());
        // January == 0
        this.getCalendar().set(Calendar.MONTH, month - 1);
        manageCurrentDateTimeUpdate();
    }

    private void manageCurrentYearUpdate(AmResponse response) {
        int year = DateTimeConverter.convertYear(response.getParameterValue());
        year += 2000;
        this.getCalendar().set(Calendar.YEAR, year);
        manageCurrentDateTimeUpdate();
    }

    private void manageCurrentDateTimeUpdate() {
        // wait until fully set
        if (this.isCalendarFullySet()) {
            updateState(HusqvarnaAmBindingConstants.CURRENT_DATETIME_CHANNEL,
                    new DateTimeType(ZonedDateTime.ofInstant(currentAmDateTime.toInstant(), ZoneId.systemDefault())));
        }
    }

    private Calendar getCalendar() {
        return currentAmDateTime;
    }

    private boolean isCalendarFullySet() {
        return this.currentAmDateTime.isSet(Calendar.SECOND)
                && this.currentAmDateTime.isSet(Calendar.MINUTE)
                && this.currentAmDateTime.isSet(Calendar.HOUR)
                && this.currentAmDateTime.isSet(Calendar.DAY_OF_MONTH)
                && this.currentAmDateTime.isSet(Calendar.MONTH)
                && this.currentAmDateTime.isSet(Calendar.YEAR);
    }

    private void manageBatteryCapacityUsed(AmResponse response) {
        updateState(HusqvarnaAmBindingConstants.BATTERY_CAPACITY_USED_CHANNEL,
                new QuantityType<Energy>(
                        AmInformationConverter.convertBatteryCapacityMAH(
                                response.getParameterValue()), HusqvarnaAmBindingUnitsOfMeasurement.MILLIWATT_PER_HOUR));
    }

    private void manageBatteryCurrentMa(AmResponse response) {
        updateState(HusqvarnaAmBindingConstants.BATTERY_CURRENT_MA_CHANNEL,
                new QuantityType<ElectricCurrent>(
                        AmInformationConverter.convertBatteryCurrentMessage(
                                response.getParameterValue()), MetricPrefix.MILLI(SmartHomeUnits.AMPERE)));
    }

    private void manageBatteryCapacityMah(AmResponse response) {
        updateState(HusqvarnaAmBindingConstants.BATTERY_CAPACITY_MAH_CHANNEL,
                new QuantityType<Energy>(
                        AmInformationConverter.convertBatteryCapacityMAH(
                                response.getParameterValue()), HusqvarnaAmBindingUnitsOfMeasurement.MILLIWATT_PER_HOUR));
    }

    private void manageBatteryCapacitySearchStartMah(AmResponse response) {
        updateState(
                HusqvarnaAmBindingConstants.BATTERY_CAPACITY_SEARCH_START_MAH_CHANNEL,
                new QuantityType<Energy>(
                        AmInformationConverter.convertBatteryCapacityMAH(
                                response.getParameterValue()), HusqvarnaAmBindingUnitsOfMeasurement.MILLIWATT_PER_HOUR));
    }

    private void manageBatteryVoltage(AmResponse response) {
        updateState(HusqvarnaAmBindingConstants.BATTERY_VOLTAGE_CHANNEL,
                new QuantityType<ElectricPotential>(
                        AmInformationConverter.convertBatteryVoltageMV(
                                response.getParameterValue()),MetricPrefix.MILLI(SmartHomeUnits.VOLT)));
    }

    private void manageBatteryTemperature(AmResponse response) {
        updateState(HusqvarnaAmBindingConstants.BATTERY_TEMPERATURE_CHANNEL,
                new QuantityType<Temperature>(
                        AmInformationConverter.convertBatteryTemperatureC(
                                response.getParameterValue()),SIUnits.CELSIUS));
    }

    private void manageBatteryTemperatureCharge(AmResponse response) {
        updateState(
                HusqvarnaAmBindingConstants.BATTERY_TEMPERATURE_CHARGE_CHANNEL,
                new QuantityType<Temperature>(
                        AmInformationConverter.convertBatteryTemperatureC(
                                response.getParameterValue()),SIUnits.CELSIUS));
    }

    private void manageBatteryLastChargeMin(AmResponse response) {
        updateState(
                HusqvarnaAmBindingConstants.BATTERY_TEMPERATURE_LATEST_CHARGE_MIN_CHANNEL,
                new QuantityType<Time>(HexConverter
                        .convertAsUnsignedBytes(response.getParameterValue()),SmartHomeUnits.MINUTE));
    }

    private void manageBatteryTemperatureNextMeasurement(AmResponse response) {
        updateState(
                HusqvarnaAmBindingConstants.BATTERY_TEMPERATURE_NEXT_MEASUREMENT_CHANNEL,
                new StringType(HexConverter
                        .convertHex(response.getParameterValue())));
    }

    private void manageRectangleModeState(AmResponse response) {
        updateState(HusqvarnaAmBindingConstants.RECTANGLEMODE_STATE_CHANNEL,
                AmInformationConverter
                        .convertBoolean(response.getParameterValue())
                                ? OnOffType.ON
                                : OnOffType.OFF);
    }

    private void manageRectangleModePercent(AmResponse response) {
        updateState(HusqvarnaAmBindingConstants.RECTANGLEMODE_PERCENT_CHANNEL,
                new QuantityType<Dimensionless>(AmInformationConverter
                        .convertPercent(response.getParameterValue()),SmartHomeUnits.PERCENT));
    }

    private void manageRectangleModeReference(AmResponse response) {
        updateState(HusqvarnaAmBindingConstants.RECTANGLEMODE_REFERENCE_CHANNEL,
                new StringType(HexConverter
                        .convertHex(response.getParameterValue())));
    }

    private void manageVelocityMotor(AmResponse response) {
        updateState(HusqvarnaAmBindingConstants.VELOCITY_MOTOR_CHANNEL,
                new QuantityType<Frequency>(AmInformationConverter
                        .convertVelocity(response.getParameterValue()),HusqvarnaAmBindingUnitsOfMeasurement.REVOLUTION_PER_MINUTE));
    }

    private void manageVelocityLeftWheel(AmResponse response) {
        updateState(HusqvarnaAmBindingConstants.VELOCITY_LEFT_CHANNEL,
                new QuantityType<Speed>(AmInformationConverter
                        .convertVelocity(response.getParameterValue()),HusqvarnaAmBindingUnitsOfMeasurement.CENTIMETER_PER_SECOND));
    }

    private void manageVelocityRightWheel(AmResponse response) {
        updateState(HusqvarnaAmBindingConstants.VELOCITY_RIGHT_CHANNEL,
                new QuantityType<Speed>(AmInformationConverter
                        .convertVelocity(response.getParameterValue()),HusqvarnaAmBindingUnitsOfMeasurement.CENTIMETER_PER_SECOND));
    }

    private void manageFirmwareVersion(AmResponse response) {
        updateState(HusqvarnaAmBindingConstants.FIRMWARE_VERSION_CHANNEL,
                new StringType(HexConverter
                        .convertHex(response.getParameterValue())));
    }

    private void manageLanguageFileVersion(AmResponse response) {
        updateState(HusqvarnaAmBindingConstants.LANGUAGE_FILE_VERSION_CHANNEL,
                new StringType(HexConverter
                        .convertHex(response.getParameterValue())));
    }

    private void manageTime1StartUpdate(AmResponse response) {
        updateState(HusqvarnaAmBindingConstants.TIMER_TIMER1_START,
                new DateTimeType(DateTimeConverter
                        .convertDateTime(response.getParameterValue())));
    }

    private void manageTime1StopUpdate(AmResponse response) {
        updateState(HusqvarnaAmBindingConstants.TIMER_TIMER1_STOP,
                new DateTimeType(DateTimeConverter
                        .convertDateTime(response.getParameterValue())));
    }

    private void manageTime2StartUpdate(AmResponse response) {
        updateState(HusqvarnaAmBindingConstants.TIMER_TIMER2_START,
                new DateTimeType(DateTimeConverter
                        .convertDateTime(response.getParameterValue())));
    }

    private void manageTime2StopUpdate(AmResponse response) {
        updateState(HusqvarnaAmBindingConstants.TIMER_TIMER2_STOP,
                new DateTimeType(DateTimeConverter
                        .convertDateTime(response.getParameterValue())));
    }

    private void manageWeTime1StartUpdate(AmResponse response) {
        updateState(HusqvarnaAmBindingConstants.TIMER_WETIMER1_START,
                new DateTimeType(DateTimeConverter
                        .convertDateTime(response.getParameterValue())));
    }

    private void manageWeTime1StopUpdate(AmResponse response) {
        updateState(HusqvarnaAmBindingConstants.TIMER_WETIMER1_STOP,
                new DateTimeType(DateTimeConverter
                        .convertDateTime(response.getParameterValue())));
    }

    private void manageWeTime2StartUpdate(AmResponse response) {
        updateState(HusqvarnaAmBindingConstants.TIMER_WETIMER2_START,
                new DateTimeType(DateTimeConverter
                        .convertDateTime(response.getParameterValue())));
    }

    private void manageWeTime2StopUpdate(AmResponse response) {
        updateState(HusqvarnaAmBindingConstants.TIMER_WETIMER2_STOP,
                new DateTimeType(DateTimeConverter
                        .convertDateTime(response.getParameterValue())));
    }

    private void manageTimerActiveUpdate(AmResponse response) {
        byte[] p = response.getParameterValue();
        OnOffType onOff = OnOffType.ON;
        // AM G2: "timer eingeschaltet" -> no flag
        if (p[0] == 0x00) {
            onOff = OnOffType.ON;
            // AM G2: "timer ausgeschaltet" -> flagged
        } else if (p[0] == 0x01) {
            onOff = OnOffType.OFF;
        }
        updateState(HusqvarnaAmBindingConstants.TIMER_ACTIVE_CHANNEL, onOff);
    }

    private void manageTimerWeekdaysActiveUpdate(AmResponse response) {
        updateState(HusqvarnaAmBindingConstants.TIMER_WEEKDAYS_ACTIVE_CHANNEL,
                new DecimalType(AmInformationConverter
                        .convertTimerBitArray(response.getParameterValue())));
    }

    private void manageExpertMode() {
        updateState(HusqvarnaAmBindingConstants.EXPERT_MODE_CHANNEL,OnOffType.from(this.expertMode));
    }
    
    private void manageLatestUpdateTime() {
        updateState(HusqvarnaAmBindingConstants.LATEST_UPDATE_TIME, new DateTimeType());
    }
}
