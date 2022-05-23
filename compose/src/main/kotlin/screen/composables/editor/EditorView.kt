package screen.composables.editor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.mindovercnc.editor.EditorTheme
import extensions.draggableScroll
import screen.composables.common.AppTheme
import screen.composables.common.Fonts
import screen.composables.common.Settings
import screen.composables.platform.VerticalScrollbar
import screen.composables.util.loadableScoped
import screen.composables.util.withoutWidthConstraints
import kotlin.text.Regex.Companion.fromLiteral

@Composable
fun EditorView(
    model: Editor,
    settings: Settings,
    modifier: Modifier = Modifier
) = key(model) {
    val editorTheme = LocalEditorTheme.current
    SelectionContainer {
        Surface(
            modifier = modifier,
            color = editorTheme.background.toColor(),
        ) {
            val lines by loadableScoped(model.lines)

            if (lines != null) {
                Lines(lines!!, settings)
            } else {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(36.dp)
                        .padding(4.dp)
                )
            }
        }
    }
}

@Composable
private fun Lines(lines: Editor.Lines, settings: Settings) = with(LocalDensity.current) {
    val maxNum = remember(lines.lineNumberDigitCount) {
        (1..lines.lineNumberDigitCount).joinToString(separator = "") { "9" }
    }

    val scope = rememberCoroutineScope()

    Box(
        Modifier.fillMaxSize()
    ) {
        val scrollState = rememberLazyListState()
        val lineHeight = settings.fontSize.toDp() * 2f

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .draggableScroll(scrollState, scope),
            state = scrollState
        ) {
            items(lines.size) { index ->
//                val selectedModifier = when {
//                    index == 5 -> Modifier.height(lineHeight)
//                        //.align(Alignment.Center)
//                        .border(BorderStroke(1.dp, Color.Blue))
//                    else -> Modifier.height(lineHeight)
//                }
                Box(
                    modifier = Modifier.height(lineHeight)
                ) {
                    Line(
                        maxNum = maxNum,
                        line = lines[index],
                        settings = settings,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                }
            }
        }

        VerticalScrollbar(
            Modifier.align(Alignment.CenterEnd),
            scrollState,
            lines.size,
            lineHeight
        )
    }
}

@Composable
private fun Line(
    maxNum: String,
    line: Editor.Line,
    settings: Settings,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        DisableSelection {
            Box {
                LineNumber(
                    number = maxNum,
                    settings = settings,
                    modifier = Modifier.alpha(0f).padding(start = 12.dp)
                )
                LineNumber(
                    number = line.number.toString(),
                    settings = settings,
                    modifier = Modifier.align(Alignment.CenterEnd).padding(start = 12.dp)
                )
            }
            LineContent(
                line.content,
                modifier = Modifier
                    .weight(1f)
                    .withoutWidthConstraints()
                    .padding(start = 28.dp, end = 12.dp),
                settings = settings
            )
        }
    }
}

@Composable
private fun LineNumber(
    number: String,
    settings: Settings,
    modifier: Modifier = Modifier
) = Text(
    text = number,
    fontSize = settings.fontSize,
    fontFamily = Fonts.jetbrainsMono(),
    color = LocalEditorTheme.current.lineNumber.toColor(),
    modifier = modifier
)

@Composable
private fun LineContent(
    content: Editor.Content,
    settings: Settings,
    modifier: Modifier = Modifier
) {
    val editorTheme = LocalEditorTheme.current
    Text(
        text = if (content.isGCode) {
            codeString(editorTheme, content.value.value)
        } else {
            buildAnnotatedString {
                withStyle(editorTheme.text.toSpanStyle()) {
                    append(content.value.value)
                }
            }
        },
        fontSize = settings.fontSize,
        fontFamily = Fonts.jetbrainsMono(),
        modifier = modifier,
        softWrap = false
    )
}

private fun codeString(
    editorTheme: EditorTheme,
    str: String
): AnnotatedString {
    val keyword = editorTheme.keyword.toSpanStyle()
    val punctuation = editorTheme.punctuation.toSpanStyle()
    val value = editorTheme.value.toSpanStyle()
    val comment = editorTheme.comment.toSpanStyle()
    return buildAnnotatedString {
        withStyle(editorTheme.text.toSpanStyle()) {
            val strFormatted = str.replace("\t", "    ")
            append(strFormatted)
            addStyle(punctuation, strFormatted, ":")
            addStyle(punctuation, strFormatted, "=")
            addStyle(punctuation, strFormatted, "\"")
            addStyle(punctuation, strFormatted, "[")
            addStyle(punctuation, strFormatted, "]")
            addStyle(punctuation, strFormatted, "{")
            addStyle(punctuation, strFormatted, "}")
            addStyle(keyword, strFormatted, "SUB")
            addStyle(keyword, strFormatted, "ENDSUB")
            addStyle(keyword, strFormatted, "G0")
            addStyle(keyword, strFormatted, "G1")
            addStyle(keyword, strFormatted, "G2")
            addStyle(keyword, strFormatted, "G3")
            addStyle(keyword, strFormatted, "G54")
            addStyle(keyword, strFormatted, "G18")
            addStyle(keyword, strFormatted, "G21")
            addStyle(keyword, strFormatted, "G43")
            addStyle(keyword, strFormatted, "G71.1")
            addStyle(keyword, strFormatted, "G71.2")
            addStyle(keyword, strFormatted, "G70")
            addStyle(keyword, strFormatted, "G64")
            addStyle(keyword, strFormatted, "G21")
            addStyle(keyword, strFormatted, "M")
            addStyle(keyword, strFormatted, "X")
            addStyle(keyword, strFormatted, "Y")
            addStyle(keyword, strFormatted, "Z")
            addStyle(keyword, strFormatted, "I")
            addStyle(keyword, strFormatted, "J")
            addStyle(keyword, strFormatted, "K")
            addStyle(keyword, strFormatted, "F")
            addStyle(keyword, strFormatted, "S")
            addStyle(keyword, strFormatted, "T")
            addStyle(value, strFormatted, "true")
            addStyle(value, strFormatted, "false")
            //addStyle(AppTheme.code.value, strFormatted, Regex("[0-9]*"))
            //addStyle(AppTheme.code.annotation, strFormatted, Regex("^@[a-zA-Z_]*"))
            addStyle(comment, strFormatted, Regex("^\\s*;.*"))
            addStyle(comment, strFormatted, Regex("^\\s*\\(.*"))
        }
    }
}

private fun AnnotatedString.Builder.addStyle(style: SpanStyle, text: String, regexp: String) {
    addStyle(style, text, fromLiteral(regexp))
}

private fun AnnotatedString.Builder.addStyle(style: SpanStyle, text: String, regexp: Regex) {
    for (result in regexp.findAll(text)) {
        addStyle(style, result.range.first, result.range.last + 1)
    }
}