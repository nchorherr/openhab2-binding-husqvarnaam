/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.husqvarnaam.protocol.event;

import org.openhab.binding.husqvarnaam.protocol.AmConnection;

/**
 * The event fired when a status is received from the AM.
 *
 * Based on the Pioneer binding by Antoine Besnard and authors mentioned there
 * 
 * @author Nikolaus Chorherr - Initial Contribution
 */
public class AmStatusUpdateEvent {

    private AmConnection connection;
    private byte[] response;

    public AmStatusUpdateEvent(AmConnection connection, byte[] data) {
        this.connection = connection;
        this.response = data;
    }

    public AmConnection getConnection() {
        return connection;
    }

    public byte[] getData() {
        return response;
    }

}
