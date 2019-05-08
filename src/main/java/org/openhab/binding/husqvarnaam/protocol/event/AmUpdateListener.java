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

import java.util.EventListener;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.husqvarnaam.protocol.event.AmStatusUpdateEvent;

/**
 * This interface defines interface to receive status updates from am.
 * 
 * Based on the Pioneer binding by Antoine Besnard and authors mentioned there
 * 
 * @author Nikolaus Chorherr - Initial contribution
 */
@NonNullByDefault
public interface AmUpdateListener extends EventListener {

    /**
     * Procedure for receive status update from Pioneer receiver.
     */
    public void statusUpdateReceived(AmStatusUpdateEvent event);

}
