package posidon.texter.backend.syntaxHighlighters

object CustomSyntax {
    fun getHighlighter(fileName: String): SyntaxHighlighter {
        val pathToHighlighter = "/code/highlighters/${fileName.split(".").last()}.highlighter"
        val text = CustomSyntax::class.java.getResource(pathToHighlighter)?.readText()?.split('\n') ?: return DefaultSyntaxHighlighter()
        var syntax = Syntax.DEFAULT
        for (line in text) {
            if (line.startsWith('@')) {
                val tokens = line.split(' ')
                if (tokens[0] == "@syntax")
                    syntax = when(tokens[1]) {
                        "bracket", "bracketed" -> Syntax.BRACKETED
                        "indent", "indented" -> Syntax.INDENTED
                        "tag", "tagged" -> Syntax.TAGGED
                        else -> Syntax.DEFAULT
                    }
            }
        }
        return when(syntax) {
            Syntax.BRACKETED -> BracketedSyntaxHighlighter(pathToHighlighter)
            Syntax.INDENTED -> BracketedSyntaxHighlighter(pathToHighlighter)
            Syntax.TAGGED -> XmlSyntaxHighlighter()
            else -> BracketedSyntaxHighlighter(pathToHighlighter)
        }
    }

    enum class Syntax {
        BRACKETED,
        INDENTED,
        TAGGED,
        DEFAULT
    }

    val HIGHLIGHTER_FLAGS = arrayListOf(
        "@syntax",
        "@line-comment",
        "@selective-comment",
        "@hex-prefix",
        "@binary-prefix",
        "@string-sides"
    )
}