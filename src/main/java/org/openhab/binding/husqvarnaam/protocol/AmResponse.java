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
 * Represent a response of the AM.
 *
 * Based on the Pioneer binding by Antoine Besnard and authors mentioned there
 * 
 * @author Nikolaus Chorherr - Initial Contribution
 *
 */
@NonNullByDefault
public interface AmResponse {

    /**
     * Represent the type of a response.
     *
     * @author Antoine Besnard
     *
     */
    @NonNullByDefault
    public interface ResponseType {

        /**
         * Return true if the responses of this type has to have a parameter.
         *
         * @return
         */
        public boolean hasParameter();

        /**
         * Return the zone number if the responseData matches a zone of this
         * responseType.
         *
         * If any zone matches, return null.
         *
         * @param responseData
         * @return
         */
        public boolean match(byte[] responseData);

        /**
         * Return the parameter value of the given responseData.
         *
         * @param responseData
         * @return
         */
        public byte[] parseParameter(byte[] responseData);
    }

    /**
     * Return the response type of this response
     *
     * @return
     */
    public ResponseType getResponseType();

    /**
     * Return the parameter of this response or null if the resposne has no
     * parameter.
     *
     * @return
     */
    public byte[] getParameterValue();

    /**
     * Return true if this response has a parameter.
     *
     * @return
     */
    public boolean hasParameter();

}
