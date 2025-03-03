package screen.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import extensions.toFixedDigitsString
import org.kodein.di.compose.rememberInstance
import screen.uimodel.AxisPosition
import themes.ComposeFonts
import usecase.OffsetsUseCase
import usecase.ProgramsUseCase

private enum class CoordinateType(
    val fontSize: TextUnit,
    val width: Dp
) {
    PRIMARY(28.sp, 140.dp),
    SECONDARY(28.sp, 140.dp),
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProgramCoordinatesView(modifier: Modifier = Modifier) {
    val programsUseCase: ProgramsUseCase by rememberInstance()
    val offsetsUseCase: OffsetsUseCase by rememberInstance()

    val currentWcs by offsetsUseCase.currentWcs.collectAsState("--")
    val model = programsUseCase.uiModel.collectAsState(null).value

    Row(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column {
            model?.let {
                CoordinatesHeader(currentWcs)
                Spacer(modifier = Modifier.height(8.dp))
                AxisCoordinate(
                    it.xAxisPos,
                    it.isDiameterMode
                )
                Spacer(modifier = Modifier.height(8.dp))
                AxisCoordinate(
                    it.zAxisPos
                )
            }
        }
    }
}

@Composable
private fun CoordinatesHeader(
    currentWcs: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            modifier = Modifier.padding(end = 124.dp),
            text = "$currentWcs POS",
            fontSize = 24.sp
        )
        Text(
            modifier = Modifier.padding(end = 32.dp),
            text = "DTG",
            fontSize = 24.sp)
    }
}

@Composable
private fun AxisCoordinate(
    axisPosition: AxisPosition,
    isDiameterMode: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = Modifier
            .background(Color.DarkGray),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .background(Color(0xffd8e6ff))
        ) {
            //val line = HorizontalAlignmentLine()
            AxisLetter(axisPosition, modifier = Modifier)
            SpacerOrDiameter(axisPosition.axis == AxisPosition.Axis.X && isDiameterMode, modifier = Modifier)
            Position(CoordinateType.PRIMARY, axisPosition, isDiameterMode, modifier = Modifier)
            Units(axisPosition.units, modifier = Modifier)
            Spacer(modifier = Modifier.width(8.dp))
            Position(CoordinateType.SECONDARY, axisPosition, isDiameterMode, modifier = Modifier)
            Units(axisPosition.units, modifier = Modifier)
        }
    }
}

@Composable
private fun AxisLetter(axisPosition: AxisPosition, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier.padding(start = 8.dp),
        text = axisPosition.axis.name,
        fontSize = 40.sp,
    )
}

@Composable
private fun SpacerOrDiameter(showDiameter: Boolean, modifier: Modifier = Modifier) {
    val sizeToFill = 30.dp
    if (showDiameter) {
        Text(
            modifier = modifier
                .width(sizeToFill),
            text = "\u2300",
            textAlign = TextAlign.Start,
            fontSize = 20.sp,
        )
    } else {
        Spacer(modifier = modifier.width(sizeToFill))
    }
}

@Composable
private fun Position(positionType: CoordinateType, axisPosition: AxisPosition, isDiameterMode: Boolean = false, modifier: Modifier = Modifier) {

    val value = when (positionType) {
        CoordinateType.PRIMARY -> axisPosition.primaryValue
        CoordinateType.SECONDARY -> axisPosition.secondaryValue
    }
    if (value != null) {
        Text(
            modifier = modifier.width(positionType.width),
            text = (value * (if (isDiameterMode) 2 else 1)).toFixedDigitsString(axisPosition.units.displayDigits),
            fontSize = positionType.fontSize,
            fontFamily = ComposeFonts.Family.position,
            fontWeight = FontWeight.Thin,
            textAlign = TextAlign.End
        )
    } else {
        Spacer(modifier = modifier.width(positionType.width))
    }
}

@Composable
private fun Units(units: AxisPosition.Units, modifier: Modifier = Modifier) {
    Text(
        text = units.name.lowercase(),
        modifier = modifier.padding(top = 6.dp),
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium
    )
}

