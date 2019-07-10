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

import javax.measure.Unit;
import javax.measure.quantity.Energy;
import javax.measure.quantity.Frequency;
import javax.measure.quantity.Speed;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.library.unit.MetricPrefix;
import org.eclipse.smarthome.core.library.unit.SmartHomeUnits;

import tec.uom.se.format.SimpleUnitFormat;
import tec.uom.se.unit.ProductUnit;
import tec.uom.se.unit.Units;


/**
 * The {@link HusqvarnaAmBindingUnitsOfMeasurement} class defines additional Units of Measurement
 *
 * @author Nikolaus Chorherr - Initial contribution
 */
@NonNullByDefault
public final class HusqvarnaAmBindingUnitsOfMeasurement {

    public static Unit<Energy> MILLIWATT_PER_HOUR = MetricPrefix.MILLI(SmartHomeUnits.WATT_HOUR);
    public static Unit<Speed> CENTIMETER_PER_SECOND = new ProductUnit<Speed>(MetricPrefix.CENTI(Units.METRE).divide(Units.SECOND));
    public static Unit<Frequency> REVOLUTION_PER_MINUTE = SmartHomeUnits.HERTZ.divide(60.);

    static {
         SimpleUnitFormat.getInstance().label(MILLIWATT_PER_HOUR, "mWh");
         SimpleUnitFormat.getInstance().label(CENTIMETER_PER_SECOND, "cm/s");
         SimpleUnitFormat.getInstance().label(REVOLUTION_PER_MINUTE, "rpm");
    }
}
