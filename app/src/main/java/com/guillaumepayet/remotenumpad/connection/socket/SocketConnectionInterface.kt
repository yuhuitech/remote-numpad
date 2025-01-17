/*
 * Remote Numpad - a numpad application on Android for PCs lacking one.
 * Copyright (C) 2016-2020 Guillaume Payet
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.guillaumepayet.remotenumpad.connection.socket

import androidx.annotation.Keep
import com.guillaumepayet.remotenumpad.R
import com.guillaumepayet.remotenumpad.connection.*
import kotlinx.coroutines.*
import java.io.IOException
import java.io.Writer
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketException

/**
 * This class handles the IP connection through which a [IDataSender] object sends data.
 *
 * @param sender The [IDataSender] to listen for data to send
 *
 * @see IDataSender
 */
@Suppress("BlockingMethodInNonBlockingContext") // The warnings are wrong from what I understand
@Keep
open class SocketConnectionInterface(sender: IDataSender) : AbstractConnectionInterface(sender) {

    companion object {

        /**
         * The port through which to connect to the server.
         */
        const val PORT = 4576
    }


    @Volatile
    private var socket: Socket? = null

    @Volatile
    private var writer: Writer? = null


    override suspend fun open(host: String) = withContext(Dispatchers.IO) {
        onConnectionStatusChange(R.string.status_connecting)

        try {
            socket = openSocket(host)
            writer = socket?.outputStream?.writer()
            onConnectionStatusChange(R.string.status_connected)
        } catch (e: IOException) {
            closeConnection()
            onConnectionStatusChange(R.string.status_could_not_connect)
        }
    }

    override suspend fun close() = withContext(Dispatchers.IO) {
        super.close()
        onConnectionStatusChange(R.string.status_disconnecting)
        closeConnection()
        onConnectionStatusChange(R.string.status_disconnected)
    }

    override suspend fun sendString(string: String): Boolean = withContext(Dispatchers.IO) {
        try {
            writer?.write(string)
            writer?.flush()
            true
        } catch (e: SocketException) {
            closeConnection()
            onConnectionStatusChange(R.string.status_connection_lost)
            false
        }
    }


    /**
     * Instantiate and connects a socket to the given host and port.
     *
     * @param host The host to connect to
     * @param port The port through which to connect
     * @param timeout The time to wait before the host/port is considered unresponsive
     */
    protected open suspend fun openSocket(host: String, port: Int = PORT, timeout: Int = 3000): Socket = withContext(Dispatchers.IO) {
        val endpoint = InetSocketAddress(host, port)
        val socket = Socket()
        socket.connect(endpoint, timeout)
        return@withContext socket
    }


    private fun closeConnection() {
        writer?.close()
        writer = null

        socket?.close()
        socket = null
    }
}