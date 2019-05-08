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
package org.openhab.binding.husqvarnaam.internal.protocol;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.smarthome.core.library.types.DateTimeType;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.openhab.binding.husqvarnaam.HusqvarnaAmBindingConstants;
import org.openhab.binding.husqvarnaam.internal.protocol.ParameterizedCommand.ParameterizedCommandType;
import org.openhab.binding.husqvarnaam.internal.protocol.SimpleCommand.SimpleCommandType;
import org.openhab.binding.husqvarnaam.protocol.AmCommand;
import org.openhab.binding.husqvarnaam.protocol.AmConnection;
import org.openhab.binding.husqvarnaam.protocol.CommandTypeNotSupportedException;
import org.openhab.binding.husqvarnaam.protocol.event.AmDisconnectionEvent;
import org.openhab.binding.husqvarnaam.protocol.event.AmDisconnectionListener;
import org.openhab.binding.husqvarnaam.protocol.event.AmStatusUpdateEvent;
import org.openhab.binding.husqvarnaam.protocol.event.AmUpdateListener;
import org.openhab.binding.husqvarnaam.protocol.utils.DateTimeConverter;
import org.openhab.binding.husqvarnaam.protocol.utils.HexConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * A class that wraps the communication to Husqvarna AM devices by using
 * Input/Ouptut streams. see
 * {@link https://homematic-forum.de/forum/viewtopic.php?f=31&t=7295} There is
 * no offical Husqvaran Documentation available
 *
 * Based on the Pioneer binding by Antoine Besnard and authors mentioned there
 * 
 * @author Nikolaus Chorherr - Initial Contribution
 */
public abstract class StreamAmConnection implements AmConnection {

    private final Logger logger = LoggerFactory
            .getLogger(StreamAmConnection.class);

    // The maximum time to wait incoming messages.
    private static final Integer READ_TIMEOUT = 1000;

    private List<AmUpdateListener> updateListeners;
    private List<AmDisconnectionListener> disconnectionListeners;

    private IpControlInputStreamReader inputStreamReader;
    private DataOutputStream outputStream;

    public StreamAmConnection() {
        this.updateListeners = new ArrayList<>();
        this.disconnectionListeners = new ArrayList<>();
    }

    @Override
    public void addUpdateListener(AmUpdateListener listener) {
        synchronized (updateListeners) {
            updateListeners.add(listener);
        }
    }

    @Override
    public void addDisconnectionListener(AmDisconnectionListener listener) {
        synchronized (disconnectionListeners) {
            disconnectionListeners.add(listener);
        }
    }

    @Override
    public boolean connect() {
        if (!isConnected()) {
            try {
                openConnection();

                // Start the inputStream reader.
                inputStreamReader = new IpControlInputStreamReader(
                        getInputStream());
                inputStreamReader.start();

                // Get Output stream
                outputStream = new DataOutputStream(getOutputStream());

            } catch (IOException ioException) {
                logger.debug("Can't connect to {}. Cause: {}",
                        getConnectionName(), ioException.getMessage());
                return false;
            }

        }
        return isConnected();
    }

    /**
     * Open the connection to the AM.
     *
     * @throws IOException
     */
    protected abstract void openConnection() throws IOException;

    /**
     * Return the inputStream to read responses.
     *
     * @return
     * @throws IOException
     */
    protected abstract InputStream getInputStream() throws IOException;

    /**
     * Return the outputStream to send commands.
     *
     * @return
     * @throws IOException
     */
    protected abstract OutputStream getOutputStream() throws IOException;

    @Override
    public void close() {
        if (inputStreamReader != null) {
            // This method blocks until the reader is really stopped.
            inputStreamReader.stopReader();
            inputStreamReader = null;
            logger.debug("Stream reader stopped!");
        }
    }

    /**
     * Sends to command to the automower. It does not wait for a reply.
     *
     * @param ipControlCommand
     *            the command to send.
     **/
    protected boolean sendCommand(AmCommand ipControlCommand) {
        boolean isSent = false;
        if (connect()) {
            byte[] command = ipControlCommand.getCommand();
            // Protocol says 5 byte with trailing 0x00
            while (command.length < AmCommand.TOTAL_COMMAND_LENGTH) {
                command = ArrayUtils.add(command, AmCommand.FILL_BYTE);
            }
            try {
                logger.trace("Sending {} bytes: {}", command.length,
                        HexConverter.toString(command));
                if (isConnected()) {
                    if (outputStream == null) {
                        outputStream = new DataOutputStream(getOutputStream());
                    }
                    outputStream.write(command);
                    outputStream.flush();
                    isSent = true;
                } else {
                    isSent = false;
                }

            } catch (IOException ioException) {
                logger.error("Error occurred when sending command",
                        ioException);
                // If an error occurs, close the connection
                close();
            }

            logger.debug("Command sent to AM @{}: {}", getConnectionName(),
                    ipControlCommand.getCommandType().name());
        }

        return isSent;
    }
    // Special commands for AM here

    @Override
    public boolean sendStatusQuery() {
        return sendCommand(RequestResponseFactory
                .getIpControlCommand(SimpleCommandType.STATE_QUERY));
    }

    @Override
    public boolean sendChargeTimeQuery() {
        return sendCommand(RequestResponseFactory
                .getIpControlCommand(SimpleCommandType.CHARGETIME_QUERY));
    }

    @Override
    public boolean sendMowTimeQuery() {
        return sendCommand(RequestResponseFactory
                .getIpControlCommand(SimpleCommandType.MOWTIME_QUERY));
    }

    @Override
    public boolean sendCurrentModeQuery() {
        return sendCommand(RequestResponseFactory
                .getIpControlCommand(SimpleCommandType.CURRENT_MODE_QUERY));
    }

    @Override
    public boolean sendOperationalTimeQuery() {
        return sendCommand(RequestResponseFactory
                .getIpControlCommand(SimpleCommandType.OPERATIONTIME_QUERY));
    }

    @Override
    public boolean sendCurrentDateTimeQuery() {
        sendCommand(RequestResponseFactory.getIpControlCommand(
                SimpleCommandType.CURRENT_DATE_TIME_DAY_QUERY));
        sendCommand(RequestResponseFactory.getIpControlCommand(
                SimpleCommandType.CURRENT_DATE_TIME_HOUR_QUERY));
        sendCommand(RequestResponseFactory.getIpControlCommand(
                SimpleCommandType.CURRENT_DATE_TIME_MIN_QUERY));
        sendCommand(RequestResponseFactory.getIpControlCommand(
                SimpleCommandType.CURRENT_DATE_TIME_MONTH_QUERY));
        sendCommand(RequestResponseFactory.getIpControlCommand(
                SimpleCommandType.CURRENT_DATE_TIME_SEC_QUERY));
        return sendCommand(RequestResponseFactory.getIpControlCommand(
                SimpleCommandType.CURRENT_DATE_TIME_YEAR_QUERY));
    }

    @Override
    public boolean sendTimerActiveQuery() {
        sendCommand(RequestResponseFactory
                .getIpControlCommand(SimpleCommandType.TIMER_ACTIVE_QUERY));
        return sendCommand(RequestResponseFactory
                .getIpControlCommand(SimpleCommandType.TIMER_DAYSOFWEEK_QUERY));
    }

    @Override
    public boolean sendTimerQuery() {
        sendCommand(RequestResponseFactory
                .getIpControlCommand(SimpleCommandType.TIMER1_START_QUERY));
        sendCommand(RequestResponseFactory
                .getIpControlCommand(SimpleCommandType.TIMER1_STOP_QUERY));
        sendCommand(RequestResponseFactory
                .getIpControlCommand(SimpleCommandType.TIMER2_START_QUERY));
        sendCommand(RequestResponseFactory
                .getIpControlCommand(SimpleCommandType.TIMER2_STOP_QUERY));
        sendCommand(RequestResponseFactory
                .getIpControlCommand(SimpleCommandType.WE_TIMER1_START_QUERY));
        sendCommand(RequestResponseFactory
                .getIpControlCommand(SimpleCommandType.WE_TIMER1_STOP_QUERY));
        sendCommand(RequestResponseFactory
                .getIpControlCommand(SimpleCommandType.WE_TIMER2_START_QUERY));
        return sendCommand(RequestResponseFactory
                .getIpControlCommand(SimpleCommandType.WE_TIMER2_STOP_QUERY));
    }

    @Override
    public boolean sendDetailsQuery() {
        sendCommand(RequestResponseFactory.getIpControlCommand(
                SimpleCommandType.CURRENT_BATTERY_CAPACITY_USED_MAH_QUERY));
        sendCommand(RequestResponseFactory.getIpControlCommand(
                SimpleCommandType.RECTANGLE_MODE_STATE_QUERY));
        sendCommand(RequestResponseFactory.getIpControlCommand(
                SimpleCommandType.RECTANGLE_MODE_PERCENT_QUERY));
        sendCommand(RequestResponseFactory.getIpControlCommand(
                SimpleCommandType.RECTANGLE_MODE_REFERENCE_QUERY));
        sendCommand(RequestResponseFactory.getIpControlCommand(
                SimpleCommandType.CURRENT_BATTERY_CAPACITY_MA_QUERY));
        sendCommand(RequestResponseFactory.getIpControlCommand(
                SimpleCommandType.CURRENT_BATTERY_CAPACITY_MAH_QUERY));
        sendCommand(RequestResponseFactory.getIpControlCommand(
                SimpleCommandType.CURRENT_BATTERY_CAPACITY_SEARCH_START_MAH_QUERY));
        sendCommand(RequestResponseFactory.getIpControlCommand(
                SimpleCommandType.CURRENT_BATTERY_CAPACITY_USED_MAH_QUERY));
        sendCommand(RequestResponseFactory.getIpControlCommand(
                SimpleCommandType.CURRENT_BATTERY_VOLTAGE_MV_QUERY));
        sendCommand(RequestResponseFactory.getIpControlCommand(
                SimpleCommandType.CURRENT_BATTERY_TEMPERATURE_QUERY));
        sendCommand(RequestResponseFactory.getIpControlCommand(
                SimpleCommandType.CURRENT_BATTERY_TEMPERATURE_CHARGE_QUERY));
        sendCommand(RequestResponseFactory.getIpControlCommand(
                SimpleCommandType.CURRENT_BATTERY_LAST_CHARGE_MIN_QUERY));
        sendCommand(RequestResponseFactory.getIpControlCommand(
                SimpleCommandType.CURRENT_BATTERY_NEXT_TEMPERATURE_MEASUREMENT_SEC_QUERY));
        sendCommand(RequestResponseFactory.getIpControlCommand(
                SimpleCommandType.CURRENT_VELOCITY_MOTOR_QUERY));
        sendCommand(RequestResponseFactory.getIpControlCommand(
                SimpleCommandType.CURRENT_VELOCITY_RIGHT_QUERY));
        sendCommand(RequestResponseFactory.getIpControlCommand(
                SimpleCommandType.CURRENT_VELOCITY_LEFT_QUERY));
        sendCommand(RequestResponseFactory
                .getIpControlCommand(SimpleCommandType.FIRMWARE_VERSION_QUERY));
        return sendCommand(RequestResponseFactory.getIpControlCommand(
                SimpleCommandType.LANGUAGE_FILE_VERSION_QUERY));
    }

    @Override
    public boolean sendTimer1StartCommand(Command command)
            throws CommandTypeNotSupportedException {
        return this.sendTimerCommand(command,
                ParameterizedCommandType.SET_TIMER1_START,
                SimpleCommandType.TIMER1_START_QUERY);
    }

    @Override
    public boolean sendTimer1StopCommand(Command command)
            throws CommandTypeNotSupportedException {
        return this.sendTimerCommand(command,
                ParameterizedCommandType.SET_TIMER1_STOP,
                SimpleCommandType.TIMER1_STOP_QUERY);
    }

    @Override
    public boolean sendTimer2StartCommand(Command command)
            throws CommandTypeNotSupportedException {
        return this.sendTimerCommand(command,
                ParameterizedCommandType.SET_TIMER2_START,
                SimpleCommandType.TIMER2_START_QUERY);
    }

    @Override
    public boolean sendTimer2StopCommand(Command command)
            throws CommandTypeNotSupportedException {
        return this.sendTimerCommand(command,
                ParameterizedCommandType.SET_TIMER2_STOP,
                SimpleCommandType.TIMER2_STOP_QUERY);
    }

    @Override
    public boolean sendWeTimer1StartCommand(Command command)
            throws CommandTypeNotSupportedException {
        return this.sendTimerCommand(command,
                ParameterizedCommandType.SET_WETIMER1_START,
                SimpleCommandType.WE_TIMER1_START_QUERY);
    }

    @Override
    public boolean sendWeTimer1StopCommand(Command command)
            throws CommandTypeNotSupportedException {
        return this.sendTimerCommand(command,
                ParameterizedCommandType.SET_WETIMER1_STOP,
                SimpleCommandType.WE_TIMER1_STOP_QUERY);
    }

    @Override
    public boolean sendWeTimer2StartCommand(Command command)
            throws CommandTypeNotSupportedException {
        return this.sendTimerCommand(command,
                ParameterizedCommandType.SET_WETIMER2_START,
                SimpleCommandType.WE_TIMER2_START_QUERY);
    }

    @Override
    public boolean sendWeTimer2StopCommand(Command command)
            throws CommandTypeNotSupportedException {
        return this.sendTimerCommand(command,
                ParameterizedCommandType.SET_WETIMER2_STOP,
                SimpleCommandType.WE_TIMER2_STOP_QUERY);
    }

    private boolean sendTimerCommand(Command command,
            ParameterizedCommandType commandTyp, SimpleCommandType refreshType)
            throws CommandTypeNotSupportedException {
        AmCommand commandToSend = null;

        if (command instanceof DateTimeType) {
            byte[] parameter =DateTimeConverter.convert(((DateTimeType) command).getZonedDateTime());
            commandToSend = RequestResponseFactory
                    .getIpControlCommand(commandTyp).setParameter(parameter);
             logger.trace("Timer command: {}",
                    HexConverter.toString(commandToSend.getCommand()));
        } else if (command == RefreshType.REFRESH) {
            commandToSend = RequestResponseFactory
                    .getIpControlCommand(refreshType);
        } else {
            throw new CommandTypeNotSupportedException(
                    "Command type not supported. " + command.toFullString());

        }
        return sendCommand(commandToSend);
    }

    @Override
    public boolean sendTimerActiveCommand(Command command)
            throws CommandTypeNotSupportedException {
        AmCommand commandToSend = null;
        boolean sent = false;

        if (command == OnOffType.ON) {
            commandToSend = RequestResponseFactory
                    .getIpControlCommand(SimpleCommandType.SET_TIMER_ACTIVE);
        } else if (command == OnOffType.OFF) {
            commandToSend = RequestResponseFactory
                    .getIpControlCommand(SimpleCommandType.SET_TIMER_INACTIVE);
        } else if (command == RefreshType.REFRESH) {
            commandToSend = RequestResponseFactory
                    .getIpControlCommand(SimpleCommandType.TIMER_ACTIVE_QUERY);
        } else {
            throw new CommandTypeNotSupportedException(
                    "Command type not supported. " + command.toFullString());
        }
        logger.trace("TimerActive command: {}",
                HexConverter.toString(commandToSend.getCommand()));
        sent = sendCommand(commandToSend);
        if (command instanceof OnOffType) {
            commandToSend = RequestResponseFactory
                    .getIpControlCommand(SimpleCommandType.SET_KEY_YES);
            logger.trace("Additional TimerActive command (Press YES): {}",
                    HexConverter.toString(commandToSend.getCommand()));
            return sendCommand(commandToSend);
        }
        return sent;
    }

    @Override
    public boolean sendTimerWeekdaysActiveCommand(Command command)
            throws CommandTypeNotSupportedException {
        AmCommand commandToSend = null;

        if (command instanceof DecimalType) {
            byte[] parameter = new byte[]{
                    (byte) ((DecimalType) command).intValue()}; // 0-127
            commandToSend = RequestResponseFactory
                    .getIpControlCommand(
                            ParameterizedCommandType.SET_TIMER_WEEK)
                    .setParameter(parameter);
        } else if (command == RefreshType.REFRESH) {
            commandToSend = RequestResponseFactory
                    .getIpControlCommand(SimpleCommandType.TIMER_ACTIVE_QUERY);
        } else {
            throw new CommandTypeNotSupportedException(
                    "Command type not supported. " + command.toFullString()
                            + " - java type is "
                            + command.getClass().getName());
        }
        logger.trace("TimerWeekdaysActive command: {}", commandToSend);
        return sendCommand(commandToSend);
    }

    @Override
    public boolean sendModeCommand(Command command)
            throws CommandTypeNotSupportedException {
        AmCommand commandToSend = null;

        if (command instanceof StringType) {
            String modeValue = ((StringType) command).toString(); // (0)MAN,(1)AUTO,(3)HOME,(4)DEMO
            switch (modeValue) {
                case HusqvarnaAmBindingConstants.MODE_MAN :
                    commandToSend = RequestResponseFactory.getIpControlCommand(
                            SimpleCommandType.SET_MODE_MAN);
                    break;
                case HusqvarnaAmBindingConstants.MODE_AUTO :
                    commandToSend = RequestResponseFactory.getIpControlCommand(
                            SimpleCommandType.SET_MODE_AUTO);
                    break;
                case HusqvarnaAmBindingConstants.MODE_HOME :
                    commandToSend = RequestResponseFactory.getIpControlCommand(
                            SimpleCommandType.SET_MODE_HOME);
                    break;
                case HusqvarnaAmBindingConstants.MODE_DEMO :
                    commandToSend = RequestResponseFactory.getIpControlCommand(
                            SimpleCommandType.SET_MODE_DEMO);
                    break;
                default :
                    throw new CommandTypeNotSupportedException(
                            "Parameter value " + modeValue
                                    + " not supported for "
                                    + command.toFullString());
            }
        } else if (command == RefreshType.REFRESH) {
            commandToSend = RequestResponseFactory
                    .getIpControlCommand(SimpleCommandType.CURRENT_MODE_QUERY);
        } else {
            throw new CommandTypeNotSupportedException(
                    "Command type not supported. " + command.toFullString());
        }
        logger.trace("Mode command: {}",
                HexConverter.toString(commandToSend.getCommand()));
        return sendCommand(commandToSend);
    }

    //

    /**
     * Read incoming data from the Husqvarna AM and notify listeners for
     * dataReceived and disconnection.
     *
     * @author Antoine Besnard
     *
     */
    private class IpControlInputStreamReader extends Thread {

        private InputStream inStream = null;

        private volatile boolean stopReader;

        // This latch is used to block the stop method until the reader is
        // really stopped.
        private CountDownLatch stopLatch;

        /**
         * Construct a reader that read the given inputStream
         *
         * @param ipControlSocket
         * @throws IOException
         */
        public IpControlInputStreamReader(InputStream inputStream) {
            // this.bufferedReader = new BufferedReader(new
            // InputStreamReader(inputStream));
            this.inStream = inputStream;
            this.stopLatch = new CountDownLatch(1);

            this.setDaemon(true);
            this.setName("IpControlInputStreamReader-" + getConnectionName());
        }

        @Override
        public void run() {
            try {
                while (!stopReader && !Thread.currentThread().isInterrupted()) {
                    byte[] receivedData = new byte[AmCommand.TOTAL_COMMAND_LENGTH];
                    try {
                        // responses are 5 byte
                        int offset = 0;
                        while (offset < receivedData.length) {
                            offset += inStream.read(receivedData, offset,
                                    (receivedData.length - offset));
                        }
                    } catch (SocketTimeoutException e) {
                        // Do nothing. Just happen to allow the thread to check
                        // if it has to stop.
                    }
                    logger.trace("Data received from AM @{}: {}",
                            getConnectionName(),
                            HexConverter.toString(receivedData));
                    AmStatusUpdateEvent event = new AmStatusUpdateEvent(
                            StreamAmConnection.this, receivedData);
                    synchronized (updateListeners) {
                        for (AmUpdateListener husqvarnaAmEventListener : updateListeners) {
                            husqvarnaAmEventListener
                                    .statusUpdateReceived(event);
                        }
                    }
                }

            } catch (IOException e) {
                logger.warn("The AM @{} is disconnected.", getConnectionName(),
                        e);
                AmDisconnectionEvent event = new AmDisconnectionEvent(
                        StreamAmConnection.this, e);
                for (AmDisconnectionListener husqvarnaAmDisconnectionListener : disconnectionListeners) {
                    husqvarnaAmDisconnectionListener.onDisconnection(event);
                }
            }

            // Notify the stopReader method caller that the reader is stopped.
            this.stopLatch.countDown();
        }

        /**
         * Stop this reader. Block until the reader is really stopped.
         */
        public void stopReader() {
            this.stopReader = true;
            try {
                this.stopLatch.await(READ_TIMEOUT, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                // Do nothing. The timeout is just here for safety and to be
                // sure that the call to this method will not
                // block the caller indefinitely.
                // This exception should never happen.
            }
        }

    }

}
