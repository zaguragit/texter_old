package posidon.texter.backend

import posidon.texter.Window
import posidon.texter.backend.syntaxHighlighters.*
import java.io.*
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import javax.swing.ImageIcon
import javax.swing.text.StyledDocument
import kotlin.streams.toList

class TextFile(private val path: String, private var content: List<String>) {

    private var syntaxHighlighter: SyntaxHighlighter =
        when {
            path.endsWith(".kt") -> BracketedSyntaxHighlighter("/code/highlighters/kt.highlighter")
            path.endsWith(".java") -> BracketedSyntaxHighlighter("/code/highlighters/java.highlighter")
            path.endsWith(".sh") -> BracketedSyntaxHighlighter("/code/highlighters/shellscript.highlighter")
            path.endsWith(".xml") ||
                    path.endsWith(".iml") ||
                    path.endsWith(".svg") ||
                    path.endsWith(".html") -> XmlSyntaxHighlighter()
            path.endsWith(".highlighter") -> HighlighterSyntaxHighlighter()
            else -> DefaultSyntaxHighlighter()
        }

    val icon: ImageIcon =
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

    var text
        get() = content.joinToString("\n")
        set(value) { content = value.split("\n") }

    fun colorLine(doc: StyledDocument, caretI: Int) {
        val lineI = getLineByIndex(caretI)
        syntaxHighlighter.colorLine(doc, getLineStartByCaret(caretI), content[lineI], lineI)
    }

    fun colorAll(doc: StyledDocument) {
        for (lineI in content.indices) {
            syntaxHighlighter.colorLine(doc, getLineStart(lineI), content[lineI], lineI)
        }
    }

    private fun getLineStartByCaret(caretI: Int): Int {
        var out = 0
        (0 until getLineByIndex(caretI)).forEach { i -> out += content[i].length + 1 }
        return out
    }

    private fun getLineStart(lineI: Int): Int {
        var out = 0
        (0 until lineI).forEach { i -> out += content[i].length + 1 }
        return out
    }

    private fun getLineByIndex(i: Int): Int {
        var sum = content[0].length + 1
        var j = 0
        while (i > sum) {
            j++
            sum += content[j].length + 1
        }
        return j
    }

    val name = path.split('/').last()

    fun save(): Boolean {
        return try {
            val buf = BufferedWriter(FileWriter(path))
            buf.write(text)
            buf.flush()
            buf.close()
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    companion object {
        fun open(path: String): TextFile? {
            try {
                val buf = BufferedReader(FileReader(path))
                return TextFile(path, buf.lines().toList()).also { buf.close() }
            } catch (e: IOException) {
                println("Couldn't open file")
                e.printStackTrace()
            } catch (e: Exception) { e.printStackTrace() }
            return null
        }

        fun new(path: String): TextFile? {
            try {
                val content = getText("code/templates/" + path.split('/').last().split('.').last() + ".txt").split('\n')
                Files.write(File(path).toPath(), content, StandardOpenOption.CREATE)
                return TextFile(path, content)
            } catch (e: IOException) {
                println("Couldn't create file")
                e.printStackTrace()
            }
            return null
        }

        private fun getText(resourcePath: String): String {
            return TextFile::class.java.getResource(resourcePath).readText()
        }
    }
}