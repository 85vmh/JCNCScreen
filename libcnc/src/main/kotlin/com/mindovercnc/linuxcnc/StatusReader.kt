package com.mindovercnc.linuxcnc

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*

/*
 * **************************************************************************
 *
 *  file:       com.mindovercnc.linuxcnc.StatusReader.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    2.9.2019 by Django Reinhard
 *  copyright:  all rights reserved
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * **************************************************************************
 */
class StatusReader(private val updateListener: StatusUpdateListener? = null) {
    private val statusBuffer: ByteBuffer?
    private val _status = MutableSharedFlow<ByteBuffer?>()
    val status: SharedFlow<ByteBuffer?> get() = _status

    init {
        statusBuffer = init()
        statusBuffer!!.order(ByteOrder.LITTLE_ENDIAN)
        updateListener?.onInitialStatus(statusBuffer)
    }

    suspend fun launch() {
        while (true){
            refreshData()
            delay(100L)
        }
    }

    private suspend fun refreshData() {
        update()
        _status.emit(statusBuffer)
        //println("Emitted new status buffer: ${System.currentTimeMillis()}")
    }

    fun update() {
        readStatus()
        if (statusBuffer == null) return
        updateListener?.onStatusUpdated(statusBuffer)
    }

    private external fun init(): ByteBuffer?
    private external fun readStatus(): Int
    external fun getString(offset: Int, length: Int): String?

    interface StatusUpdateListener {
        fun onInitialStatus(statusBuffer: ByteBuffer?)
        fun onStatusUpdated(statusBuffer: ByteBuffer)
    }
}