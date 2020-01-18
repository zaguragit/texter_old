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
            val highlighter = Highlighter(it)
            return when(highlighter.syntax) {
                Highlighter.Syntax.BRACKETED -> BracketedSyntaxHighlighter(highlighter)
                Highlighter.Syntax.INDENTED -> BracketedSyntaxHighlighter(highlighter)
                Highlighter.Syntax.TAGGED -> TagSyntaxHighlighter(highlighter)
                else -> DefaultSyntaxHighlighter()
            }
        }
        return DefaultSyntaxHighlighter()
    }
}