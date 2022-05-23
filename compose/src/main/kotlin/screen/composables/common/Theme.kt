package screen.composables.common

import androidx.compose.material.darkColors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import com.mindovercnc.editor.EditorTheme
import com.mindovercnc.editor.IntColor

object AppTheme {

    val editorTheme = EditorTheme(
        comment = IntColor(0xFF808080),
        value = IntColor(0xFF6897BB),
        text = IntColor(0xFFA9B7C6),
        keyword = IntColor(0xFFCC7832),
        punctuation = IntColor(0xFFA1C17E),
        background = IntColor(0xFFFFFFFF),
        lineNumber = IntColor(0xFFCECECE)
    )

    class Colors(
        val backgroundDark: Color = Color(0xFF2B2B2B),
        val backgroundMedium: Color = Color(0xFF3C3F41),
        val backgroundLight: Color = Color(0xFF4E5254),

        val material: androidx.compose.material.Colors = darkColors(
            background = backgroundDark,
            surface = backgroundMedium,
            primary = Color.White
        ),

        val textDisabled: Color = Color(0x772B2B2B)
    )

}