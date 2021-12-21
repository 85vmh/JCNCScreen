package screen.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import org.kodein.di.compose.rememberInstance
import usecase.ConversationalUseCase

@Composable
fun ConversationalView(onFinish: () -> Unit) {
    val useCase: ConversationalUseCase by rememberInstance()
    val scope = rememberCoroutineScope()

    val kbState = mutableStateOf("")

    val numPadState = remember {
        NumPadState().apply {
            setFieldState(kbState)
        }
    }

    Column {
        Row(
            modifier = Modifier.weight(1f)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.Top)
                    .padding(16.dp)
            ) {
                ConversationalTabs()

                Card(
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier,
                    border = BorderStroke(1.dp, SolidColor(Color.DarkGray)),
                    elevation = 8.dp
                ) {
                    TechnologyDataView()
                }


            }
            Spacer(modifier = Modifier.fillMaxHeight().width(1.dp).background(color = Color.Black))
            NumPadView(
                modifier = Modifier.fillMaxHeight().width(300.dp),
                state = numPadState
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 10.dp)
                .height(50.dp)
                .background(Color(170, 170, 180))
        ) {
            Button(
                modifier = Modifier.fillMaxHeight(),
                onClick = {
                    onFinish.invoke()
                }) {
                Text("<- Back")
            }
        }
    }
}

@Composable
fun ConversationalTabs() {
    val tabIndex = remember { mutableStateOf(0) }
    val tabData = listOf(
        "Technology Data",
        "Geometry Data"
    )
    TabRow(selectedTabIndex = tabIndex.value) {
        tabData.forEachIndexed { index, text ->
            Tab(selected = tabIndex.value == index,
                onClick = {
                    tabIndex.value = index
                },
                text = {
                    Text(text = text)
                })
        }
    }
}
