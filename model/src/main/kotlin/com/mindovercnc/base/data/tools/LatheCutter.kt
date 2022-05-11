package com.mindovercnc.base.data.tools

sealed class LatheCutter(
    open val cutterId: Int?,
    open val tipOrientation: TipOrientation,
    open val allowedSpindleDirection: AllowedSpindleDirection,
) {
    data class Turning(
        override val cutterId: Int? = null,
        val insert: CuttingInsert,
        override val tipOrientation: TipOrientation,
        override val allowedSpindleDirection: AllowedSpindleDirection
    ) : LatheCutter(cutterId, tipOrientation, allowedSpindleDirection)

    data class Boring(
        override val cutterId: Int? = null,
        val insert: CuttingInsert,
        override val tipOrientation: TipOrientation,
        override val allowedSpindleDirection: AllowedSpindleDirection,
        val minBoreDiameter: Double,
        val maxZDepth: Double
    ) : LatheCutter(cutterId, tipOrientation, allowedSpindleDirection)

    data class DrillingReaming(
        override val cutterId: Int? = null,
        val insert: CuttingInsert? = null,
        val toolDiameter: Double,
        val maxZDepth: Double
    ) : LatheCutter(cutterId, TipOrientation.Position7, AllowedSpindleDirection.Reverse)

    data class Parting(
        override val cutterId: Int? = null,
        val insert: CuttingInsert,
        val bladeWidth: Double,
        val maxXDepth: Double
    ) : LatheCutter(cutterId, TipOrientation.Position6, AllowedSpindleDirection.Reverse)

    data class Grooving(
        override val cutterId: Int? = null,
        val insert: CuttingInsert,
        override val tipOrientation: TipOrientation,
        override val allowedSpindleDirection: AllowedSpindleDirection,
        val bladeWidth: Double,
        val maxXDepth: Double
    ) : LatheCutter(cutterId, tipOrientation, allowedSpindleDirection)

    data class OdThreading(
        override val cutterId: Int? = null,
        val insert: CuttingInsert,
        val minPitch: Double,
        val maxPitch: Double,
    ) : LatheCutter(cutterId, TipOrientation.Position6, AllowedSpindleDirection.Reverse)

    data class IdThreading(
        override val cutterId: Int? = null,
        val insert: CuttingInsert,
        val minPitch: Double,
        val maxPitch: Double,
    ) : LatheCutter(cutterId, TipOrientation.Position8, AllowedSpindleDirection.Reverse)

    data class Slotting(
        override val cutterId: Int? = null,
        val insert: CuttingInsert? = null,
        val bladeWidth: Double,
        val maxZDepth: Double
    ) : LatheCutter(cutterId, TipOrientation.Position7, AllowedSpindleDirection.None)
}
