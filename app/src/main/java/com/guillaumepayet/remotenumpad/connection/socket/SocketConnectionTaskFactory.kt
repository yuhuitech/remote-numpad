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

import com.guillaumepayet.remotenumpad.connection.AbstractConnectionTask
import com.guillaumepayet.remotenumpad.connection.IConnectionInterface
import com.guillaumepayet.remotenumpad.connection.IConnectionTaskFactory

/**
 * A concrete [IConnectionTaskFactory] for the [SocketConnectionInterface] object.
 *
 * Created by guillaume on 1/2/18.
 *
 * @see IConnectionTaskFactory
 * @see SocketConnectionInterface
 */
class SocketConnectionTaskFactory : IConnectionTaskFactory {

    override fun createConnectTask(connectionInterface: IConnectionInterface)
            : AbstractConnectionTask
            = SocketConnectTask(connectionInterface as SocketConnectionInterface)

    override fun createDisconnectTask(connectionInterface: IConnectionInterface)
            : AbstractConnectionTask
            = SocketDisconnectTask(connectionInterface as SocketConnectionInterface)

    override fun createSendTask(connectionInterface: IConnectionInterface)
            : AbstractConnectionTask
            = SocketSendTask(connectionInterface as SocketConnectionInterface)
}