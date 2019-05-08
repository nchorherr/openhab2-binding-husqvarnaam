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

import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.TimeZone;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.husqvarnaam.protocol.AmCommand;

/**
 * Utility class for conversion of date/time information
 * 
 * @author Nikolaus Chorherr - Initial contribution
 */
@NonNullByDefault
public abstract class DateTimeConverter {

    public static int convertSecond(byte[] responseParameter) {
        return createDecimalResponse(responseParameter);
    }

    public static int convertMinute(byte[] responseParameter) {
        return createDecimalResponse(responseParameter);
    }

    public static int convertHour(byte[] responseParameter) {
        return createDecimalResponse(responseParameter);
    }

    public static int convertDay(byte[] responseParameter) {
        return createDecimalResponse(responseParameter);
    }

    public static int convertMonth(byte[] responseParameter) {
        return createDecimalResponse(responseParameter);
    }

    public static int convertYear(byte[] responseParameter) {
        return createDecimalResponse(responseParameter);
    }

    private static int createDecimalResponse(byte[] responseParameter) {
        return Byte.toUnsignedInt(responseParameter[0]);
    }
    
    /**
     * When creating a command the byte order is 1,0 same as response!
     * 
     * @param dateTimeParameter
     * @return
     */
    public static byte[] convert(ZonedDateTime zonedDateTime) {
            byte[] time = new byte[2];
            time[1] = (byte) zonedDateTime.getHour();
            time[0] = (byte) zonedDateTime.getMinute();
            return time;
        }

    /**
     * Uses parameter [0] as value for hour and parameter [1] as value for
     * minute
     *
     * @param responseParameter
     * @return
     */
    public static ZonedDateTime convertDateTime(byte[] responseParameter) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        if (AmCommand.invalidParameterLength(responseParameter)) {
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
        } else {
            cal.set(Calendar.HOUR_OF_DAY,
                    Byte.toUnsignedInt(responseParameter[0]));
            cal.set(Calendar.MINUTE, Byte.toUnsignedInt(responseParameter[1]));
    
        }
        return ZonedDateTime.ofInstant(cal.toInstant(), TimeZone.getDefault().toZoneId())
            .withFixedOffsetZone();
    }

    }

