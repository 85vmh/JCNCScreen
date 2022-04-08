package screen.composables.editor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.CircularProgressIndicator
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
import extensions.draggableScroll
import screen.composables.common.AppTheme
import screen.composables.common.Fonts
import screen.composables.common.Settings
import screen.composables.platform.VerticalScrollbar
import screen.composables.util.loadableScoped
import screen.composables.util.withoutWidthConstraints
import kotlin.text.Regex.Companion.fromLiteral

@Composable
fun EditorView(model: Editor, settings: Settings) = key(model) {
    SelectionContainer {
        Surface(
            Modifier.fillMaxSize(),
            //color = AppTheme.colors.backgroundLight,
        ) {
            val lines by loadableScoped(model.lines)

            if (lines != null) {
                Box {
                    Lines(lines!!, settings)
                }
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
                    Line(Modifier.align(Alignment.CenterStart), maxNum, lines[index], settings)
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
private fun Line(modifier: Modifier, maxNum: String, line: Editor.Line, settings: Settings) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        DisableSelection {
            Box {
                LineNumber(maxNum, Modifier.alpha(0f), settings)
                LineNumber(line.number.toString(), Modifier.align(Alignment.CenterEnd), settings)
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
private fun LineNumber(number: String, modifier: Modifier, settings: Settings) = Text(
    text = number,
    fontSize = settings.fontSize,
    fontFamily = Fonts.jetbrainsMono(),
    color = Color.DarkGray.copy(alpha = 0.70f),
    modifier = modifier.padding(start = 12.dp)
)

@Composable
private fun LineContent(content: Editor.Content, modifier: Modifier, settings: Settings) = Text(
    text = if (content.isGCode) {
        codeString(content.value.value)
    } else {
        buildAnnotatedString {
            withStyle(AppTheme.code.simple) {
                append(content.value.value)
            }
        }
    },
    fontSize = settings.fontSize,
    fontFamily = Fonts.jetbrainsMono(),
    modifier = modifier,
    softWrap = false
)

private fun codeString(str: String) = buildAnnotatedString {
    withStyle(AppTheme.code.simple) {
        val strFormatted = str.replace("\t", "    ")
        append(strFormatted)
        addStyle(AppTheme.code.punctuation, strFormatted, ":")
        addStyle(AppTheme.code.punctuation, strFormatted, "=")
        addStyle(AppTheme.code.punctuation, strFormatted, "\"")
        addStyle(AppTheme.code.punctuation, strFormatted, "[")
        addStyle(AppTheme.code.punctuation, strFormatted, "]")
        addStyle(AppTheme.code.punctuation, strFormatted, "{")
        addStyle(AppTheme.code.punctuation, strFormatted, "}")
        addStyle(AppTheme.code.keyword, strFormatted, "SUB")
        addStyle(AppTheme.code.keyword, strFormatted, "ENDSUB")
        addStyle(AppTheme.code.keyword, strFormatted, "G0")
        addStyle(AppTheme.code.keyword, strFormatted, "G1")
        addStyle(AppTheme.code.keyword, strFormatted, "G2")
        addStyle(AppTheme.code.keyword, strFormatted, "G3")
        addStyle(AppTheme.code.keyword, strFormatted, "G54")
        addStyle(AppTheme.code.keyword, strFormatted, "G18")
        addStyle(AppTheme.code.keyword, strFormatted, "G21")
        addStyle(AppTheme.code.keyword, strFormatted, "G43")
        addStyle(AppTheme.code.keyword, strFormatted, "G71.1")
        addStyle(AppTheme.code.keyword, strFormatted, "G71.2")
        addStyle(AppTheme.code.keyword, strFormatted, "G70")
        addStyle(AppTheme.code.keyword, strFormatted, "G64")
        addStyle(AppTheme.code.keyword, strFormatted, "G21")
        addStyle(AppTheme.code.keyword, strFormatted, "M")
        addStyle(AppTheme.code.keyword, strFormatted, "X")
        addStyle(AppTheme.code.keyword, strFormatted, "Y")
        addStyle(AppTheme.code.keyword, strFormatted, "Z")
        addStyle(AppTheme.code.keyword, strFormatted, "I")
        addStyle(AppTheme.code.keyword, strFormatted, "J")
        addStyle(AppTheme.code.keyword, strFormatted, "K")
        addStyle(AppTheme.code.keyword, strFormatted, "F")
        addStyle(AppTheme.code.keyword, strFormatted, "S")
        addStyle(AppTheme.code.keyword, strFormatted, "T")
        addStyle(AppTheme.code.value, strFormatted, "true")
        addStyle(AppTheme.code.value, strFormatted, "false")
        //addStyle(AppTheme.code.value, strFormatted, Regex("[0-9]*"))
        //addStyle(AppTheme.code.annotation, strFormatted, Regex("^@[a-zA-Z_]*"))
        addStyle(AppTheme.code.comment, strFormatted, Regex("^\\s*;.*"))
        addStyle(AppTheme.code.comment, strFormatted, Regex("^\\s*\\(.*"))
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