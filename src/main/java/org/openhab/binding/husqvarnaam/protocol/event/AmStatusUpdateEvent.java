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
package org.openhab.binding.husqvarnaam.protocol.event;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.husqvarnaam.protocol.AmConnection;

/**
 * The event fired when a status is received from the AM.
 *
 * Based on the Pioneer binding by Antoine Besnard and authors mentioned there
 * 
 * @author Nikolaus Chorherr - Initial Contribution
 */
@NonNullByDefault
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
