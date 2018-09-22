/*
 * Remote Numpad - a numpad application on Android for PCs lacking one.
 * Copyright (C) 2016-2018 Guillaume Payet
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

import com.guillaumepayet.remotenumpad.R
import com.guillaumepayet.remotenumpad.connection.AbstractConnectionTask

/**
 * This class is an [AbstractConnectionTask] which handles closing a IP connection.
 *
 * Created by guillaume on 12/29/17.
 *
 * @see AbstractConnectionTask
 * @see SocketConnectionInterface
 */
class SocketDisconnectTask(private val connectionInterface: SocketConnectionInterface)
    : AbstractConnectionTask(connectionInterface) {

    @Volatile
    private var socket = connectionInterface.socket

    @Volatile
    private var writer = connectionInterface.writer


    override fun doInBackground(vararg strings: String?): Void? {
        publishProgress(R.string.status_disconnecting)

        writer?.close()
        writer = null

        socket?.close()
        socket = null

        publishProgress(R.string.status_disconnected)
        return null
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)
        connectionInterface.socket = socket
        connectionInterface.writer = writer
    }
}