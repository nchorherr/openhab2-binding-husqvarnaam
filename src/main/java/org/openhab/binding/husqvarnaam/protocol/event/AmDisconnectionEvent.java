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
 * An event fired when an AM is disconnected.
 *
 * Based on the Pioneer binding by Antoine Besnard and authors mentioned there
 * 
 * @author Nikolaus Chorherr - Initial Contribution
 *
 */
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
