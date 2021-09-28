package com.mindovercnc.base.data

/**
 * Types for motion control
 */
enum class TrajectoryMode(value: Int) {
    /**
     * Independent axis motion
     */
    MODE_FREE(1),

    /**
     * Coordinated axis motion
     */
    MODE_COORDINATED(2),

    /**
     * Velocity based world coordinates motion
     */
    MODE_TELEOP(3)

}