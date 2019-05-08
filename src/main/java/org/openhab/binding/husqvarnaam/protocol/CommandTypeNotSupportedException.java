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
package org.openhab.binding.husqvarnaam.protocol;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * Thrown when a command type is not supported by the channel
 * 
 * Based on the Pioneer binding by Antoine Besnard and authors mentioned there
 * 
 * @author Nikolaus Chorherr - Initial Contribution
 */
@NonNullByDefault
public class CommandTypeNotSupportedException extends Exception {

    private static final long serialVersionUID = -7970958467980752003L;

    public CommandTypeNotSupportedException() {
        super();
    }

    public CommandTypeNotSupportedException(String message) {
        super(message);
    }

    public CommandTypeNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandTypeNotSupportedException(Throwable cause) {
        super(cause);
    }

}
