package posidon.texter.backend

import posidon.texter.backend.syntaxHighlighters.DefaultSyntaxHighlighter
import posidon.texter.backend.syntaxHighlighters.SyntaxHighlighter
import posidon.texter.backend.syntaxHighlighters.KotlinSyntaxHighlighter
import java.io.*
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import javax.swing.text.StyledDocument
import kotlin.streams.toList

class TextFile(val path: String, var content: List<String>) {

    private var syntaxHighlighter: SyntaxHighlighter =
        if (path.endsWith(".kt")) KotlinSyntaxHighlighter()
        else DefaultSyntaxHighlighter()

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