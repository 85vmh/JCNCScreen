package screen.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
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
import usecase.ManualPositionUseCase

private enum class PositionType(
    val fontSize: TextUnit,
    val width: Dp
) {
    PRIMARY(50.sp, 300.dp),
    SECONDARY(18.sp, 110.dp),
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ManualCoordinatesView(modifier: Modifier = Modifier) {
    val useCase: ManualPositionUseCase by rememberInstance()
    val model = useCase.uiModel.collectAsState(null).value

    Row(
        modifier = modifier
            .border(BorderStroke(0.5.dp, SolidColor(Color.DarkGray))),
        horizontalArrangement = Arrangement.End
    ) {
        Column(
            horizontalAlignment = Alignment.End,
        ) {
            model?.let {
                AxisCoordinate(
                    it.xAxisPos,
                    it.isDiameterMode,
                    zeroPosClicked = { useCase.setZeroPosX() },
                    absRelClicked = { useCase.toggleXAbsRel() }
                )
                //Spacer(modifier = Modifier.height(16.dp))
                AxisCoordinate(
                    it.zAxisPos,
                    zeroPosClicked = { useCase.setZeroPosZ() },
                    absRelClicked = { useCase.toggleZAbsRel() }
                )
            }
        }
    }
}

@Composable
private fun AxisCoordinate(
    axisPosition: AxisPosition,
    isDiameterMode: Boolean = false,
    modifier: Modifier = Modifier,
    zeroPosClicked: () -> Unit,
    absRelClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(80.dp)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = modifier.background(Color(0xffd8e6ff))
        ) {
            //val line = HorizontalAlignmentLine()
            Position(PositionType.SECONDARY, axisPosition, isDiameterMode, modifier = Modifier.alignByBaseline())
            AxisLetter(axisPosition)
            SpacerOrDiameter(axisPosition.axis == AxisPosition.Axis.X && isDiameterMode, modifier = Modifier.align(Alignment.CenterVertically))
            Position(PositionType.PRIMARY, axisPosition, isDiameterMode, modifier = Modifier.alignByBaseline())
            Units(axisPosition.units, modifier = Modifier.alignByBaseline())
            ZeroPos(axisPosition, modifier = Modifier.padding(start = 16.dp)) {
                zeroPosClicked.invoke()
            }
            AbsRel(modifier = Modifier.padding(start = 16.dp)) {
                absRelClicked.invoke()
            }
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
                .padding(top = 16.dp)
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
        modifier = modifier,
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium
    )
}

@Composable
private fun ZeroPos(axisPosition: AxisPosition, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier.fillMaxHeight()
    ) {
        Text("ZERO\n${axisPosition.axis.name}-Pos")
    }
}

@Composable
private fun AbsRel(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(onClick = onClick, modifier.fillMaxHeight()) {
        Text("ABS\nREL")
    }
}

