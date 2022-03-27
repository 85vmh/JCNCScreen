package screen.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import org.kodein.di.Instance
import org.kodein.di.compose.localDI
import org.kodein.type.TypeToken
import org.kodein.type.generic

abstract class ViewModel {

    lateinit var scope: CoroutineScope

    open fun onCreate() {
        //todo
    }

    internal fun initialise(scope: CoroutineScope) {
        this.scope = scope
        onCreate()
    }

}

@Composable
fun <VM : ViewModel> createViewModel(
    scope: CoroutineScope,
    typeToken: TypeToken<VM>
): VM {
    val viewModel: VM by localDI().Instance(typeToken)
    viewModel.initialise(scope)
    return viewModel
}

@Composable
inline fun <reified VM : ViewModel> createViewModel(): VM {
    val scope: CoroutineScope = rememberCoroutineScope()
    return createViewModel(scope, generic())
}