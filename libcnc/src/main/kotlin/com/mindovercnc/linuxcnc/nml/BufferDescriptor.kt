package com.mindovercnc.linuxcnc.nml

import com.mindovercnc.linuxcnc.nml.BufferEntry.BufferEntryType
import kotlin.collections.HashMap

/*
 * **************************************************************************
 *
 *  file:       BufferDescriptor.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    28.10.2019 by Django Reinhard
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
 */ /**
 * Buffer descriptor for NML buffer of linuxcnc 2.9ff
 */
@Deprecated("Use the new BuffDescriptor")
class BufferDescriptor : IBufferDescriptor {
    override fun get(key: String): BufferEntry? {
        return bufferEntries[key]
    }

    override fun keySet(): Set<String>? {
        return bufferEntries.keys
    }

    companion object {
        private val bufferEntries: MutableMap<String, BufferEntry> = HashMap()
        private const val nmlHasToolTable = false

        init {
            //The equivalent of EMC_TASK_STAT
            bufferEntries[IBufferDescriptor.TaskMode] = BufferEntry(IBufferDescriptor.TaskMode, 212, 4, BufferEntryType.Integer)
            bufferEntries[IBufferDescriptor.TaskState] = BufferEntry(IBufferDescriptor.TaskState, 216, 4, BufferEntryType.Integer)
            bufferEntries[IBufferDescriptor.ExecState] = BufferEntry(IBufferDescriptor.ExecState, 220, 4, BufferEntryType.Integer)
            bufferEntries[IBufferDescriptor.InterpState] = BufferEntry(IBufferDescriptor.InterpState, 224, 4, BufferEntryType.Integer)
            bufferEntries[IBufferDescriptor.CallLevel] = BufferEntry(IBufferDescriptor.CallLevel, 228, 4, BufferEntryType.Integer)
            bufferEntries[IBufferDescriptor.MotionLine] = BufferEntry(IBufferDescriptor.MotionLine, 232, 4, BufferEntryType.Integer)
            bufferEntries[IBufferDescriptor.CurrentLine] = BufferEntry(IBufferDescriptor.CurrentLine, 236, 4, BufferEntryType.Integer)
            bufferEntries[IBufferDescriptor.ReadLine] = BufferEntry(IBufferDescriptor.ReadLine, 240, 4, BufferEntryType.Integer)
            bufferEntries[IBufferDescriptor.OptionalStop] = BufferEntry(IBufferDescriptor.OptionalStop, 244, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.BlockDelete] = BufferEntry(IBufferDescriptor.BlockDelete, 245, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.DigitalInputTimeout] = BufferEntry(IBufferDescriptor.DigitalInputTimeout, 246, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.File] = BufferEntry(IBufferDescriptor.File, 247, 255, BufferEntryType.String)
            bufferEntries[IBufferDescriptor.Command] = BufferEntry(IBufferDescriptor.Command, 502, 255, BufferEntryType.String)
            bufferEntries[IBufferDescriptor.G5xOffsX] = BufferEntry(IBufferDescriptor.G5xOffsX, 760, 8, BufferEntryType.Double)
            bufferEntries[IBufferDescriptor.G5xIndex] = BufferEntry(IBufferDescriptor.G5xIndex, 832, 8, BufferEntryType.Double)
            bufferEntries[IBufferDescriptor.G92OffsX] = BufferEntry(IBufferDescriptor.G92OffsX, 840, 8, BufferEntryType.Double)
            bufferEntries[IBufferDescriptor.RotationXY] = BufferEntry(IBufferDescriptor.RotationXY, 912, 8, BufferEntryType.Double)
            bufferEntries[IBufferDescriptor.ToolOffsX] = BufferEntry(IBufferDescriptor.ToolOffsX, 920, 8, BufferEntryType.Double)
            bufferEntries[IBufferDescriptor.ActiveGCodes] = BufferEntry(IBufferDescriptor.ActiveGCodes, 992, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.ActiveMCodes] = BufferEntry(IBufferDescriptor.ActiveMCodes, 1060, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.ActiveSettings] = BufferEntry(IBufferDescriptor.ActiveSettings, 1104, 5, BufferEntryType.Double)
            bufferEntries[IBufferDescriptor.ProgramUnits] = BufferEntry(IBufferDescriptor.ProgramUnits, 1144, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.IntepreterErrorCode] = BufferEntry(IBufferDescriptor.IntepreterErrorCode, 1148, 4, BufferEntryType.Integer)
            bufferEntries[IBufferDescriptor.TaskPaused] = BufferEntry(IBufferDescriptor.TaskPaused, 1152, 8, BufferEntryType.Integer) //TODO: check type here
            bufferEntries[IBufferDescriptor.DelayLeft] = BufferEntry(IBufferDescriptor.DelayLeft, 1160, 8, BufferEntryType.Double)
            bufferEntries[IBufferDescriptor.QueuedMdiCommands] = BufferEntry(IBufferDescriptor.QueuedMdiCommands, 1168, 8, BufferEntryType.Double)

            //The equivalent of EMC_MOTION_STAT

            bufferEntries[IBufferDescriptor.LinearUnits] = BufferEntry(IBufferDescriptor.LinearUnits, 1392, 8, BufferEntryType.Double)
            bufferEntries[IBufferDescriptor.AngularUnits] = BufferEntry(IBufferDescriptor.AngularUnits, 1400, 8, BufferEntryType.Double)
            bufferEntries[IBufferDescriptor.CycleTime] = BufferEntry(IBufferDescriptor.CycleTime, 1408, 8, BufferEntryType.Double)
            bufferEntries[IBufferDescriptor.NumJoints] = BufferEntry(IBufferDescriptor.NumJoints, 1416, 4, BufferEntryType.Integer)
            bufferEntries[IBufferDescriptor.NumSpindles] = BufferEntry(IBufferDescriptor.NumSpindles, 1420, 4, BufferEntryType.Integer)
            bufferEntries[IBufferDescriptor.AxisMask] = BufferEntry(IBufferDescriptor.AxisMask, 1428, 4, BufferEntryType.Integer)
            bufferEntries[IBufferDescriptor.MotionMode] = BufferEntry(IBufferDescriptor.MotionMode, 1432, 4, BufferEntryType.Integer)
            bufferEntries[IBufferDescriptor.Enabled] = BufferEntry(IBufferDescriptor.Enabled, 1436, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.InPosition] = BufferEntry(IBufferDescriptor.InPosition, 1437, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.PendingMotionQueue] = BufferEntry(IBufferDescriptor.PendingMotionQueue, 1440, 4, BufferEntryType.Integer)
            bufferEntries[IBufferDescriptor.ActiveMotionQueue] = BufferEntry(IBufferDescriptor.ActiveMotionQueue, 1444, 4, BufferEntryType.Integer)
            bufferEntries[IBufferDescriptor.MotionQueueFull] = BufferEntry(IBufferDescriptor.MotionQueueFull, 1448, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.MotionId] = BufferEntry(IBufferDescriptor.MotionId, 1452, 4, BufferEntryType.Integer)
            bufferEntries[IBufferDescriptor.MotionPaused] = BufferEntry(IBufferDescriptor.MotionPaused, 1456, 1, BufferEntryType.Byte) //TODO: Check this against the dump



            bufferEntries[IBufferDescriptor.RelPosX] = BufferEntry(IBufferDescriptor.RelPosX, 1552, 8, BufferEntryType.Double)
            bufferEntries[IBufferDescriptor.AbsPosX] = BufferEntry(IBufferDescriptor.AbsPosX, 1480, 8, BufferEntryType.Double)
            bufferEntries[IBufferDescriptor.DtgX] = BufferEntry(IBufferDescriptor.DtgX, 1752, 8, BufferEntryType.Double)

            bufferEntries[IBufferDescriptor.Joint_0_enabled] = BufferEntry(IBufferDescriptor.Joint_0_enabled, 2116, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.Joint_1_enabled] = BufferEntry(IBufferDescriptor.Joint_1_enabled, 2332, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.Joint_2_enabled] = BufferEntry(IBufferDescriptor.Joint_2_enabled, 2548, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.Joint_3_enabled] = BufferEntry(IBufferDescriptor.Joint_3_enabled, 2764, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.Joint_4_enabled] = BufferEntry(IBufferDescriptor.Joint_4_enabled, 2980, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.Joint_5_enabled] = BufferEntry(IBufferDescriptor.Joint_5_enabled, 3196, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.Joint_6_enabled] = BufferEntry(IBufferDescriptor.Joint_6_enabled, 3412, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.Joint_7_enabled] = BufferEntry(IBufferDescriptor.Joint_7_enabled, 3628, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.Joint_8_enabled] = BufferEntry(IBufferDescriptor.Joint_8_enabled, 3844, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.Joint_0_homed] = BufferEntry(IBufferDescriptor.Joint_0_homed, 2114, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.Joint_1_homed] = BufferEntry(IBufferDescriptor.Joint_1_homed, 2330, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.Joint_2_homed] = BufferEntry(IBufferDescriptor.Joint_2_homed, 2546, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.Joint_3_homed] = BufferEntry(IBufferDescriptor.Joint_3_homed, 2762, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.Joint_4_homed] = BufferEntry(IBufferDescriptor.Joint_4_homed, 2978, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.Joint_5_homed] = BufferEntry(IBufferDescriptor.Joint_5_homed, 3194, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.Joint_6_homed] = BufferEntry(IBufferDescriptor.Joint_6_homed, 3410, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.Joint_7_homed] = BufferEntry(IBufferDescriptor.Joint_7_homed, 3626, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.Joint_8_homed] = BufferEntry(IBufferDescriptor.Joint_8_homed, 3842, 1, BufferEntryType.Byte)


            bufferEntries[IBufferDescriptor.SpindleSpeed] = BufferEntry(IBufferDescriptor.SpindleSpeed, 6696, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.SpindleScale] = BufferEntry(IBufferDescriptor.SpindleScale, 6704, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.SpindleDir] = BufferEntry(IBufferDescriptor.SpindleDir, 6732, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.SpindleIncreasing] = BufferEntry(IBufferDescriptor.SpindleIncreasing, 6740, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.SpindleOverrideEnable] = BufferEntry(IBufferDescriptor.SpindleOverrideEnable, 6756, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.SpindleEnabled] = BufferEntry(IBufferDescriptor.SpindleEnabled, 6744, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.SpindleHomed] = BufferEntry(IBufferDescriptor.SpindleHomed, 6757, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.Feedrate] = BufferEntry(IBufferDescriptor.Feedrate, 1464, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.Rapidrate] = BufferEntry(IBufferDescriptor.Rapidrate, 1472, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.VelocityT] = BufferEntry(IBufferDescriptor.VelocityT, 1624, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.VelocityA] = BufferEntry(IBufferDescriptor.VelocityA, 5496, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.VelocityJ] = BufferEntry(IBufferDescriptor.VelocityJ, 2104, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.Acceleration] = BufferEntry(IBufferDescriptor.Acceleration, 1632, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.Max_velocity] = BufferEntry(IBufferDescriptor.Max_velocity, 1640, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.Max_acceleration] = BufferEntry(IBufferDescriptor.Max_acceleration, 1648, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.ToolInSpindle] = BufferEntry(IBufferDescriptor.ToolInSpindle, 9812, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.PocketPrepared] = BufferEntry(IBufferDescriptor.PocketPrepared, 9808, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.Distance2Go] = BufferEntry(IBufferDescriptor.Distance2Go, 1744, 1, BufferEntryType.Byte)

            bufferEntries[IBufferDescriptor.Current_vel] = BufferEntry(IBufferDescriptor.Current_vel, 1824, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.Feed_override_enabled] = BufferEntry(IBufferDescriptor.Feed_override_enabled, 1832, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.Adaptive_feed_enabled] = BufferEntry(IBufferDescriptor.Adaptive_feed_enabled, 1833, 1, BufferEntryType.Byte)
            bufferEntries[IBufferDescriptor.Feed_hold_enabled] = BufferEntry(IBufferDescriptor.Feed_hold_enabled, 1834, 1, BufferEntryType.Byte)
            if (nmlHasToolTable) {
                bufferEntries[IBufferDescriptor.ToolTable] = BufferEntry(IBufferDescriptor.ToolTable, 9816, 1, BufferEntryType.Byte)
                bufferEntries[IBufferDescriptor.CoolFlood] = BufferEntry(IBufferDescriptor.CoolFlood, 122036, 1, BufferEntryType.Integer)
                bufferEntries[IBufferDescriptor.CoolMist] = BufferEntry(IBufferDescriptor.CoolMist, 122032, 1, BufferEntryType.Integer)
                bufferEntries[IBufferDescriptor.Debug] = BufferEntry(IBufferDescriptor.Debug, 122264, 1, BufferEntryType.Integer)
                bufferEntries[IBufferDescriptor.Estop] = BufferEntry(IBufferDescriptor.Estop, 122144, 1, BufferEntryType.Integer)
                bufferEntries[IBufferDescriptor.Lube] = BufferEntry(IBufferDescriptor.Lube, 122256, 1, BufferEntryType.Integer)
            } else {
                bufferEntries[IBufferDescriptor.CoolFlood] = BufferEntry(IBufferDescriptor.CoolFlood, 10036, 1, BufferEntryType.Integer)
                bufferEntries[IBufferDescriptor.CoolMist] = BufferEntry(IBufferDescriptor.CoolMist, 10032, 1, BufferEntryType.Integer)
                bufferEntries[IBufferDescriptor.Debug] = BufferEntry(IBufferDescriptor.Debug, 10264, 1, BufferEntryType.Integer)
                bufferEntries[IBufferDescriptor.Estop] = BufferEntry(IBufferDescriptor.Estop, 10144, 1, BufferEntryType.Integer)
                bufferEntries[IBufferDescriptor.Lube] = BufferEntry(IBufferDescriptor.Lube, 10256, 1, BufferEntryType.Integer)
            }
        }
    }
}