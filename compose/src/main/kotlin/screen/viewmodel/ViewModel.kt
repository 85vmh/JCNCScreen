package screen.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope

abstract class ViewModel(
    val scope: CoroutineScope
)

//@Composable
//fun <VM : ViewModel> rememberViewModel(scope: CoroutineScope = rememberCoroutineScope()) : VM {
//    return remember {
//        ViewModel(scope)
//    }
//}