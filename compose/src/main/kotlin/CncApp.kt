import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import ui.tab.ManualTab

@Composable
fun CncApp() {
    TabNavigator(ManualTab) {
        CurrentTab()
    }
}