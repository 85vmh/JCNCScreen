package usecase.model

import java.util.*

data class FileSystemItem(
    val name: String,
    val path: String,
    val children: List<FileSystemItem> = emptyList(),
    val isDirectory: Boolean,
    val lastModified: Date,
    val size: Long? = null,
    val clicked: () -> Unit
){
}