package screen.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.kodein.di.compose.rememberInstance
import usecase.ManualTurningUseCase

@Composable
fun RootScreenView(
    turningSettingsClicked: () -> Unit,
    toolLibraryClicked: () -> Unit,
    offsetsClicked: () -> Unit,
    programsClicked: () -> Unit,
    conversationalClicked: () -> Unit
) {

    val useCase: ManualTurningUseCase by rememberInstance()

    val scope = rememberCoroutineScope()
//
//    val snackbarHostState = remember{ SnackbarHostState().apply {
//        scope.launch {
//            showSnackbar("Test Mesage", duration = SnackbarDuration.Short)
//        }
//    }}

    val taperTurningActive by useCase.taperTurningActive.collectAsState()

    Column(
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        CoordinatesView()
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.Start
        ) {
            Column(
                modifier = Modifier
                    //.width(IntrinsicSize.Max)
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SpindleStatusView(
                        Modifier.width(380.dp)
                            .padding(8.dp)
                            .clickable(onClick = turningSettingsClicked)

                    )
                    FeedStatusView(
                        Modifier.width(380.dp)
                            .padding(8.dp)
                            .clickable(onClick = turningSettingsClicked)
                    )
                }
                //SnackbarHost(snackbarHostState, modifier = Modifier.fillMaxWidth())
            }
            Column(
                modifier = Modifier.width(180.dp)
                    .fillMaxHeight()
                    .background(Color.LightGray),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                val btnModifier = Modifier.height(50.dp)

                Button(
                    modifier = btnModifier,
                    onClick = {
                        useCase.toggleTaperTurning()
                    }) {
                    Text(if (taperTurningActive) "Taper Turning OFF" else "Taper Turning ON")
                }

                Button(
                    modifier = btnModifier,
                    onClick = {
                        toolLibraryClicked.invoke()
                    }) {
                    Text("Tool Library")
                }

                Button(
                    modifier = btnModifier,
                    onClick = {
                        offsetsClicked.invoke()
                    }) {
                    Text("Offsets")
                }

                Button(
                    modifier = btnModifier,
                    onClick = {
                        programsClicked.invoke()
                    }) {
                    Text("Programs")
                }

                Button(
                    modifier = btnModifier,
                    onClick = {
                        conversationalClicked.invoke()
//                    val generator = ThreadGenerator(1.0, -10.0, 1.0, 0.2)
//                    println(generator.getGCode())

//                    val generator = TurningGenerator(
//                        TurningProfile(),
//                        startingXPosition = 30.0,
//                        startingZPosition = 1.0,
//                        turningStrategies = listOf(
//                            TurningGenerator.TurningStrategy.Roughing(
//                                toolNumber = 1,
//                                remainingDistance = 1.0,
//                                cuttingIncrement = 1.5,
//                                retractDistance = 0.5,
//                                cutType = TurningGenerator.CutType.Straight,
//                                direction = TurningGenerator.TraverseDirection.ZAxis
//                            ),
//                            TurningGenerator.TurningStrategy.Roughing(
//                                toolNumber = 1,
//                                remainingDistance = 1.0,
//                                cuttingIncrement = 1.5,
//                                retractDistance = 0.5,
//                                cutType = TurningGenerator.CutType.Pocket,
//                                direction = TurningGenerator.TraverseDirection.ZAxis
//                            ),
//                            TurningGenerator.TurningStrategy.Finishing(
//                                toolNumber = 1,
//                                startingDistance = 0.0,
//                                endingDistance = 0.0,
//                                numberOfPasses = 1,
//                            )
//                        )
//                    )
//                    generator.getGCode().forEach {
//                        println(it)
//                    }

                    }) {
                    Text("Conversational")
                }

            }
        }
        Divider(
            color = Color.DarkGray,
            thickness = 1.dp
        )
        Row(
            modifier = Modifier.height(50.dp)
        ) {
            HandwheelStatus()
            JoystickStatus()
            ToolStatus()
        }
    }
}