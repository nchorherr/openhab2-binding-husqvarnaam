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

import static org.junit.Assert.assertEquals;

import javax.measure.quantity.ElectricCharge;
import javax.measure.quantity.ElectricCurrent;
import javax.measure.quantity.Frequency;
import javax.measure.quantity.Length;
import javax.measure.quantity.Speed;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.library.types.QuantityType;
import org.eclipse.smarthome.core.library.unit.MetricPrefix;
import org.eclipse.smarthome.core.library.unit.SIUnits;
import org.eclipse.smarthome.core.library.unit.SmartHomeUnits;
import org.junit.Test;


/**
 * The {@link QuantitiesTest} class tests the newly defined custom units
 *
 * @author Nikolaus Chorherr - Initial contribution
 */
@NonNullByDefault
public class QuantitiesTest {
    
    @Test
    public void testQuantitymA() {
        // 2000 mA
        QuantityType<ElectricCurrent> q = new QuantityType<ElectricCurrent>(2000,SmartHomeUnits.AMPERE);
        @SuppressWarnings("rawtypes")
        QuantityType qNew=QuantityType.valueOf(q.toFullString());
        assertEquals(q,qNew);
        q=new QuantityType<ElectricCurrent>(2000,MetricPrefix.MILLI(SmartHomeUnits.AMPERE));
        qNew=QuantityType.valueOf(q.toFullString());
        assertEquals(q,qNew);
    }
    
    @Test
    public void testQuantitymm() {
        // 2000 mm
        QuantityType<Length> q = new QuantityType<Length>(2000,SIUnits.METRE);
        @SuppressWarnings("rawtypes")
        QuantityType qNew=QuantityType.valueOf(q.toFullString());
        assertEquals(q,qNew);
        q=new QuantityType<Length>(2000,MetricPrefix.MILLI(SIUnits.METRE));
        qNew=QuantityType.valueOf(q.toFullString());
        assertEquals(q,qNew);
    }
    
    @Test
    public void testUnitsmWh() {
// mAh
        QuantityType<ElectricCharge> q=new QuantityType<ElectricCharge>(2000,HusqvarnaAmBindingUnitsOfMeasurement.MILLIAMPERE_HOUR);
        @SuppressWarnings("rawtypes")
        QuantityType qNew=QuantityType.valueOf(q.toFullString());
        assertEquals(q,qNew);
    }
    
    @Test
    public void testUnitscmPers() {
// cm/s
        QuantityType<Speed> q1=new QuantityType<Speed>(2000,HusqvarnaAmBindingUnitsOfMeasurement.CENTIMETER_PER_SECOND);
        @SuppressWarnings("rawtypes")
        QuantityType qNew1=QuantityType.valueOf(q1.toFullString());
        assertEquals(q1,qNew1);
    }
    
    @Test
    public void testUnitsRpm() {
// rpm
        QuantityType<Frequency> q2=new QuantityType<Frequency>(2000,HusqvarnaAmBindingUnitsOfMeasurement.REVOLUTION_PER_MINUTE);
        @SuppressWarnings("rawtypes")
        QuantityType qNew2=QuantityType.valueOf(q2.toFullString());
        assertEquals(q2,qNew2);
    }

}
