/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.husqvarnaam.protocol;

/**
 * Exception for eISCP errors.
 * 
 * Based on the Pioneer binding by Antoine Besnard and authors mentioned there
 * 
 * @author Nikolaus Chorherr - Initial Contribution
 */
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
