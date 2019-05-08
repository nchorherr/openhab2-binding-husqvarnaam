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

import org.eclipse.smarthome.core.thing.Thing;
import org.openhab.binding.husqvarnaam.protocol.AmConnection;

/**
 * An handler of an AM connected through a serial port.
 *
 * Based on the Pioneer binding by Antoine Besnard and authors mentioned there
 * 
 * @author Nikolaus Chorherr - Initial contribution
 *
 */
public class SerialAmHandler extends AbstractAmHandler {

    public SerialAmHandler(Thing thing) {
        super(thing);
    }

    @Override
    protected AmConnection createConnection() {
        // String serialPort = (String)
        // this.getConfig().get(HusqvarnaAmBindingConstants.SERIAL_PORT_PARAMETER);
        //
        // return new SerialAmConnection(serialPort);
        throw new UnsupportedOperationException();
    }

}
