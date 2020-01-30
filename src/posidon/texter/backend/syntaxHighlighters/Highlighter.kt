package posidon.texter.backend.syntaxHighlighters

class Highlighter(text: String) {
    val syntax: Syntax

    val declarations = ArrayList<String>()
    val mods = ArrayList<String>()
    val funcBreaks = ArrayList<String>()
    val conditions = ArrayList<String>()
    val operators = ArrayList<String>()
    val exceptions = ArrayList<String>()
    val values = ArrayList<String>()

    var numSuffixes: List<String>? = null
    var stringSides: List<String>? = null
    var lineComment: String? = null
    var startComment: String? = null
    var endComment: String? = null
    var hexPrefix: String? = null
    var binPrefix: String? = null

    var separators = "=,.;:-+*/()[]{}&|!"

    init {
        val lines = text.split('\n')
        var tmpSyntax = Syntax.DEFAULT
        for (line in lines) {
            if (line.startsWith("//")) continue
            if (line.startsWith('@')) {
                val tokens = line.split(' ')
                when(tokens[0]) {
                    "@syntax" -> tmpSyntax = when(tokens[1]) {
                        "bracket", "bracketed" -> Syntax.BRACKETED
                        "indent", "indented" -> Syntax.INDENTED
                        "tag", "tagged" -> Syntax.TAGGED
                        else -> Syntax.DEFAULT
                    }
                    "@line-comment" -> lineComment = tokens[1]
                    "@selective-comment" -> {
                        startComment = tokens[1]
                        endComment = tokens[2]
                    }
                    "@hex-prefix" -> hexPrefix = tokens[1]
                    "@binary-prefix" -> binPrefix = tokens[1]
                    "@string-sides" -> stringSides = tokens.subList(1, tokens.size)
                    "@num-suffixes" -> numSuffixes = tokens.subList(1, tokens.size)
                    "@separators" -> separators = tokens[1]
                }
            } else {
                val tokens = line.split(' ')
                when {
                    line.startsWith("declarations") -> declarations
                    line.startsWith("mods") -> mods
                    line.startsWith("func_breaks") -> funcBreaks
                    line.startsWith("conditions") -> conditions
                    line.startsWith("operators") -> operators
                    line.startsWith("exceptions") -> exceptions
                    line.startsWith("values") -> values
                    else -> values
                }.addAll(tokens.subList(2, tokens.size))
            }
        }
        syntax = tmpSyntax
    }

    enum class Syntax {
        BRACKETED,
        INDENTED,
        TAGGED,
        DEFAULT
    }

    companion object {
        val FLAGS = arrayListOf(
            "@syntax",
            "@line-comment",
            "@selective-comment",
            "@hex-prefix",
            "@binary-prefix",
            "@string-sides",
            "@num-suffixes",
            "@separators"
        )

        operator fun get(fileName: String): SyntaxHighlighter {
            when {
                fileName.split('.').last() == "highlighter" -> HighlighterSyntaxHighlighter()
                fileName.split('.').last() == "md" -> MarkdownSyntaxHighlighter()
                fileName.equals("makefile", true) -> return MakefileSyntaxHighlighter()
            }
            var extension = fileName.split(".").last()
            var pathToHighlighter = "/code/highlighters/${extension}.highlighter"
            var text = Highlighter::class.java.getResource(pathToHighlighter)?.readText()
            if (text == null) {
                if (extension.endsWith("ml")) extension = "xml"
            }
            pathToHighlighter = "/code/highlighters/${extension}.highlighter"
            text = Highlighter::class.java.getResource(pathToHighlighter)?.readText()
            text?.let {
                val highlighter = Highlighter(it)
                return when(highlighter.syntax) {
                    Syntax.BRACKETED -> BracketedSyntaxHighlighter(highlighter)
                    Syntax.INDENTED -> BracketedSyntaxHighlighter(highlighter)
                    Syntax.TAGGED -> TagSyntaxHighlighter(highlighter)
                    else -> DefaultSyntaxHighlighter()
                }
            }
            return DefaultSyntaxHighlighter()
        }
    }
}