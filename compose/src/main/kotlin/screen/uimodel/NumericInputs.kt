package screen.uimodel

import java.util.*

object NumericInputs {
    private val map: MutableMap<InputType, NumInputParameters> = EnumMap(InputType::class.java)

    val entries: Map<InputType, NumInputParameters>
        get() = map

    init {
        map[InputType.RPM] = NumInputParameters(
            valueDescription = "Spindle Speed",
            unit = "rev/min",
            minValue = 10.0,
            maxValue = 3000.0,
            initialValue = 100.0
        )
        map[InputType.CSS] = NumInputParameters(
            valueDescription = "Constant Surface Speed",
            unit = "m/min",
            minValue = 1.0,
            maxValue = 500.0,
            initialValue = 50.0
        )
        map[InputType.CSS_MAX_RPM] = NumInputParameters(
            valueDescription = "Spindle Max Speed",
            unit = "rev/min",
            minValue = 10.0,
            maxValue = 3000.0,
            initialValue = 100.0
        )
        map[InputType.ORIENTED_STOP] = NumInputParameters(
            valueDescription = "Oriented Stop Angle",
            unit = "degrees",
            minValue = 0.0,
            maxValue = 360.0,
            initialValue = 100.0,
            maxDecimalPlaces = 1
        )
        map[InputType.FEED_PER_REV] = NumInputParameters(
            valueDescription = "Feed per revolution",
            unit = "mm/rev",
            minValue = 0.010,
            maxValue = 5.000,
            initialValue = 0.100,
            maxDecimalPlaces = 3
        )
        map[InputType.FEED_PER_MIN] = NumInputParameters(
            valueDescription = "Feed per minute",
            unit = "mm/min",
            minValue = 10.0,
            maxValue = 500.0,
            initialValue = 10.0,
            maxDecimalPlaces = 1
        )
        map[InputType.TOUCH_OFF_X] = NumInputParameters(
            valueDescription = "Touch-Off X",
            unit = "diameter",
            minValue = 0.010,
            maxValue = 320.000,
            initialValue = 10.0,
            maxDecimalPlaces = 3
        )
        map[InputType.TOUCH_OFF_Z] = NumInputParameters(
            valueDescription = "Touch-Off Z",
            unit = "",
            minValue = 0.001,
            maxValue = 650.000,
            initialValue = 10.0,
            maxDecimalPlaces = 3
        )
        map[InputType.TOOL_CLEARANCE] = NumInputParameters(
            valueDescription = "Tool Clearance",
            unit = "mm",
            minValue = 0.1,
            maxValue = 100.000,
            initialValue = 1.0,
            maxDecimalPlaces = 3
        )
        map[InputType.TOOL_NUMBER] = NumInputParameters(
            valueDescription = "Tool #",
            minValue = 1.0,
            maxValue = 99.0,
            initialValue = 1.0,
        )
        map[InputType.DOC] = NumInputParameters(
            valueDescription = "Depth of cut",
            minValue = 0.05,
            maxValue = 5.0,
            initialValue = 1.0,
            maxDecimalPlaces = 3

        )
        map[InputType.FINISH_PASSES] = NumInputParameters(
            valueDescription = "Finish passes",
            minValue = 0.05,
            maxValue = 5.0,
            initialValue = 1.0,
        )
        map[InputType.THREAD_STARTS] = NumInputParameters(
            valueDescription = "Thread Starts",
            minValue = 1.0,
            maxValue = 10.0,
            initialValue = 1.0,
        )
        map[InputType.THREAD_PITCH] = NumInputParameters(
            valueDescription = "Thread Pitch",
            minValue = 0.5,
            maxValue = 10.0,
            initialValue = 1.0,
        )
        map[InputType.THREAD_X_START] = NumInputParameters(
            valueDescription = "X Start",
            minValue = 0.0,
            maxValue = 300.0,
            initialValue = 0.0,
            maxDecimalPlaces = 3
        )
        map[InputType.THREAD_MAJOR_DIAMETER] = NumInputParameters(
            valueDescription = "Major Diameter",
            minValue = 0.0,
            maxValue = 300.0,
            initialValue = 0.0,
            maxDecimalPlaces = 3
        )
        map[InputType.THREAD_MINOR_DIAMETER] = NumInputParameters(
            valueDescription = "Minor Diameter",
            minValue = 0.0,
            maxValue = 300.0,
            initialValue = 0.0,
            maxDecimalPlaces = 3
        )
        map[InputType.THREAD_Z_START] = NumInputParameters(
            valueDescription = "Z Start",
            minValue = 0.0,
            maxValue = 600.0,
            initialValue = 0.0,
            maxDecimalPlaces = 3
        )
        map[InputType.THREAD_Z_END] = NumInputParameters(
            valueDescription = "Z End",
            minValue = 0.0,
            maxValue = 600.0,
            initialValue = 0.0,
            maxDecimalPlaces = 3
        )
        map[InputType.THREAD_LEAD_LENGTH] = NumInputParameters(
            valueDescription = "Lead Length",
            minValue = 0.0,
            maxValue = 20.0,
            initialValue = 3.0,
            maxDecimalPlaces = 3
        )
        map[InputType.X_START] = NumInputParameters(
            valueDescription = "Initial X",
            minValue = 0.0,
            maxValue = 300.0,
            initialValue = 0.0,
            allowsNegativeValues = true,
            maxDecimalPlaces = 3
        )
        map[InputType.X_END] = NumInputParameters(
            valueDescription = "Final X",
            minValue = 0.0,
            maxValue = 300.0,
            initialValue = 0.0,
            allowsNegativeValues = true,
            maxDecimalPlaces = 3
        )
        map[InputType.Z_START] = NumInputParameters(
            valueDescription = "Z Start",
            minValue = -500.0,
            maxValue = 500.0,
            initialValue = 0.0,
            allowsNegativeValues = true,
            maxDecimalPlaces = 3
        )
        map[InputType.Z_END] = NumInputParameters(
            valueDescription = "Z End",
            minValue = -500.0,
            maxValue = 500.0,
            initialValue = 0.0,
            allowsNegativeValues = true,
            maxDecimalPlaces = 3
        )
        map[InputType.FILLET_RADIUS] = NumInputParameters(
            valueDescription = "Fillet Radius",
            unit = "mm",
            minValue = -500.0,
            maxValue = 500.0,
            initialValue = 0.0,
            maxDecimalPlaces = 3
        )
        map[InputType.TOOL_X_COORDINATE] = NumInputParameters(
            valueDescription = "Tool X Coordinate",
            unit = "mm",
            minValue = -500.0,
            maxValue = 500.0,
            initialValue = 0.0,
            allowsNegativeValues = true,
            maxDecimalPlaces = 3
        )
        map[InputType.TOOL_Z_COORDINATE] = NumInputParameters(
            valueDescription = "Tool Z Coordinate",
            unit = "mm",
            minValue = -500.0,
            maxValue = 500.0,
            initialValue = 0.0,
            allowsNegativeValues = true,
            maxDecimalPlaces = 3
        )
        map[InputType.TIP_RADIUS] = NumInputParameters(
            valueDescription = "Tip Radius",
            unit = "mm",
            minValue = 0.0,
            maxValue = 20.0,
            initialValue = 0.0,
            maxDecimalPlaces = 3
        )
        map[InputType.BLADE_WIDTH] = NumInputParameters(
            valueDescription = "Blade Width",
            unit = "mm",
            minValue = 0.0,
            maxValue = 20.0,
            initialValue = 0.0,
            maxDecimalPlaces = 3
        )
        map[InputType.DIAMETER] = NumInputParameters(
            valueDescription = "Diameter",
            unit = "mm",
            minValue = 0.0,
            maxValue = 50.0,
            initialValue = 0.0,
            maxDecimalPlaces = 3
        )
        map[InputType.TAPER_ANGLE] = NumInputParameters(
            valueDescription = "Taper Angle",
            unit = "degrees",
            minValue = 1.0,
            maxValue = 89.0,
            initialValue = 45.0,
            maxDecimalPlaces = 0
        )
        map[InputType.VIRTUAL_LIMIT_X_MINUS] = NumInputParameters(
            valueDescription = "X Minus Limit",
            allowsNegativeValues = true,
            maxDecimalPlaces = 3
        )
        map[InputType.VIRTUAL_LIMIT_X_PLUS] = NumInputParameters(
            valueDescription = "X Plus Limit",
            allowsNegativeValues = true,
            maxDecimalPlaces = 3
        )
        map[InputType.VIRTUAL_LIMIT_Z_MINUS] = NumInputParameters(
            valueDescription = "Z Minus Limit",
            allowsNegativeValues = true,
            maxDecimalPlaces = 3
        )
        map[InputType.VIRTUAL_LIMIT_Z_PLUS] = NumInputParameters(
            valueDescription = "Z Plus Limit",
            allowsNegativeValues = true,
            maxDecimalPlaces = 3
        )
        map[InputType.DIAL_INDICATOR_DISTANCE] = NumInputParameters(
            valueDescription = "Dial indicator distance",
            allowsNegativeValues = true,
            maxDecimalPlaces = 3
        )
        map[InputType.WORKPIECE_ZERO_COORDINATE] = NumInputParameters(
            valueDescription = "ToolTip to Z0 distance",
            allowsNegativeValues = true,
            maxDecimalPlaces = 3
        )
    }
}