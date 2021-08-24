// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.Window
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

fun main() = Window() {
    var text by remember { mutableStateOf("Hello, World!") }

    MaterialTheme {
        displaySomething()
    }
}

@Composable
@Preview
fun displaySomething() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth().padding(8.dp)
    ) {
        Button(onClick = {}) {
            Text("Spindle")
        }
        Button(onClick = {}) {
            Text("Feed Rate")
        }
        Button(onClick = {}) {
            Text("Tools")
        }
        Button(onClick = {}) {
            Text("Position")
        }
    }
}