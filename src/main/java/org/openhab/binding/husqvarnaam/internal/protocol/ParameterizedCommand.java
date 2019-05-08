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

import org.apache.commons.lang.ArrayUtils;
import org.openhab.binding.husqvarnaam.protocol.AmCommand;
import org.openhab.binding.husqvarnaam.protocol.AmConnectionException;
import org.openhab.binding.husqvarnaam.protocol.utils.HexConverter;

/**
 * A command which accept a parameter.
 *
 * Based on the Pioneer binding by Antoine Besnard and authors mentioned there
 * 
 * @author Nikolaus Chorherr - Initial Contribution
 *
 */
public class ParameterizedCommand extends SimpleCommand {

    /**
     * List of the commands with a parameter.
     *
     * @author Antoine Besnard
     * @author Nikolaus Chorherr
     *
     */
    public enum ParameterizedCommandType implements AmCommand.CommandType {

    SET_TIMER_WEEK(new byte[]{0x0F, (byte) 0xCA, 0x50, 0x00},""),
    SET_TIMER_ACTIVE(new byte[]{0x0F, (byte) 0xCA, 0x4E, 0x00},""),
    SET_TIMER1_START(new byte[]{0x0F, (byte) 0xCA, 0x38}, ""),
    SET_TIMER1_STOP(new byte[]{0x0F, (byte) 0xCA, 0x3A},""),
    SET_TIMER2_START(new byte[]{0x0F, (byte) 0xCA, 0x40},""),
    SET_TIMER2_STOP(new byte[]{0x0F, (byte) 0xCA, 0x42},""),
    SET_WETIMER1_START(new byte[]{0x0F, (byte) 0xCA, 0x3C},""),
    SET_WETIMER1_STOP(new byte[]{0x0F, (byte) 0xCA, 0x3E},""),
    SET_WETIMER2_START(new byte[]{0x0F,(byte) 0xCA, 0x44},""),
    SET_WETIMER2_STOP(new byte[]{0x0F,(byte) 0xCA,0x46},"");

        private byte[] command;

        private String parameterPattern;

        private boolean invert = false;

        private ParameterizedCommandType(byte[] command,
                String parameterPattern, boolean invert) {
            this.command = command;
            this.parameterPattern = parameterPattern;
            this.invert = invert;
        }

        private ParameterizedCommandType(byte[] command,
                String parameterPattern) {
            this(command, parameterPattern, true);
        }

        @Override
        public byte[] getCommand() {
            return command;
        }

        public String getParameterPattern() {
            return parameterPattern;
        }

        public boolean isInvert() {
            return this.invert;
        }
    }

    private byte[] parameter;

    private String parameterPattern;

    protected ParameterizedCommand(ParameterizedCommandType command) {
        super(command);
        this.parameterPattern = command.getParameterPattern();
    }

    /**
     * Return the command to send to the AM with the parameter value configured.
     *
     * throws {@link AmConnectionException} if the parameter is null or empty
     */
    @Override
    public byte[] getCommand() throws AmConnectionException {
        if (ArrayUtils.isEmpty(parameter)) {
            throw new AmConnectionException("The parameter of the command "
                    + HexConverter.toString(super.getCommand())
                    + " must not be null or empty.");
        }
        if (((ParameterizedCommandType) super.getCommandType()).isInvert()) {
            // Parameter for timer must be in order p[0} <=> minutes p[1] <=>
            // hours
            ArrayUtils.reverse(parameter);
        }
        return ArrayUtils.addAll(super.getCommand(), parameter);
    }

    public ParameterizedCommand setParameter(byte[] parameter) {
        this.parameter = parameter;
        return this;
    }

    public byte[] getParameter() {
        return this.parameter;
    }

    public String getParameterPattern() {
        return parameterPattern;
    }
}
