/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.husqvarnaam.internal.handler;

import org.eclipse.smarthome.core.thing.Thing;
import org.openhab.binding.husqvarnaam.HusqvarnaAmBindingConstants;
import org.openhab.binding.husqvarnaam.internal.protocol.ip.IpAmConnection;
import org.openhab.binding.husqvarnaam.protocol.AmConnection;

/**
 * An handler of an AM connected through an IP connection.
 *
 * Based on the Pioneer binding by Antoine Besnard and authors mentioned there
 * 
 * @author Nikolaus Chorherr - Initial Contribution
 *
 */
public class IpAmHandler extends AbstractAmHandler {

    public IpAmHandler(Thing thing) {
        super(thing);
    }

    @Override
    protected AmConnection createConnection() {
        String host = (String) this.getConfig()
                .get(HusqvarnaAmBindingConstants.HOST_PARAMETER);
        Integer tcpPort = ((Number) this.getConfig()
                .get(HusqvarnaAmBindingConstants.TCP_PORT_PARAMETER))
                        .intValue();

        return new IpAmConnection(host, tcpPort);
    }

}
