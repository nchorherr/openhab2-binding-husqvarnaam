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
package org.openhab.binding.husqvarnaam.protocol.utils;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.husqvarnaam.protocol.AmCommand;
import org.openhab.binding.husqvarnaam.protocol.AmConnectionException;

/**
 * Utility for conversion of various information of the am
 * 
 * @author Nikolaus Chorherr - Initial contribution
 */
@NonNullByDefault
public class AmInformationConverter {

    /**
     * Converts an state information to a readable String. It must be mapped
     * to the correct language (use Transformation/map and map-files)
     *
     * @param responseParameter the parameter of the ip response
     * @return the decimal number as String
     * @throws AmConnectionException
     */
    public static Integer convertStateResponse(byte[] responseParameter) {
        if (AmCommand.invalidParameterLength(responseParameter)) {
            return Integer.valueOf(-1);
        }
        return Integer.valueOf(createDecimalResponse(responseParameter));
    }

    /**
     * Flips order of parameter bytes 0<->1 byte (corresponds to byte 4 and 5 of
     * ip response) and returns 10 based as an integer
     *
     * @param responseParameter
     * @return
     */
    static int createDecimalResponse(byte[] responseParameter) {
        return HexConverter.convertAsUnsignedBytes(responseParameter);
    }

    public static Integer convertOperationTimeMessage(byte[] responseParameter)
            throws AmConnectionException {
        return Integer.valueOf(createDecimalResponse(responseParameter));
    }

    public static Integer convertMowTimeMessage(byte[] responseParameter)
            throws AmConnectionException {
        return Integer.valueOf(createDecimalResponse(responseParameter));
    }

    public static Integer convertChargeTimeMessage(byte[] responseParameter)
            throws AmConnectionException {
        return Integer.valueOf(createDecimalResponse(responseParameter));
    }

    public static Integer convertModeMessage(byte[] responseParameter)
            throws AmConnectionException {
        return Integer.valueOf(createDecimalResponse(responseParameter));
    }

    public static Integer convertBatteryCurrentMessage(
            byte[] responseParameter) {
        // TODO Check if OK on display
        return Integer
                .valueOf(HexConverter.convertAsUnsignedBytes(responseParameter));
    }

    public static Integer convertBatteryCapacityMAH(byte[] responseParameter) {
        return Integer.valueOf(
                HexConverter.convertAsUnsignedBytes(responseParameter));
    }

    public static Integer convertBatteryVoltageMV(byte[] responseParameter) {
        return Integer.valueOf(
                HexConverter.convertAsUnsignedBytes(responseParameter));
    }

    public static Integer convertBatteryTemperatureC(byte[] responseParameter) {
        return Integer.valueOf(
                HexConverter.convertAsUnsignedBytes(responseParameter));
    }

    public static Integer convertVelocity(byte[] responseParameter) {
        return Integer.valueOf(
                HexConverter.convertAsUnsignedBytes(responseParameter));
    }

    public static Integer convertPercent(byte[] responseParameter) {
        return Integer.valueOf(
                HexConverter.convertAsUnsignedBytes(responseParameter));
    }

    public static boolean convertBoolean(byte[] responseParameter) {
        return HexConverter.convertAsUnsignedBytes(responseParameter) == 1;
    }

    public static Integer convertTimerBitArray(byte[] responseParameter) {
        if (AmCommand.invalidParameterLength(responseParameter)) {
            return Integer.valueOf(-1);
        }
        return Byte.toUnsignedInt(responseParameter[0]);
    }
}

