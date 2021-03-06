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
package org.openhab.binding.husqvarnaam.protocol;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * The base interface for an AM command.
 *
 * Based on the Pioneer binding by Antoine Besnard and authors mentioned there
 * 
 * @author Nikolaus Chorherr - Initial Contribution
 *
 */
@NonNullByDefault
public interface AmCommand {

    /**
     * The standard total command length for Husqvarna AM in bytes
     *
     */
    public static final int TOTAL_COMMAND_LENGTH = 5;

    /**
     * The distinguishable comand length for Husqvarna AM in bytes
     */
    public static final int COMMAND_LENGTH = 3;

    /**
     * Null Byte for communication with AM
     */
    public static final byte FILL_BYTE = 0x00;
    
    /**
     * Checks on invalid response parameter array length
     * @param responseParameter
     * @return
     */
    static boolean invalidParameterLength(byte[] responseParameter) {
        return responseParameter == null
                || responseParameter.length < AmCommand.TOTAL_COMMAND_LENGTH
                        - AmCommand.COMMAND_LENGTH;
    }


    /**
     * Represent a CommandType of command requests
     *
     * @author Antoine Besnard
     * @author Nikolaus Chorherr
     *
     */
    @NonNullByDefault
    public interface CommandType {

        /**
         * Return the command of this command type.
         *
         *
         * @return
         */
        public byte[] getCommand();

        /**
         * Return the name of the command type
         *
         * @return
         */
        public String name();
    }

    /**
     * Return the command to send to the AM.
     *
     * @return
     */
    public byte[] getCommand();

    /**
     * Return the the command type of this command.
     *
     * @return
     */
    public CommandType getCommandType();

}
