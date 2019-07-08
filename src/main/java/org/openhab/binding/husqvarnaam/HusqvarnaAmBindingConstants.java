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
package org.openhab.binding.husqvarnaam;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingTypeUID;


/**
 * The {@link HusqvarnaAmBinding} class defines common constants, which are used
 * across the whole binding.
 *
 * @author Nikolaus Chorherr - Initial contribution
 */
@NonNullByDefault
public class HusqvarnaAmBindingConstants {

    public static final String BINDING_ID = "husqvarnaam";

    public static final Set<String> SUPPORTED_DEVICE_MODELS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("220", "230X")));

    // List of all Thing Type UIDs
    public static final ThingTypeUID IP_AM_THING_TYPE = new ThingTypeUID(
            BINDING_ID, "ipAm");
    public static final ThingTypeUID IP_AM_UNSUPPORTED_THING_TYPE = new ThingTypeUID(
            BINDING_ID, "ipAmUnsupported");

    // List of thing parameters names
    public static final String PROTOCOL_PARAMETER = "protocol";
    public static final String HOST_PARAMETER = "address";
    public static final String TCP_PORT_PARAMETER = "tcpPort";
    public static final String POLLING_INTERVAL_IN_SEC = "pollingIntervalInSec";
    public static final String EXPERT_MODE = "expertMode";

    public static final String IP_PROTOCOL_NAME = "IP";
    public static final String SERIAL_PROTOCOL_NAME = "serial";

    // List of all Channel names
    public static final String CHANNEL_GROUP_DISPLAY = "displayInformation";
    public static final String MODE_CHANNEL = CHANNEL_GROUP_DISPLAY + "#mode";
    public static final String CURRENT_STATE_CHANNEL = CHANNEL_GROUP_DISPLAY
            + "#currentState";
    public static final String MOWTIME_CHANNEL = CHANNEL_GROUP_DISPLAY
            + "#mowTime";
    public static final String CHARGETIME_CHANNEL = CHANNEL_GROUP_DISPLAY
            + "#chargeTime";
    public static final String OPERATIONTIME_CHANNEL = CHANNEL_GROUP_DISPLAY
            + "#operationTime";
    public static final String CURRENT_DATETIME_CHANNEL = CHANNEL_GROUP_DISPLAY
            + "#currentDateTime";
    public static final String LATEST_UPDATE_TIME = CHANNEL_GROUP_DISPLAY+"#latestUpdateTime";
    
    // Timer constants
    public static final String CHANNEL_GROUP_TIMER = "timer";
    public static final String TIMER_ACTIVE_CHANNEL = CHANNEL_GROUP_TIMER
            + "#timerActiveMode";
    public static final String TIMER_WEEKDAYS_ACTIVE_CHANNEL = CHANNEL_GROUP_TIMER
            + "#timerActiveWeekdays";
    //
    public static final String TIMER_TIMER1_START = CHANNEL_GROUP_TIMER
            + "#timer1Start";
    public static final String TIMER_TIMER1_STOP = CHANNEL_GROUP_TIMER
            + "#timer1Stop";
    public static final String TIMER_TIMER2_START = CHANNEL_GROUP_TIMER
            + "#timer2Start";
    public static final String TIMER_TIMER2_STOP = CHANNEL_GROUP_TIMER
            + "#timer2Stop";
    public static final String TIMER_WETIMER1_START = CHANNEL_GROUP_TIMER
            + "#weTimer1Start";
    public static final String TIMER_WETIMER1_STOP = CHANNEL_GROUP_TIMER
            + "#weTimer1Stop";
    public static final String TIMER_WETIMER2_START = CHANNEL_GROUP_TIMER
            + "#weTimer2Start";
    public static final String TIMER_WETIMER2_STOP = CHANNEL_GROUP_TIMER
            + "#weTimer2Stop";

    // Constants for Details
    public static final String CHANNEL_GROUP_DETAILS = "details";
    public static final String RECTANGLEMODE_STATE_CHANNEL = CHANNEL_GROUP_DETAILS
            + "#rectangleModeState";
    public static final String RECTANGLEMODE_PERCENT_CHANNEL = CHANNEL_GROUP_DETAILS
            + "#rectangleModePercent";
    public static final String RECTANGLEMODE_REFERENCE_CHANNEL = CHANNEL_GROUP_DETAILS
            + "#rectangleModeReference";
    public static final String BATTERY_CAPACITY_USED_CHANNEL = CHANNEL_GROUP_DETAILS
            + "#batteryCapacityUsedMaH";
    public static final String BATTERY_CURRENT_MA_CHANNEL = CHANNEL_GROUP_DETAILS
            + "#batteryCurrentMa";
    public static final String BATTERY_CAPACITY_MAH_CHANNEL = CHANNEL_GROUP_DETAILS
            + "#batteryCapacityMaH";
    public static final String BATTERY_CAPACITY_SEARCH_START_MAH_CHANNEL = CHANNEL_GROUP_DETAILS
            + "#batteryCapacitySearchStartMaH";
    public static final String BATTERY_VOLTAGE_CHANNEL = CHANNEL_GROUP_DETAILS
            + "#batteryVoltage";
    public static final String BATTERY_TEMPERATURE_CHANNEL = CHANNEL_GROUP_DETAILS
            + "#batteryTemperature";
    public static final String BATTERY_TEMPERATURE_CHARGE_CHANNEL = CHANNEL_GROUP_DETAILS
            + "#batteryTemperatureCharge";
    public static final String BATTERY_TEMPERATURE_LATEST_CHARGE_MIN_CHANNEL = CHANNEL_GROUP_DETAILS
            + "#batteryLatestChargeMin";
    public static final String BATTERY_TEMPERATURE_NEXT_MEASUREMENT_CHANNEL = CHANNEL_GROUP_DETAILS
            + "#batteryNextTemperatureMeasurementSec";
    public static final String VELOCITY_MOTOR_CHANNEL = CHANNEL_GROUP_DETAILS
            + "#velocityMotor";
    public static final String VELOCITY_LEFT_CHANNEL = CHANNEL_GROUP_DETAILS
            + "#velocityLeft";
    public static final String VELOCITY_RIGHT_CHANNEL = CHANNEL_GROUP_DETAILS
            + "#velocityRight";
    public static final String FIRMWARE_VERSION_CHANNEL = CHANNEL_GROUP_DETAILS
            + "#firmwareVersion";
    public static final String LANGUAGE_FILE_VERSION_CHANNEL = CHANNEL_GROUP_DETAILS
            + "#languageFileVersion";

    // Used for Discovery service
    public static final String MANUFACTURER = "HUSQVARNA";
    // TODO do we need/use this
    public static final String UPNP_DEVICE_TYPE = "Automower";

    public static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = 
            Collections.unmodifiableSet(new HashSet<>(Arrays.asList(IP_AM_THING_TYPE)));

    // Mode types
    public static final String MODE_MAN = "0";
    public static final String MODE_AUTO = "1";
    public static final String MODE_HOME = "3";
    public static final String MODE_DEMO = "4";

}
