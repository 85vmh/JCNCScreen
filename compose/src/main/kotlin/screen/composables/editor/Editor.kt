package screen.composables.editor

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import screen.composables.platform.File
import screen.composables.util.EmptyTextLines
import screen.composables.util.SingleSelection

class Editor(
    val fileName: String,
    val lines: (backgroundScope: CoroutineScope) -> Lines,
) {
    var close: (() -> Unit)? = null
    lateinit var selection: SingleSelection

    val isActive: Boolean
        get() = selection.selected === this

    fun activate() {
        selection.selected = this
    }

    class Line(val number: Int, val content: Content)

    interface Lines {
        val lineNumberDigitCount: Int get() = size.toString().length
        val size: Int
        operator fun get(index: Int): Line
    }

    class Content(val value: State<String>, val isGCode: Boolean)
}

fun Editor(file: File) = Editor(
    fileName = file.name
) { backgroundScope ->
    val textLines = try {
        file.readLines(backgroundScope)
    } catch (e: Throwable) {
        e.printStackTrace()
        EmptyTextLines
    }
    val isGCode = file.name.endsWith(".ngc", ignoreCase = true) || file.name.endsWith(".nc", ignoreCase = true)

    fun content(index: Int): Editor.Content {
        val text = textLines.get(index)
        val state = mutableStateOf(text)
        return Editor.Content(state, isGCode)
    }

    object : Editor.Lines {
        override val size get() = textLines.size

        override fun get(index: Int) = Editor.Line(
            number = index + 1,
            content = content(index)
        )
    }
}
