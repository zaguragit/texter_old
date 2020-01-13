package posidon.texter.backend

import posidon.texter.Window
import javax.swing.ImageIcon

abstract class AnyFile(protected val path: String) {

    val icon: ImageIcon
        get() = getIcon(path)
    val name = path.split('/').last()
    val extension = name.split('.').last()

    abstract fun save(): Boolean

    companion object {
        fun getIcon(path: String): ImageIcon =
            when {
                path.endsWith(".kt") -> Window.theme.iconTheme.kotlin
                path.endsWith(".java") -> Window.theme.iconTheme.java
                path.endsWith(".xml") ||
                path.endsWith(".iml") ||
                path.endsWith(".html") -> Window.theme.iconTheme.xml
                path.endsWith(".class") ||
                path.endsWith(".elf") ||
                path.endsWith(".exe") ||
                path.endsWith(".efi") ||
                path.endsWith(".so") ||
                path.endsWith(".o") ||
                path.endsWith(".bin") ||
                path.endsWith(".iso") ||
                path.endsWith(".sh") -> Window.theme.iconTheme.file_exec
                path.endsWith(".png") ||
                path.endsWith(".jpg") ||
                path.endsWith(".jpeg") ||
                path.endsWith(".tiff") ||
                path.endsWith(".svg") ||
                path.endsWith(".arw") ||
                path.endsWith(".dng") -> Window.theme.iconTheme.img
                path.endsWith(".txt") -> Window.theme.iconTheme.file_text
                path.endsWith(".highlighter") -> Window.theme.iconTheme.file_highlighter
                else -> Window.theme.iconTheme.file
            }
    }
}