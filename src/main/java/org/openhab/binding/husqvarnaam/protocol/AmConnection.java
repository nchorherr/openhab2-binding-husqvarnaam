/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.husqvarnaam.protocol;

import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.husqvarnaam.protocol.event.AmDisconnectionListener;
import org.openhab.binding.husqvarnaam.protocol.event.AmUpdateListener;

/**
 * Represent a connection to a remote Husqvarna AM.
 *
 * Based on the Pioneer binding by Antoine Besnard and authors mentioned there
 * 
 * @author Nikolaus Chorherr - Initial Contribution
 *
 */
public interface AmConnection {

    /**
     * Add an update listener. It is notified when an update is received from
     * the AM.
     *
     * @param listener
     */
    void addUpdateListener(AmUpdateListener listener);

    /**
     * Add a disconnection listener. It is notified when the AM is disconnected.
     *
     * @param listener
     */
    void addDisconnectionListener(AmDisconnectionListener listener);

    /**
     * Connect to the automower. Return true if the connection has succeeded or
     * if already connected.
     *
     **/
    boolean connect();

    /**
     * Return true if this manager is connected to the AM.
     *
     * @return
     */
    boolean isConnected();

    /**
     * Closes the connection.
     **/
    void close();

    /**
     * Return the connection name
     *
     * @return
     */
    String getConnectionName();

    boolean sendStatusQuery();

    boolean sendChargeTimeQuery();

    boolean sendCurrentModeQuery();

    boolean sendMowTimeQuery();

    boolean sendOperationalTimeQuery();

    boolean sendCurrentDateTimeQuery();

    boolean sendModeCommand(Command command)
            throws CommandTypeNotSupportedException;

    // for expert mode
    boolean sendDetailsQuery();

    // methods for Timer Query
    // methods for DateTimeQuery

    // Updates TimerActive and TimerWeekdays
    boolean sendTimerActiveQuery();

    // Updates all Timer
    boolean sendTimerQuery();

    boolean sendTimerActiveCommand(Command command)
            throws CommandTypeNotSupportedException;

    boolean sendTimerWeekdaysActiveCommand(Command command)
            throws CommandTypeNotSupportedException;

    boolean sendTimer1StartCommand(Command command)
            throws CommandTypeNotSupportedException;

    boolean sendTimer1StopCommand(Command command)
            throws CommandTypeNotSupportedException;

    boolean sendTimer2StartCommand(Command command)
            throws CommandTypeNotSupportedException;

    boolean sendTimer2StopCommand(Command command)
            throws CommandTypeNotSupportedException;

    boolean sendWeTimer1StartCommand(Command command)
            throws CommandTypeNotSupportedException;

    boolean sendWeTimer1StopCommand(Command command)
            throws CommandTypeNotSupportedException;

    boolean sendWeTimer2StartCommand(Command command)
            throws CommandTypeNotSupportedException;

    boolean sendWeTimer2StopCommand(Command command)
            throws CommandTypeNotSupportedException;

}