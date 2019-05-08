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
package org.openhab.binding.husqvarnaam.internal.handler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.openhab.binding.husqvarnaam.HusqvarnaAmBindingConstants;
import org.osgi.service.component.ComponentContext;

/**
 * The {@link AmHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * Based on the Pioneer binding by Antoine Besnard and authors mentioned there
 * 
 * @author Nikolaus Chorherr - Initial contribution
 */
public class AmHandlerFactory extends BaseThingHandlerFactory {

                     // HusqvarnaAmBindingConstants.SERIAL_AVR_THING_TYPE,
    private static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = 
            new HashSet<>(Arrays.asList(HusqvarnaAmBindingConstants.IP_AM_THING_TYPE, HusqvarnaAmBindingConstants.IP_AM_UNSUPPORTED_THING_TYPE));

    protected void activate(ComponentContext componentContext,
            Map<String, Object> configProps) {
        super.activate(componentContext);
    }

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (thingTypeUID.equals(HusqvarnaAmBindingConstants.IP_AM_THING_TYPE)
                || thingTypeUID.equals(
                        HusqvarnaAmBindingConstants.IP_AM_UNSUPPORTED_THING_TYPE)) {
            return new IpAmHandler(thing);
            // } else if
            // (thingTypeUID.equals(HusqvarnaAmBindingConstants.SERIAL_AVR_THING_TYPE))
            // {
            // return new SerialAmHandler(thing);
        }

        return null;
    }
}
