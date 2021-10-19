package screen.composables

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
@Preview
private fun CoordinatesPreview() {
    Position(123.34F)
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun Position(value: Float) {
    Box(modifier = Modifier.padding(16.dp)) {
        Row(

            modifier = Modifier.height(60.dp).background(Color.LightGray)
        ) {
            Text(
                text = "X",
                fontSize = TextUnit(50f, TextUnitType.Sp),
            )
            Text(
                modifier = Modifier.padding(start = 10.dp).fillMaxHeight(),
                text = "\u2300",
                fontSize = TextUnit(30f, TextUnitType.Sp),
            )
            Value(value)
            Units()
        }
    }
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun Value(value: Float) {
    Box(
        modifier = Modifier.fillMaxHeight()
    ) {
        Text(
            modifier = Modifier.padding(start = 50.dp).fillMaxHeight(),
            text = BigDecimal(value.toDouble()).setScale(3, RoundingMode.HALF_EVEN).toString(),
            fontSize = TextUnit(50f, TextUnitType.Sp)
        )
    }
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun Units() {
    Box(
        modifier = Modifier.fillMaxHeight()
    ) {
        Text(
            text = "mm",
            modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp),
            fontSize = TextUnit(20f, TextUnitType.Sp)
        )
    }
}

