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
package org.openhab.binding.husqvarnaam.internal.protocol;

import org.openhab.binding.husqvarnaam.protocol.AmCommand;

/**
 * A simple command without parameters.
 *
 * Based on the Pioneer binding by Antoine Besnard and authors mentioned there
 * 
 * @author Nikolaus Chorherr - Initial Contribution
 *
 */
public class SimpleCommand implements AmCommand {

    /**
     * List of the simple command types.
     *
     * @author Nikolaus Chorherr
     *
     */
    public enum SimpleCommandType implements AmCommand.CommandType {

    STATE_QUERY(new byte[]{0x0F, 0x01, (byte) 0xF1}),
    CHARGETIME_QUERY(new byte[]{0x0F, 0x01, (byte) 0xEC}),
    // current date time (changed to R_ values)
    CURRENT_DATE_TIME_SEC_QUERY(new byte[]{0x0F,0x36,(byte) 0xB1}),
    CURRENT_DATE_TIME_MIN_QUERY(new byte[]{0x0F,0x36,(byte) 0xB3}),
    CURRENT_DATE_TIME_HOUR_QUERY(new byte[]{0x0F,0x36,(byte) 0xB5}),
    // CURRENT_DATE_TIME_DAYOFWEEK_QUERY(new byte[] { 0x0F, 0x36, 0xB3 }), //
    // minute
    CURRENT_DATE_TIME_DAY_QUERY(new byte[]{0x0F,0x36,(byte) 0xB7}),
    CURRENT_DATE_TIME_MONTH_QUERY(new byte[]{0x0F,0x36,(byte) 0xB9}),
    CURRENT_DATE_TIME_YEAR_QUERY(new byte[]{0x0F,0x36,(byte) 0xBD}),
    CURRENT_MODE_QUERY(new byte[]{0x0F,(byte) 0x01, 0x2C}), // might not work
    // Set mode
    SET_MODE_MAN(new byte[]{0x0F,(byte) 0x81,0x2C,0x00,0x00}),
    SET_MODE_AUTO(new byte[]{0x0F,(byte) 0x81,0x2C,0x00,0x01}),
    SET_MODE_HOME(new byte[]{0x0F,(byte) 0x81,0x2C,0x00,0x03}),
    SET_MODE_DEMO(new byte[]{0x0F,(byte) 0x81, 0x2C,0x00,0x04}),
    // which one
    MOWTIME_QUERY(new byte[]{0x0F, 0x00, 0x56}),
    R_MAEHZEIT(new byte[]{0x0F, 0x00, 0x38}),
    // does it exist ??
    OPERATIONTIME_QUERY(new byte[]{0x0F, 0x46, (byte) 0x98}),

    RECTANGLE_MODE_STATE_QUERY(new byte[]{0x0F,0x01,0x38}),
    RECTANGLE_MODE_PERCENT_QUERY(new byte[]{0x0F,0x01,0x34}),
    RECTANGLE_MODE_REFERENCE_QUERY(new byte[]{0x0F,0x01,0x37}),
    CURRENT_BATTERY_CAPACITY_MA_QUERY(new byte[]{0x0F,0x01,(byte)0xEB}),
    CURRENT_BATTERY_CAPACITY_MAH_QUERY(new byte[]{0x0F,0x01,(byte)0xEF}),
    CURRENT_BATTERY_CAPACITY_SEARCH_START_MAH_QUERY(new byte[]{0x0F,0x01,(byte)0xF0}),
    CURRENT_BATTERY_CAPACITY_USED_MAH_QUERY(new byte[]{0x0F,0x2E,(byte)0xE0}),
    CURRENT_BATTERY_VOLTAGE_MV_QUERY(new byte[]{0x0F,0x2E,(byte)0xF4}),
    CURRENT_BATTERY_TEMPERATURE_QUERY(new byte[]{0x0F,0x02,0x33}),
    CURRENT_BATTERY_TEMPERATURE_CHARGE_QUERY(new byte[]{0x0F,0x02,0x35}),
    CURRENT_BATTERY_LAST_CHARGE_MIN_QUERY(new byte[]{0x0F,0x02,0x34}),
    CURRENT_BATTERY_NEXT_TEMPERATURE_MEASUREMENT_SEC_QUERY(new byte[]{0x0F,0x02,0x36}),
    CURRENT_VELOCITY_MOTOR_QUERY(new byte[]{0x0F,0x2E,(byte)0xEA}),
    CURRENT_VELOCITY_RIGHT_QUERY(new byte[]{0x0F,0x24,(byte)0xBF}),
    CURRENT_VELOCITY_LEFT_QUERY(new byte[]{0x0F,0x24,(byte)0xC0}),
    FIRMWARE_VERSION_QUERY(new byte[]{0x0F, 0x33, (byte) 0x90}),
    LANGUAGE_FILE_VERSION_QUERY(new byte[]{0x0F, 0x3A, (byte) 0xC0}),
    // All Timer Infos must be consolidated
    TIMER_DAYSOFWEEK_QUERY(new byte[]{0x0F, 0x4A, 0x50}),
    TIMER_ACTIVE_QUERY(new byte[]{0x0F,0x4A,0x4E}), 
    SET_TIMER_ACTIVE(new byte[]{0x0F,(byte) 0xCA,0x4E,0x00,0x00}), 
    SET_TIMER_INACTIVE(new byte[]{0x0F,(byte) 0xCA,0x4E,0x00,0x01}),
    TIMER1_START_QUERY(new byte[]{0x0F, 0x4A, 0x38}), 
    TIMER1_STOP_QUERY(new byte[]{0x0F, 0x4A, 0x3A}), 
    TIMER2_START_QUERY(new byte[]{0x0F, 0x4A, 0x40}), 
    TIMER2_STOP_QUERY(new byte[]{0x0F, 0x4A, 0x42}), 
    WE_TIMER1_START_QUERY(new byte[]{0x0F,0x4A,0x3C}), 
    WE_TIMER1_STOP_QUERY(new byte[]{0x0F, 0x4A,0x3E}), 
    WE_TIMER2_START_QUERY(new byte[]{0x0F,0x4A,0x44}), 
    WE_TIMER2_STOP_QUERY(new byte[]{0x0F,0x4A,0x46}), 
    SET_KEY_YES(new byte[]{0x0F,(byte) 0x80,0x5F,0x00,0x12});

        /*
         * Pressing Buttons will not be implemented
         *
         * W_KEY_0("0F805F0000", IpControlCommandRef.W_KEY_0),
         * W_KEY_1("0F805F0001", IpControlCommandRef.W_KEY_1),
         * W_KEY_2("0F805F0002", IpControlCommandRef.W_KEY_2),
         * W_KEY_3("0F805F0003", IpControlCommandRef.W_KEY_3),
         * W_KEY_4("0F805F0004", IpControlCommandRef.W_KEY_4),
         * W_KEY_5("0F805F0005", IpControlCommandRef.W_KEY_5),
         * W_KEY_6("0F805F0006", IpControlCommandRef.W_KEY_6),
         * W_KEY_7("0F805F0007", IpControlCommandRef.W_KEY_7),
         * W_KEY_8("0F805F0008", IpControlCommandRef.W_KEY_8),
         * W_KEY_9("0F805F0009", IpControlCommandRef.W_KEY_9),
         * W_PRG_A("0F805F000A", IpControlCommandRef.W_PRG_A),
         * W_PRG_B("0F805F000B", IpControlCommandRef.W_PRG_B),
         * W_PRG_C("0F805F000C", IpControlCommandRef.W_PRG_C),
         * W_KEY_HOME("0F805F000D", IpControlCommandRef.W_KEY_HOME),
         * W_KEY_MANAUTO("0F805F000E", IpControlCommandRef.W_KEY_MANAUTO),
         * W_KEY_C("0F805F000F", IpControlCommandRef.W_KEY_C),
         * W_KEY_UP("0F805F0010", IpControlCommandRef.W_KEY_UP),
         * W_KEY_DOWN("0F805F0011", IpControlCommandRef.W_KEY_DOWN),
         * W_KEY_YES("0F805F0012", IpControlCommandRef.W_KEY_YES);
         */

        private byte[] command;

        private SimpleCommandType(byte[] ch) {
            this.command = ch;
        }

        @Override
        public byte[] getCommand() {
            return command;
        }
    }

    private CommandType commandType;

    public SimpleCommand(CommandType commandType) {
        this.commandType = commandType;
    }

    @Override
    public byte[] getCommand() {
        return commandType.getCommand();
    }

    @Override
    public CommandType getCommandType() {
        return commandType;
    }

}
