package screen.composables

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import screen.uimodel.AxisPosition
import themes.ComposeFonts
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
@Preview
private fun CoordinatesPreview() {
    Column {
        CoordinateView(AxisPosition(axis = AxisPosition.Axis.X, absValue = 123.562788993, relValue = -23.2213, units = AxisPosition.Units.MM))
        CoordinateView(AxisPosition(axis = AxisPosition.Axis.Z, absValue = 123.562788993, units = AxisPosition.Units.MM))
    }
}

enum class PositionType {
    ABS,
    REL,
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun CoordinateView(axisPosition: AxisPosition, isDiameterMode: Boolean = false) {
    Box(modifier = Modifier.padding(8.dp)) {
        Row(
            modifier = Modifier
                .height(80.dp)
                .border(BorderStroke(0.5.dp, SolidColor(Color.DarkGray)))
        ) {
            Position(PositionType.REL, axisPosition, isDiameterMode)
            AxisLetter(axisPosition)
            SpacerOrDiameter(axisPosition.axis == AxisPosition.Axis.X && isDiameterMode)
            Position(PositionType.ABS, axisPosition, isDiameterMode)
            Units(axisPosition.units)
        }
    }
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun AxisLetter(axisPosition: AxisPosition) {
    Text(
        modifier = Modifier.padding(start = 16.dp),
        text = axisPosition.axis.name,
        fontSize = TextUnit(70f, TextUnitType.Sp),
    )
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun SpacerOrDiameter(showDiameter: Boolean) {
    val sizeToFill = 50.dp
    if (showDiameter) {
        Text(
            modifier = Modifier
                .width(sizeToFill)
                .fillMaxHeight(),
            text = "\u2300",
            textAlign = TextAlign.Center,
            fontSize = TextUnit(60f, TextUnitType.Sp),
        )
    } else {
        Box(modifier = Modifier.width(sizeToFill))
    }
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun Position(positionType: PositionType, axisPosition: AxisPosition, isDiameterMode: Boolean = false) {

    data class PosSetting(val value: Double?, val fontSize: TextUnit, val width: Dp, val alignment: Alignment, val boxModifier: Modifier)

    val posSetting = when (positionType) {
        PositionType.ABS -> PosSetting(
            value = axisPosition.absValue,
            fontSize = TextUnit(60f, TextUnitType.Sp),
            width = 300.dp,
            alignment = Alignment.CenterEnd,
            boxModifier = Modifier
        )
        PositionType.REL -> PosSetting(
            value = axisPosition.relValue,
            fontSize = TextUnit(20f, TextUnitType.Sp),
            width = 100.dp,
            alignment = Alignment.BottomEnd,
            boxModifier = Modifier.padding(bottom = 12.dp)
        )
    }
    Box(
        modifier = posSetting.boxModifier.fillMaxHeight().width(posSetting.width),
        contentAlignment = posSetting.alignment,
    ) {
        posSetting.value?.let {
            Text(
                modifier = Modifier.width(posSetting.width).align(posSetting.alignment),
                text = BigDecimal(it * (if (isDiameterMode) 2 else 1))
                    .setScale(axisPosition.units.displayDigits, RoundingMode.HALF_EVEN).toString(),
                fontSize = posSetting.fontSize,
                fontFamily = ComposeFonts.Family.position,
                fontWeight = FontWeight.Thin,
                textAlign = TextAlign.End
            )
        }
    }
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun Units(units: AxisPosition.Units) {
    Box(
        modifier = Modifier.fillMaxHeight()
    ) {
        Text(
            text = units.name.lowercase(),
            modifier = Modifier.align(Alignment.BottomEnd).padding(start = 4.dp, end = 8.dp, bottom = 20.dp),
            fontSize = TextUnit(20f, TextUnitType.Sp),
            fontWeight = FontWeight.Medium
        )
    }
}

