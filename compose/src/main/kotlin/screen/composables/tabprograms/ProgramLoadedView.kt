package screen.composables.tabprograms

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.kodein.di.compose.rememberInstance
import screen.composables.VerticalDivider
import screen.composables.common.Settings
import screen.composables.editor.EditorEmptyView
import screen.composables.editor.EditorView
import usecase.ProgramsUseCase

@Composable
fun ProgramLoadedView(
    modifier: Modifier
) {
    val useCase: ProgramsUseCase by rememberInstance()
    val currentEditor by useCase.currentEditor.collectAsState(null)
    val settings = Settings()

//    val state = remember {
//        val cone = vtkConeSource()
//        cone.SetResolution(8)
//
//        val coneMapper = vtkPolyDataMapper()
//        coneMapper.SetInputConnection(cone.GetOutputPort())
//
//        val coneActor = vtkActor()
//        coneActor.SetMapper(coneMapper)
//        VtkState(coneActor)
//    }

    Row(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.width(600.dp).fillMaxHeight()
        ) {
            //VtkView(state, modifier = Modifier.fillMaxWidth().height(300.dp))
            if (currentEditor != null) {
                EditorView(currentEditor!!, settings)
            } else {
                EditorEmptyView()
            }
        }
        VerticalDivider()
        Column(
            modifier = Modifier.weight(1f).fillMaxHeight()
        ) {

        }
    }
}