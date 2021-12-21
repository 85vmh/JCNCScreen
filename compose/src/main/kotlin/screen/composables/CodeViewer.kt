package screen.composables

import screen.composables.common.Settings
import screen.composables.editor.Editors
import screen.composables.filetree.FileTree


class CodeViewer(
    val editors: Editors,
    val fileTree: FileTree,
    val settings: Settings
)