package usecase.model

sealed class FileSystemItem(
    open val name: String,
    open val path: String,
    open val lastModified: Long,
    open val size: Long? = null
) {
    data class FileItem(
        override val name: String,
        override val path: String,
        val extension: String,
        override val lastModified: Long,
        override val size: Long? = null,
        val clicked: () -> Unit
    ) : FileSystemItem(name, path, lastModified, size)

    data class FolderItem(
        override val name: String,
        override val path: String,
        override val lastModified: Long,
        override val size: Long? = null,
        val children: List<FileSystemItem> = emptyList(),
        val clicked: () -> Unit
    ) : FileSystemItem(name, path, lastModified, size)
}