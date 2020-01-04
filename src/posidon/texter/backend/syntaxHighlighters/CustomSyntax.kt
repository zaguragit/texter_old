package posidon.texter.backend.syntaxHighlighters

object CustomSyntax {
    fun getHighlighter(path: String): SyntaxHighlighter {
        val text = CustomSyntax::class.java.getResource(path).readText().split('\n')
        var syntax = SYNTAX_DEFAULT
        for (line in text) {
            if (line.startsWith('@')) {
                val tokens = line.split(' ')
                if (tokens[0] == "@syntax")
                    syntax = when(tokens[1]) {
                        "bracket", "bracketed" -> SYNTAX_BRACKETED
                        "indent", "indented" -> SYNTAX_INDENTED
                        "tag", "tagged" -> SYNTAX_TAGGED
                        else -> SYNTAX_DEFAULT
                    }
            }
        }
        return when(syntax) {
            SYNTAX_BRACKETED -> BracketedSyntaxHighlighter(path)
            SYNTAX_INDENTED -> BracketedSyntaxHighlighter(path)
            SYNTAX_TAGGED -> XmlSyntaxHighlighter()
            else -> BracketedSyntaxHighlighter(path)
        }
    }

    private const val SYNTAX_BRACKETED = 0
    private const val SYNTAX_INDENTED = 1
    private const val SYNTAX_TAGGED = 2
    private const val SYNTAX_DEFAULT = SYNTAX_BRACKETED

    val HIGHLIGHTER_FLAGS = arrayListOf(
        "@syntax",
        "@line-comment",
        "@multiline-comment",
        "@hex-prefix",
        "@binary-prefix",
        "@string-sides"
    )
}