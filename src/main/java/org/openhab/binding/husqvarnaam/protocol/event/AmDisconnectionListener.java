/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.husqvarnaam.protocol.event;

/**
 * A listener which is notified when an AM is disconnected.
 * 
 * Based on the Pioneer binding by Antoine Besnard and authors mentioned there
 * 
 * @author Nikolaus Chorherr - Initial Contribution
 *
 */
public interface AmDisconnectionListener {

    /**
     * Called when an AVR is disconnected.
     * 
     * @param event
     */
    public void onDisconnection(AmDisconnectionEvent event);

}
