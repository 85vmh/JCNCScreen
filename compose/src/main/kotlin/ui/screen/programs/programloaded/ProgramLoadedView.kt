//package screen.composables.tabprograms
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.Button
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import extensions.toFixedDigitsString
//import org.kodein.di.compose.rememberInstance
//import screen.composables.ProgramCoordinatesView
//import screen.composables.SettingStatusRow
//import screen.composables.VerticalDivider
//import screen.composables.common.Settings
//import screen.composables.editor.EditorEmptyView
//import screen.composables.editor.EditorView
//import usecase.ProgramsUseCase
//import usecase.model.SpindleControlMode
//
//@Composable
//fun ProgramLoadedView(
//    modifier: Modifier
//) {
//    val programsUseCase: ProgramsUseCase by rememberInstance()
//    val currentEditor by programsUseCase.currentEditor.collectAsState(null)
//    val settings = Settings()
//
////    val state = remember {
////        val cone = vtkConeSource()
////        cone.SetResolution(8)
////
////        val coneMapper = vtkPolyDataMapper()
////        coneMapper.SetInputConnection(cone.GetOutputPort())
////
////        val coneActor = vtkActor()
////        coneActor.SetMapper(coneMapper)
////        VtkState(coneActor)
////    }
//
//    Row(
//        modifier = modifier
//    ) {
//        Column(
//            modifier = Modifier.width(600.dp).fillMaxHeight()
//        ) {
//            //VtkView(state, modifier = Modifier.fillMaxWidth().height(300.dp))
//            if (currentEditor != null) {
//                EditorView(currentEditor!!, settings)
//            } else {
//                EditorEmptyView()
//            }
//        }
//        VerticalDivider()
//        Column(
//            modifier = Modifier
//                .weight(1f)
//                .padding(8.dp)
//                .fillMaxHeight()
//        ) {
//            ProgramCoordinatesView()
//
////            SettingStatusRow("Set feed:", setFeed.toFixedDigitsString(), feed.units, modifier = settingsModifier)
////            SettingStatusRow("Actual feed:", actualSpeed.toFixedDigitsString(), feed.units, modifier = settingsModifier)
//
////            SettingStatusRow("Set ${spModeWithUnits.mode}:", spModeWithUnits.value, spModeWithUnits.units, modifier = settingsModifier)
////            if (useCase.getSpindleState().spindleMode.value == SpindleControlMode.CSS) {
////                SettingStatusRow("Max RPM:", useCase.getSpindleState().maxCssRpm.value.toString(), "rev/min", modifier = settingsModifier)
////            }
////            SettingStatusRow("Actual RPM:", kotlin.math.abs(actualSpeed).toString(), "rev/min", modifier = settingsModifier)
//
//            Spacer(modifier = Modifier.weight(1f))
//            Row(
//                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Button(onClick = {
//                    programsUseCase.stopProgram()
//                }) {
//                    Text("Stop Program")
//                }
//                Button(onClick = {
//                    programsUseCase.runProgram()
//                }) {
//                    Text("Run Program")
//                }
//            }
//        }
//    }
//}