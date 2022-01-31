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
    }
}