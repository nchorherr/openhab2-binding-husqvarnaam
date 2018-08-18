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
import org.openhab.binding.husqvarnaam.internal.protocol.ip.IpAmConnection;

/**
 * Factory that allows to build IpControl commands/responses.
 *
 * Based on the Pioneer binding by Antoine Besnard and authors mentioned there
 * 
 * @author Nikolaus Chorherr - Initial Contribution
 *
 */
public final class RequestResponseFactory {

    /**
     * Return a connection to the AVR with the given host and port.
     *
     * @param host
     * @param port
     * @return
     */
    public static IpAmConnection getConnection(String host, Integer port) {
        return new IpAmConnection(host, port);
    }

    /**
     * Return a ParameterizedCommand of the type given in parameter and for the
     * given zone.
     *
     * @param command
     * @param zone
     * @return
     */
    public static SimpleCommand getIpControlCommand(SimpleCommandType command) {
        SimpleCommand result = new SimpleCommand(command);
        return result;
    }

    /**
     * Return a ParameterizedCommand of the type given in parameter. The
     * parameter of the command has to be set before send.
     *
     * @param command
     * @param zone
     * @return
     */
    public static ParameterizedCommand getIpControlCommand(
            ParameterizedCommandType command) {
        ParameterizedCommand result = new ParameterizedCommand(command);
        return result;
    }

    /**
     * Return a ParameterizedCommand of the type given in parameter. The
     * parameter of the command is set with the given paramter value.
     *
     * @param command
     * @param parameter
     * @param zone
     * @return
     */
    public static ParameterizedCommand getIpControlCommand(
            ParameterizedCommandType command, byte[] parameter) {
        ParameterizedCommand result = getIpControlCommand(command);
        result.setParameter(parameter);
        return result;
    }

    /**
     * Return a IpControlResponse object based on the given response data.
     *
     * @param responseData
     * @return
     */
    public static Response getIpControlResponse(byte[] responseData) {
        return new Response(responseData);
    }

}
