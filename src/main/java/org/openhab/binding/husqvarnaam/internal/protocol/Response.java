/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.husqvarnaam.internal.protocol;

import org.openhab.binding.husqvarnaam.internal.protocol.ParameterizedCommand.ParameterizedCommandType;
import org.openhab.binding.husqvarnaam.internal.protocol.SimpleCommand.SimpleCommandType;
import org.openhab.binding.husqvarnaam.protocol.AmCommand;
import org.openhab.binding.husqvarnaam.protocol.AmConnectionException;
import org.openhab.binding.husqvarnaam.protocol.AmResponse;
import org.openhab.binding.husqvarnaam.protocol.utils.HexConverter;

/**
 * Represents an AM response.
 *
 * Based on the Pioneer binding by Antoine Besnard and authors mentioned there
 * 
 * @author Nikolaus Chorherr - Initial Contribution
 *
 */
public class Response implements AmResponse {

    /**
     * List of all supported responses coming from AM.
     *
     * @author Nikolaus Chorherr
     *
     */
    public enum ResponseType implements AmResponse.ResponseType {

    STATUS_UPDATE(SimpleCommandType.STATE_QUERY.getCommand()),
    MOW_TIME_UPDATE(SimpleCommandType.MOWTIME_QUERY.getCommand()),
    CHARGE_TIME_UPDATE(SimpleCommandType.CHARGETIME_QUERY.getCommand()),
    OPERATION_TIME_UPDATE(SimpleCommandType.OPERATIONTIME_QUERY.getCommand(),2),
    CURRENT_MODE_UPDATE(SimpleCommandType.CURRENT_MODE_QUERY.getCommand()),
    //
    CURRENT_DT_SECOND_UPDATE(SimpleCommandType.CURRENT_DATE_TIME_SEC_QUERY.getCommand()),
    CURRENT_DT_MINUTE_UPDATE(SimpleCommandType.CURRENT_DATE_TIME_MIN_QUERY.getCommand()),
    CURRENT_DT_HOUR_UPDATE(SimpleCommandType.CURRENT_DATE_TIME_HOUR_QUERY.getCommand()),
    CURRENT_DT_DAY_UPDATE(SimpleCommandType.CURRENT_DATE_TIME_DAY_QUERY.getCommand()),
    CURRENT_DT_MONTH_UPDATE(SimpleCommandType.CURRENT_DATE_TIME_MONTH_QUERY.getCommand()),
    CURRENT_DT_YEAR_UPDATE(SimpleCommandType.CURRENT_DATE_TIME_YEAR_QUERY.getCommand()),
    //
    // Details
    CURRENT_BATTERY_CAPACITY_USED_MAH_UPDATE(SimpleCommandType.CURRENT_BATTERY_CAPACITY_USED_MAH_QUERY.getCommand()),
    RECTANGLE_MODE_STATE_UPDATE(SimpleCommandType.RECTANGLE_MODE_STATE_QUERY.getCommand()),
    RECTANGLE_MODE_PERCENT_UPDATE(SimpleCommandType.RECTANGLE_MODE_PERCENT_QUERY.getCommand()),
    RECTANGLE_MODE_REFERENCE_UPDATE(SimpleCommandType.RECTANGLE_MODE_REFERENCE_QUERY.getCommand()),
    CURRENT_BATTERY_CAPACITY_MA_UPDATE(SimpleCommandType.CURRENT_BATTERY_CAPACITY_MA_QUERY.getCommand()),
    CURRENT_BATTERY_CAPACITY_MAH_UPDATE(SimpleCommandType.CURRENT_BATTERY_CAPACITY_MAH_QUERY.getCommand()),
    CURRENT_BATTERY_CAPACITY_SEARCH_START_MAH_UPDATE(SimpleCommandType.CURRENT_BATTERY_CAPACITY_SEARCH_START_MAH_QUERY.getCommand()),
    CURRENT_BATTERY_VOLTAGE_MV_UPDATE(SimpleCommandType.CURRENT_BATTERY_VOLTAGE_MV_QUERY.getCommand()),
    CURRENT_BATTERY_TEMPERATURE_UPDATE(SimpleCommandType.CURRENT_BATTERY_TEMPERATURE_QUERY.getCommand()),
    CURRENT_BATTERY_TEMPERATURE_CHARGE_UPDATE(SimpleCommandType.CURRENT_BATTERY_TEMPERATURE_CHARGE_QUERY.getCommand()),
    CURRENT_BATTERY_LAST_CHARGE_MIN_UPDATE(SimpleCommandType.CURRENT_BATTERY_LAST_CHARGE_MIN_QUERY.getCommand()),
    CURRENT_BATTERY_NEXT_TEMPERATURE_MEASUREMENT_SEC_UPDATE(SimpleCommandType.CURRENT_BATTERY_NEXT_TEMPERATURE_MEASUREMENT_SEC_QUERY.getCommand()),
    CURRENT_VELOCITY_MOTOR_UPDATE(SimpleCommandType.CURRENT_VELOCITY_MOTOR_QUERY.getCommand()),
    CURRENT_VELOCITY_RIGHT_UPDATE(SimpleCommandType.CURRENT_VELOCITY_RIGHT_QUERY.getCommand()),
    CURRENT_VELOCITY_LEFT_UPDATE(SimpleCommandType.CURRENT_VELOCITY_LEFT_QUERY.getCommand()),
    FIRMWARE_VERSION_UPDATE(SimpleCommandType.FIRMWARE_VERSION_QUERY.getCommand(),2),
    LANGUAGE_FILE_VERSION_UPDATE(SimpleCommandType.LANGUAGE_FILE_VERSION_QUERY.getCommand()),
//Timers
    SET_TIMER_WEEK_UPDATE(ParameterizedCommandType.SET_TIMER_WEEK.getCommand()),
    SET_TIMER1_START_UPDATE(ParameterizedCommandType.SET_TIMER1_START.getCommand()),
    SET_TIMER1_STOP_UPDATE(ParameterizedCommandType.SET_TIMER1_STOP.getCommand()),
    SET_TIMER2_START_UPDATE(ParameterizedCommandType.SET_TIMER2_START.getCommand()),
    SET_TIMER2_STOP_UPDATE(ParameterizedCommandType.SET_TIMER2_STOP.getCommand()),
    SET_WETIMER1_START_UPDATE(ParameterizedCommandType.SET_WETIMER1_START.getCommand()),
    SET_WETIMER1_STOP_UPDATE(ParameterizedCommandType.SET_WETIMER1_STOP.getCommand()),
    SET_WETIMER2_START_UPDATE(ParameterizedCommandType.SET_WETIMER2_START.getCommand()),
    SET_WETIMER2_STOP_UPDATE(ParameterizedCommandType.SET_WETIMER2_STOP.getCommand()),
    SET_TIMER_ACTIVE_UPDATE(ParameterizedCommandType.SET_TIMER_ACTIVE.getCommand()),
    TIMER_ACTIVE_UPDATE(SimpleCommandType.TIMER_ACTIVE_QUERY.getCommand()),
    TIMER_WEEK_UPDATE(SimpleCommandType.TIMER_DAYSOFWEEK_QUERY.getCommand()),
    TIMER1_START_UPDATE(SimpleCommandType.TIMER1_START_QUERY.getCommand()),
    TIMER1_STOP_UPDATE(SimpleCommandType.TIMER1_STOP_QUERY.getCommand()),
    TIMER2_START_UPDATE(SimpleCommandType.TIMER2_START_QUERY.getCommand()),
    TIMER2_STOP_UPDATE(SimpleCommandType.TIMER2_STOP_QUERY.getCommand()),
    WETIMER1_START_UPDATE(SimpleCommandType.WE_TIMER1_START_QUERY.getCommand()),
    WETIMER1_STOP_UPDATE(SimpleCommandType.WE_TIMER1_STOP_QUERY.getCommand()),
    WETIMER2_START_UPDATE(SimpleCommandType.WE_TIMER2_START_QUERY.getCommand()),
    WETIMER2_STOP_UPDATE(SimpleCommandType.WE_TIMER2_STOP_QUERY.getCommand()),
    //
    IAMALIVE(new byte[]{0x00, 0x00, 0x00});

        private byte[] responseData;
        private int comparableLength = AmCommand.COMMAND_LENGTH;

        private ResponseType(byte[] response) {
            this.responseData = response;
        }

        private ResponseType(byte[] response, int comparableLength) {
            this.responseData = response;
            this.comparableLength = comparableLength;
        }

        @Override
        public boolean hasParameter() {
            // TODO implement correctly
            // return StringUtils.isNotEmpty(parameterPattern);
            // Is there a response without parameter?
            return true;
        }

        @Override
        public boolean match(byte[] responseData) {
            if (responseData == null
                    || responseData.length < comparableLength) {
                return false;
            }
            boolean matched = true;
            for (int i = 0; i < comparableLength; i++) {
                matched &= (this.responseData[i] == responseData[i]);
            }
            return matched;
        }

        /**
         * Return the parameter value of the given responseData.
         *
         * @param responseData
         * @return
         */
        @Override
        public byte[] parseParameter(byte[] responseData) {
            // although we can compare only comparable length
            // we assume that there must be a total 5 - 3 bytes as parameter
            // so bytes 4 and 5 are parameter
            if (responseData == null
                    || responseData.length <= AmCommand.COMMAND_LENGTH) {
                return new byte[]{};
            }
            byte[] parameter = new byte[responseData.length
                    - AmCommand.COMMAND_LENGTH];
            for (int i = 0; i < responseData.length
                    - AmCommand.COMMAND_LENGTH; i++) {
                parameter[i] = responseData[AmCommand.COMMAND_LENGTH + i];
            }
            return parameter;
        }
    }

    private ResponseType responseType;

    private byte[] parameter;

    public Response(byte[] responseData) throws AmConnectionException {
        if (responseData == null || responseData.length == 0) {
            throw new AmConnectionException(
                    "responseData is empty. Cannot parse the response.");
        }

        parseResponseType(responseData);

        if (this.responseType == null) {
            throw new AmConnectionException(
                    "Cannot find the responseType of the responseData "
                            + HexConverter.toString(responseData));
        }

        if (this.responseType.hasParameter()) {
            this.parameter = this.responseType.parseParameter(responseData);
        }
    }

    private void parseResponseType(byte[] responseData) {
        this.responseType = null;
        // first 'comparable' bytes must match
        for (ResponseType responseType : ResponseType.values()) {
            if (responseType.match(responseData)) {
                this.responseType = responseType;
                break;
            }
        }
    }

    @Override
    public ResponseType getResponseType() {
        return this.responseType;
    }

    @Override
    public byte[] getParameterValue() {
        return parameter;
    }

    @Override
    public boolean hasParameter() {
        return responseType.hasParameter();
    }

}
