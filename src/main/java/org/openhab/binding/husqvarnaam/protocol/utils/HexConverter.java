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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Formatter;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.husqvarnaam.protocol.AmCommand;

/**
 *
 * Utility Class for conversion of string <-> hex code
 * 
 * @author Nikolaus Chorherr - Initial contribution
 *
 */
@NonNullByDefault
public class HexConverter {

    public static String toString(byte[] hexCode) {
        if (hexCode == null || hexCode.length == 0) {
            return "";
        }
        Formatter f = new Formatter(new StringBuilder());
        for (int i = 0; i < hexCode.length - 1; i++) {
            f.format("0x%02X,", hexCode[i]);
        }
        f.format("0x%02X", hexCode[hexCode.length - 1]);
        String ret=f.toString();
        f.close();
        return ret;
    }

    public static int convertAsUnsignedBytes(byte[] hexCode) {
        assert hexCode.length == 2;
        return Byte.toUnsignedInt(hexCode[0])
                + (Byte.toUnsignedInt(hexCode[1]) << 8);
    }

    public static int convertAsSignedBytes(byte[] hexCode) {
        return ByteBuffer.wrap(hexCode).order(ByteOrder.LITTLE_ENDIAN)
                .getShort();
    }

    /**
    * Combines [1] and byte [0]
    *
    * @param responseParameter
    * @return
    */
    public static String convertHex(byte[] responseParameter) {
        String ret = null;
        if (!AmCommand.invalidParameterLength(responseParameter)) {
           ret = String.valueOf(AmInformationConverter.createDecimalResponse(responseParameter));
        } else {
           ret = "N/A";
        }
        ret += " (" + toString(responseParameter) + ")";
        return ret;
    }

}

