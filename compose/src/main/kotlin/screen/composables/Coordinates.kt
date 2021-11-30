package screen.composables

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import extensions.trimDigits
import org.kodein.di.compose.rememberInstance
import screen.uimodel.AxisPosition
import screen.viewmodel.CoordinatesViewModel
import themes.ComposeFonts
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
@Preview
private fun CoordinatesPreview() {
    Column {
        AxisCoordinate(AxisPosition(axis = AxisPosition.Axis.X, primaryValue = 123.562788993, secondaryValue = -23.2213, units = AxisPosition.Units.MM))
        AxisCoordinate(AxisPosition(axis = AxisPosition.Axis.Z, primaryValue = 123.562788993, units = AxisPosition.Units.MM))
    }
}

private enum class PositionType(
    val fontSize: TextUnit,
    val width: Dp
) {
    PRIMARY(50.sp, 300.dp),
    SECONDARY(18.sp, 110.dp),
}

@Composable
fun CoordinatesView(modifier: Modifier = Modifier) {
    val viewModel: CoordinatesViewModel by rememberInstance()
    val model by viewModel.uiModel.collectAsState(null)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(BorderStroke(0.5.dp, SolidColor(Color.DarkGray))),
        horizontalArrangement = Arrangement.End
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly

        ) {
            model?.let {
                AxisCoordinate(it.xAxisPos, it.isDiameterMode)
                AxisCoordinate(it.zAxisPos)
            }
        }
    }
}

@Composable
private fun AxisCoordinate(axisPosition: AxisPosition, isDiameterMode: Boolean = false, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .height(80.dp)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = modifier
                .background(Color(0xffd8e6ff))
        ) {
            //val line = HorizontalAlignmentLine()

            Position(PositionType.SECONDARY, axisPosition, isDiameterMode, modifier = Modifier.alignByBaseline())
            AxisLetter(axisPosition)
            SpacerOrDiameter(axisPosition.axis == AxisPosition.Axis.X && isDiameterMode, modifier = Modifier.alignByBaseline())
            Position(PositionType.PRIMARY, axisPosition, isDiameterMode, modifier = Modifier.alignByBaseline())
            Units(axisPosition.units, modifier = Modifier.alignByBaseline())
            ZeroPos(axisPosition, modifier = Modifier.padding(start = 16.dp))
            AbsRel(modifier = Modifier.padding(start = 16.dp))
        }
    }
}

@Composable
private fun AxisLetter(axisPosition: AxisPosition, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier.padding(start = 16.dp),
        text = axisPosition.axis.name,
        fontSize = 50.sp,
    )
}

@Composable
private fun SpacerOrDiameter(showDiameter: Boolean, modifier: Modifier = Modifier) {
    val sizeToFill = 35.dp
    if (showDiameter) {
        Text(
            modifier = modifier
                .width(sizeToFill)
                .fillMaxHeight(),
            text = "\u2300",
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
        )
    } else {
        Spacer(modifier = modifier.width(sizeToFill))
    }
}

@Composable
private fun Position(positionType: PositionType, axisPosition: AxisPosition, isDiameterMode: Boolean = false, modifier: Modifier = Modifier) {

    val value = when (positionType) {
        PositionType.PRIMARY -> axisPosition.primaryValue
        PositionType.SECONDARY -> axisPosition.secondaryValue
    }
    if (value != null) {
        Text(
            modifier = modifier.width(positionType.width),
            text = (value * (if (isDiameterMode) 2 else 1)).trimDigits(axisPosition.units.displayDigits),
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
        modifier = modifier,
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium
    )
}

@Composable
private fun ZeroPos(axisPosition: AxisPosition, modifier: Modifier = Modifier) {
    Button(
        onClick = {},
        modifier.fillMaxHeight()
    ) {
        Text("ZERO\n${axisPosition.axis.name}-Pos")
    }
}

@Composable
private fun AbsRel(modifier: Modifier = Modifier) {
    Button(onClick = {}, modifier.fillMaxHeight()) {
        Text("ABS\nREL")
    }
}

