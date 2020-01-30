package posidon.texter.backend

import posidon.texter.backend.syntaxHighlighters.*
import java.io.*
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import javax.swing.text.StyledDocument
import kotlin.streams.toList

class TextFile(path: String, private var content: List<String>) : AnyFile(path) {

    private val syntaxHighlighter: SyntaxHighlighter = Highlighter[path.split(File.separator).last()]

    var text
        get() = content.joinToString("\n")
        set(value) { content = value.split("\n") }

    fun colorLine(doc: StyledDocument, caretI: Int) {
        try {
            val lineI = getLineIndex(caretI)
            syntaxHighlighter.colorLine(doc, getLineStart(lineI), content[lineI], lineI)
            if (lineI != content.lastIndex) syntaxHighlighter.colorLine(
                doc,
                getLineStart(lineI + 1),
                content[lineI + 1],
                lineI + 1
            )
        } catch (e: Exception) { e.printStackTrace() }
    }

    fun colorAll(doc: StyledDocument) {
        try {
            for (lineI in content.indices) {
                val lineStart = getLineStart(lineI)
                doc.setCharacterAttributes(
                    lineStart,
                    content[lineI].length,
                    SyntaxHighlighter.defaultTextStyle(),
                    false
                )
                syntaxHighlighter.colorLine(doc, lineStart, content[lineI], lineI)
            }
        } catch (e: Exception) { e.printStackTrace() }
    }

    private fun getLineStartByCaret(caretI: Int): Int {
        var out = 0
        (0 until getLineIndex(caretI)).forEach { i -> out += content[i].length + 1 }
        return out
    }

    private fun getLineStart(lineI: Int): Int {
        var out = 0
        (0 until lineI).forEach { i -> out += content[i].length + 1 }
        return out
    }

    private fun getLineIndex(i: Int): Int {
        var sum = content[0].length + 1
        var j = 0
        while (i > sum) {
            j++
            sum += content[j].length + 1
        }
        return j
    }

    override fun save(): Boolean {
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
                //val content = getText("/code/templates/" + path.split('/').last().split('.').last() + ".txt")?.split('\n') ?: listOf("")
                Files.write(File(path).toPath(), listOf(""), StandardOpenOption.CREATE)
                return TextFile(path, listOf(""))
            } catch (e: IOException) {
                println("Couldn't create file")
                e.printStackTrace()
            }
            return null
        }

        private fun getText(resourcePath: String): String? {
            return TextFile::class.java.getResource(resourcePath)?.readText()
        }
    }
}