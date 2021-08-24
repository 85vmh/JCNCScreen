package de.schwarzrot.system;


/*
 * **************************************************************************
 *
 *  file:       StatusReader.java
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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class NStatusReader {
    private ByteBuffer statusBuffer;
    private StatusUpdateListener updateListener;

    public NStatusReader(StatusUpdateListener updateListener) {
        this.updateListener = updateListener;
        statusBuffer = init();
        statusBuffer.order(ByteOrder.LITTLE_ENDIAN);
        if (updateListener != null) {
            updateListener.onInitialStatus(statusBuffer);
        }
    }

    public ByteBuffer getStatusBuffer() {
        return statusBuffer;
    }

    public void update() {
        readStatus();
        if (statusBuffer == null)
            return;
        if (updateListener != null) {
            updateListener.onStatusUpdated(statusBuffer);
        }
    }

    private native final ByteBuffer init();

    protected native final int readStatus();

    protected native String getString(int offset, int length);

    public interface StatusUpdateListener {
        void onInitialStatus(ByteBuffer statusBuffer);

        void onStatusUpdated(ByteBuffer statusBuffer);
    }
}
