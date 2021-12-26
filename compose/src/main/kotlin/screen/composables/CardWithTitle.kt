package screen.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CardWithTitle(
    cardTitle: String,
    titleSize: TextUnit = 20.sp,
    contentSpacing: Dp = 8.dp,
    modifier: Modifier = Modifier,
    borderColor: Color = Color.DarkGray,
    cardCornerRadius: Dp = 8.dp,
    cardElevation: Dp = 8.dp,
    content: @Composable () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(cardCornerRadius),
        modifier = modifier,
        border = BorderStroke(1.dp, SolidColor(borderColor)),
        elevation = cardElevation
    ) {
        Column(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                fontSize = titleSize,
                text = cardTitle
            )
            Divider(
                modifier = Modifier,
                color = Color.DarkGray,
                thickness = 1.dp
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(contentSpacing)
            ) {
                content.invoke()
            }
        }
    }
}