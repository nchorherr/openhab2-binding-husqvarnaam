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
 * An event fired when an AM is disconnected.
 *
 * Based on the Pioneer binding by Antoine Besnard and authors mentioned there
 * 
 * @author Nikolaus Chorherr - Initial Contribution
 *
 */
@NonNullByDefault
public class AmDisconnectionEvent {

    private AmConnection connection;
    private Throwable cause;

    public AmDisconnectionEvent(AmConnection connection, Throwable cause) {
        this.connection = connection;
        this.cause = cause;
    }

    public AmConnection getConnection() {
        return connection;
    }

    public Throwable getCause() {
        return cause;
    }

}
