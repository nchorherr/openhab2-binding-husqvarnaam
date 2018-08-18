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
