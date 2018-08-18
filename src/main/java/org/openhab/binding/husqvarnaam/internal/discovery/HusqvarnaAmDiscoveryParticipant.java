/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.husqvarnaam.internal.discovery;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder;
import org.eclipse.smarthome.config.discovery.UpnpDiscoveryParticipant;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.jupnp.model.meta.RemoteDevice;
import org.openhab.binding.husqvarnaam.HusqvarnaAmBindingConstants;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Collections2;

/**
 * An UpnpDiscoveryParticipant which allows to discover Husqvarna AMs. 
 * TODO
 * check if AM supports UPNP
 *
 * Based on the Pioneer binding by Antoine Besnard and authors mentioned there
 * 
 * @author Nikolaus Chorherr - Initial contribution
 *
 */
public class HusqvarnaAmDiscoveryParticipant
        implements
            UpnpDiscoveryParticipant {

    private Logger logger = LoggerFactory
            .getLogger(HusqvarnaAmDiscoveryParticipant.class);

    private boolean isAutoDiscoveryEnabled;
    private Set<ThingTypeUID> supportedThingTypes;

    public HusqvarnaAmDiscoveryParticipant() {
        this.isAutoDiscoveryEnabled = true;
        this.supportedThingTypes = HusqvarnaAmBindingConstants.SUPPORTED_THING_TYPES_UIDS;
    }

    /**
     * Called at the service activation.
     *
     * @param componentContext
     */
    protected void activate(ComponentContext componentContext) {
        if (componentContext.getProperties() != null) {
            String autoDiscoveryPropertyValue = (String) componentContext
                    .getProperties().get("enableAutoDiscovery");
            if (StringUtils.isNotEmpty(autoDiscoveryPropertyValue)) {
                isAutoDiscoveryEnabled = Boolean
                        .valueOf(autoDiscoveryPropertyValue);
            }
        }
        supportedThingTypes = isAutoDiscoveryEnabled
                ? HusqvarnaAmBindingConstants.SUPPORTED_THING_TYPES_UIDS
                : new HashSet<ThingTypeUID>();
    }

    @Override
    public Set<ThingTypeUID> getSupportedThingTypeUIDs() {
        return supportedThingTypes;
    }

    @Override
    public DiscoveryResult createResult(RemoteDevice device) {
        DiscoveryResult result = null;
        ThingUID thingUid = getThingUID(device);
        if (thingUid != null) {
            String label = StringUtils
                    .isEmpty(device.getDetails().getFriendlyName())
                            ? device.getDisplayString()
                            : device.getDetails().getFriendlyName();
            Map<String, Object> properties = new HashMap<>(2, 1);
            properties.put(HusqvarnaAmBindingConstants.HOST_PARAMETER,
                    device.getIdentity().getDescriptorURL().getHost());
            properties.put(HusqvarnaAmBindingConstants.PROTOCOL_PARAMETER,
                    HusqvarnaAmBindingConstants.IP_PROTOCOL_NAME);
            result = DiscoveryResultBuilder.create(thingUid).withLabel(label)
                    .withProperties(properties).build();
        }

        return result;
    }

    @Override
    public ThingUID getThingUID(RemoteDevice device) {
        ThingUID result = null;
        if (isAutoDiscoveryEnabled) {
            if (StringUtils.containsIgnoreCase(
                    device.getDetails().getManufacturerDetails()
                            .getManufacturer(),
                    HusqvarnaAmBindingConstants.MANUFACTURER)) {
                logger.debug(
                        "Manufacturer matched: search: {}, device value: {}.",
                        HusqvarnaAmBindingConstants.MANUFACTURER,
                        device.getDetails().getManufacturerDetails()
                                .getManufacturer());
                if (StringUtils.containsIgnoreCase(device.getType().getType(),
                        HusqvarnaAmBindingConstants.UPNP_DEVICE_TYPE)) {
                    logger.debug(
                            "Device type matched: search: {}, device value: {}.",
                            HusqvarnaAmBindingConstants.UPNP_DEVICE_TYPE,
                            device.getType().getType());

                    String deviceModel = device.getDetails()
                            .getModelDetails() != null
                                    ? device.getDetails().getModelDetails()
                                            .getModelName()
                                    : null;
                    ThingTypeUID thingTypeUID = HusqvarnaAmBindingConstants.IP_AM_THING_TYPE;
                    if (!isSupportedDeviceModel(deviceModel)) {
                        logger.debug(
                                "Device model {} not supported. Odd behaviors may happen.",
                                deviceModel);
                        thingTypeUID = HusqvarnaAmBindingConstants.IP_AM_UNSUPPORTED_THING_TYPE;
                    }
                    result = new ThingUID(thingTypeUID, device.getIdentity()
                            .getUdn().getIdentifierString());
                }
            }
        }
        return result;
    }

    /**
     * Return true only if the given device model is supported.
     *
     * @param deviceModel
     * @return
     */
    private boolean isSupportedDeviceModel(final String deviceModel) {
        return StringUtils.isNotBlank(deviceModel) && !Collections2
                .filter(HusqvarnaAmBindingConstants.SUPPORTED_DEVICE_MODELS,
                        new com.google.common.base.Predicate<String>() {
                            @Override
                            public boolean apply(String input) {
                                return StringUtils.startsWithIgnoreCase(
                                        deviceModel, input);
                            }
                        })
                .isEmpty();
    }
}
