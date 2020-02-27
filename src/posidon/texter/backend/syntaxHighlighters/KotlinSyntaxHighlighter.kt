package posidon.texter.backend.syntaxHighlighters

import javax.swing.text.StyledDocument
import kotlin.concurrent.thread

class KotlinSyntaxHighlighter(private val highlighter: Highlighter) : SyntaxHighlighter() {

    private var inComment = false
    private val tokens = ArrayList<String>()

    fun colorAll(doc: StyledDocument) = thread {
        var str = StringBuilder()
        var textI = 0
        for (i in 0..doc.getText(0, doc.length).lastIndex) {
            when (val char = doc.getText(0, doc.length)[i]) {
                in " \n\t" -> {
                    str.toString().let {
                        tokens.add(it)
                        doTokenStuff(doc, it, tokens.lastIndex, i)
                    }
                    str.clear()
                }
                in ",.-+*/{}()[]%" -> tokens.apply {
                    str.toString().let {
                        tokens.add(it)
                        doTokenStuff(doc, it, tokens.lastIndex, i)
                    }
                    char.toString().let {
                        tokens.add(it)
                        doTokenStuff(doc, it, tokens.lastIndex, i)
                    }
                    str.clear()
                }
                else -> str.append(char)
            }
        }
    }.start()

    private inline fun doTokenStuff(doc: StyledDocument, token: String, tokenI: Int, i: Int) {
        when (token) {
            "/" -> when{
                tokens[tokenI - 1] == "*" -> inComment = false
                tokens[tokenI - 1] == "/" -> inComment = true
            }
            "*" -> if (tokens[tokenI - 1] == "/") inComment = true
        }
    }

    override fun colorLine(doc: StyledDocument, lineStart: Int, line: String, lineI: Int) = colorAll(doc)
}