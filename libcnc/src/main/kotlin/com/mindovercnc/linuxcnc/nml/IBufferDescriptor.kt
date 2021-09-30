package com.mindovercnc.linuxcnc.nml

/*
 * **************************************************************************
 *
 *  file:       IBufferDescriptor.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    29.10.2019 by Django Reinhard
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
@Deprecated("Use the new BuffDescriptor")
interface IBufferDescriptor {
    operator fun get(key: String): BufferEntry?
    fun keySet(): Set<String>?

    companion object {
        const val MotionPaused = "paused"
        const val MotionId = "id"
        const val MotionQueueFull = "queue_full"
        const val PendingMotionQueue = "queue"
        const val ActiveMotionQueue = "active_queue"
        const val InPosition = "inpos"
        const val Enabled = "enabled"
        const val MotionMode = "motion_mode"
        const val LinearUnits = "linear_units"
        const val AngularUnits = "angular_units"
        const val CycleTime = "cycle_time"
        const val QueuedMdiCommands = "queued_mdi_commands"
        const val DelayLeft = "delay_left"
        const val TaskPaused = "task_paused"
        const val IntepreterErrorCode = "interpreter_errcode"
        const val G5xIndex = "g5x_index"
        const val Command = "command"
        const val DigitalInputTimeout = "input_timeout"
        const val OptionalStop = "optional_stop"
        const val BlockDelete = "block_delete"
        const val CallLevel = "call_level"
        const val AbsPosX = "absPosX"
        const val Acceleration = "acceleration"
        const val ActiveGCodes = "activeGCodes"
        const val ActiveMCodes = "activeMCodes"
        const val ActiveSettings = "activeSettings"
        const val Adaptive_feed_enabled = "adaptive_feed_enabled"
        const val AxisMask = "axisMask"
        const val CoolFlood = "coolFlood"
        const val CoolMist = "coolMist"
        const val CurrentLine = "currentLine"
        const val Current_vel = "current_vel"
        const val Debug = "debug"
        const val Distance2Go = "distance2Go"
        const val DtgX = "dtgX"
        const val Estop = "estop"
        const val ExecState = "execState"
        const val Feed_hold_enabled = "feed_hold_enabled"
        const val Feed_override_enabled = "feed_override_enabled"
        const val Feedrate = "feedrate"
        const val File = "file"
        const val G5xOffsX = "g5xOffsX"
        const val G92OffsX = "g92OffsX"
        const val InterpState = "interpState"
        const val Joint_0_enabled = "joint_0_enabled"
        const val Joint_0_homed = "joint_0_homed"
        const val Joint_1_enabled = "joint_1_enabled"
        const val Joint_1_homed = "joint_1_homed"
        const val Joint_2_enabled = "joint_2_enabled"
        const val Joint_2_homed = "joint_2_homed"
        const val Joint_3_enabled = "joint_3_enabled"
        const val Joint_3_homed = "joint_3_homed"
        const val Joint_4_enabled = "joint_4_enabled"
        const val Joint_4_homed = "joint_4_homed"
        const val Joint_5_enabled = "joint_5_enabled"
        const val Joint_5_homed = "joint_5_homed"
        const val Joint_6_enabled = "joint_6_enabled"
        const val Joint_6_homed = "joint_6_homed"
        const val Joint_7_enabled = "joint_7_enabled"
        const val Joint_7_homed = "joint_7_homed"
        const val Joint_8_enabled = "joint_8_enabled"
        const val Joint_8_homed = "joint_8_homed"
        const val NumJoints = "joints"
        const val Lube = "lube"
        const val Max_acceleration = "max_acceleration"
        const val Max_velocity = "max_velocity"
        const val MotionLine = "motionLine"
        const val PocketPrepared = "pocketPrepared"
        const val ProgramUnits = "programUnits"
        const val Rapidrate = "rapidrate"
        const val ReadLine = "readLine"
        const val RelPosX = "relPosX"
        const val RotationXY = "rotationXY"
        const val SpindleDir = "spindleDir"
        const val SpindleEnabled = "spindleEnable"
        const val SpindleHomed = "spindleHomed"
        const val SpindleIncreasing = "spindleIncreasing"
        const val SpindleOverrideEnable = "spindleOverrideEnable"
        const val SpindleScale = "spindleScale"
        const val SpindleSpeed = "spindleSpeed"
        const val NumSpindles = "spindles"
        const val TaskMode = "taskMode"
        const val TaskState = "taskState"
        const val ToolInSpindle = "toolInSpindle"
        const val ToolOffsX = "toolOffsX"
        const val ToolTable = "toolTable"
        const val VelocityT = "velocityT"
        const val VelocityA = "velocityA"
        const val VelocityJ = "velocityJ"
    }
}