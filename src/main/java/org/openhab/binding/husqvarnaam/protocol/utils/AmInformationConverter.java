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
    public static String convertStateResponse(byte[] responseParameter) {
        if (AmCommand.invalidParameterLength(responseParameter)) {
            return "invalid State";
        }
        return String.valueOf(createDecimalResponse(responseParameter));
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

    public static String convertOperationTimeMessage(byte[] responseParameter)
            throws AmConnectionException {
        return String.valueOf(createDecimalResponse(responseParameter));
    }

    public static String convertMowTimeMessage(byte[] responseParameter)
            throws AmConnectionException {
        return String.valueOf(createDecimalResponse(responseParameter));
    }

    public static String convertChargeTimeMessage(byte[] responseParameter)
            throws AmConnectionException {
        return String.valueOf(createDecimalResponse(responseParameter));
    }

    public static String convertModeMessage(byte[] responseParameter)
            throws AmConnectionException {
        return String.valueOf(createDecimalResponse(responseParameter));
    }

    public static String convertBatteryCurrentMessage(
            byte[] responseParameter) {
        return String
                .valueOf(HexConverter.convertAsSignedBytes(responseParameter));
    }

    public static String convertBatteryCapacityMAH(byte[] responseParameter) {
        return String.valueOf(
                HexConverter.convertAsUnsignedBytes(responseParameter));
    }

    public static String convertBatteryVoltageMV(byte[] responseParameter) {
        return String.valueOf(
                HexConverter.convertAsUnsignedBytes(responseParameter));
    }

    public static String convertBatteryTemperatureC(byte[] responseParameter) {
        return String.valueOf(
                HexConverter.convertAsUnsignedBytes(responseParameter));
    }

    public static String convertVelocity(byte[] responseParameter) {
        return String.valueOf(
                HexConverter.convertAsUnsignedBytes(responseParameter));
    }

    public static String convertPercent(byte[] responseParameter) {
        return String.valueOf(
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

