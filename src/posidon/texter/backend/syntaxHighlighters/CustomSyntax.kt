package posidon.texter.backend.syntaxHighlighters

object CustomSyntax {
    fun getHighlighter(fileName: String): SyntaxHighlighter {
        var extension = fileName.split(".").last()
        var pathToHighlighter = "/code/highlighters/${extension}.highlighter"
        var text = CustomSyntax::class.java.getResource(pathToHighlighter)?.readText()
        if (text == null) {
            if (extension.endsWith("ml")) extension = "xml"
        }
        pathToHighlighter = "/code/highlighters/${extension}.highlighter"
        text = CustomSyntax::class.java.getResource(pathToHighlighter)?.readText()
        text?.let {
            val lines = it.split('\n')
            var syntax = Syntax.DEFAULT
            for (line in lines) {
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
                Syntax.BRACKETED -> BracketedSyntaxHighlighter(it)
                Syntax.INDENTED -> BracketedSyntaxHighlighter(it)
                Syntax.TAGGED -> TagSyntaxHighlighter(it)
                else -> DefaultSyntaxHighlighter()
            }
        }
        return DefaultSyntaxHighlighter()
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
        "@string-sides",
        "@num-suffixes"
    )
}