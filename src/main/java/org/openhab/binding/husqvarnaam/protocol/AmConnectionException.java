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
 * Exception for eISCP errors.
 * 
 * Based on the Pioneer binding by Antoine Besnard and authors mentioned there
 * 
 * @author Nikolaus Chorherr - Initial Contribution
 */
@NonNullByDefault
public class AmConnectionException extends RuntimeException {

    private static final long serialVersionUID = -7970958467980752003L;

    public AmConnectionException() {
        super();
    }

    public AmConnectionException(String message) {
        super(message);
    }

    public AmConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public AmConnectionException(Throwable cause) {
        super(cause);
    }

}
