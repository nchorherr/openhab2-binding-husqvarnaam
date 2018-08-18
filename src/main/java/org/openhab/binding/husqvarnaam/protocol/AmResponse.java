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
 * Represent a response of the AM.
 *
 * Based on the Pioneer binding by Antoine Besnard and authors mentioned there
 * 
 * @author Nikolaus Chorherr - Initial Contribution
 *
 */
public interface AmResponse {

    /**
     * Represent the type of a response.
     *
     * @author Antoine Besnard
     *
     */
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
