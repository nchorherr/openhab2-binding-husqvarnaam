/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.husqvarnaam.protocol.event;

import java.util.EventListener;

import org.openhab.binding.husqvarnaam.protocol.event.AmStatusUpdateEvent;

/**
 * This interface defines interface to receive status updates from am.
 * 
 * Based on the Pioneer binding by Antoine Besnard and authors mentioned there
 * 
 * @author Nikolaus Chorherr - Initial contribution
 */
public interface AmUpdateListener extends EventListener {

    /**
     * Procedure for receive status update from Pioneer receiver.
     */
    public void statusUpdateReceived(AmStatusUpdateEvent event);

}
