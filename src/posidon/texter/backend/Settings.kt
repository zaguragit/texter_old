package posidon.texter.backend

import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.StandardOpenOption

object Settings {

    const val THEME = "theme"
    const val ICON_THEME = "icon_theme"
    const val LAST_OPENED_FOLDER = "last_opened_folder"
    const val HIGHLIGHTER_FOLDER = "highlighter_folder"

    enum class Type(val string: String) {
        TEXT("text"),
        INT("int"),
        FLOAT("float"),
        BOOL("bool"),
        LIST("list"),
    }

    private val ints: HashMap<String, String> = HashMap()
    private val floats: HashMap<String, String> = HashMap()
    private val booleans: HashMap<String, String> = HashMap()
    private val strings: HashMap<String, String> = HashMap()
    private val lists: HashMap<String, String> = HashMap()


    operator fun set(key: String, value: Int) {
        checkKey(key)
        ints[key] = value.toString()
    }

    operator fun set(key: String, value: Float) {
        checkKey(key)
        floats[key] = value.toString()
    }

    operator fun set(key: String, value: Boolean) {
        checkKey(key)
        booleans[key] = if (value) "1" else "0"
    }

    operator fun set(key: String, value: String) {
        checkKey(key)
        strings[key] = value
    }

    operator fun set(key: String, value: Array<Int>) {
        checkKey(key)
        val stringBuilder = StringBuilder(Type.INT.string)
        for (i in value) stringBuilder.append(' ').append(i)
        lists[key] = stringBuilder.toString()
    }

    operator fun set(key: String, value: Array<Float>) {
        checkKey(key)
        val stringBuilder = StringBuilder(Type.FLOAT.string)
        for (i in value) stringBuilder.append(' ').append(i)
        lists[key] = stringBuilder.toString()
    }

    operator fun set(key: String, value: Array<Boolean>) {
        checkKey(key)
        val stringBuilder = StringBuilder(Type.BOOL.string)
        for (i in value) stringBuilder.append(' ').append(if (i) '1' else '0')
        lists[key] = stringBuilder.toString()
    }

    fun apply() {
        val file = File(Tools.getDataDir() + File.separator + "settings")
        Files.write(file.toPath(), generateText().lines(), StandardOpenOption.CREATE)
    }

    private fun generateText(): String {
        val stringBuilder = StringBuilder()
        for (string in ints) stringBuilder
            .append(Type.INT.string)
            .append(' ')
            .append(string.key)
            .append(' ')
            .append(string.value)
            .append('\n')
        for (string in floats) stringBuilder
            .append(Type.FLOAT.string)
            .append(' ')
            .append(string.key)
            .append(' ')
            .append(string.value)
            .append('\n')
        for (string in booleans) stringBuilder
            .append(Type.BOOL.string)
            .append(' ')
            .append(string.key)
            .append(' ')
            .append(string.value)
            .append('\n')
        for (string in strings) {
            if (string.value.contains('\n'))
                throw RuntimeException("Saved strings must not contain new-line characters (they're not supported as of now)")
            stringBuilder
                .append(Type.TEXT.string)
                .append(' ')
                .append(string.key)
                .append(' ')
                .append(string.value)
                .append('\n')
        }
        for (string in lists) stringBuilder
            .append(Type.LIST.string)
            .append(' ')
            .append(string.key)
            .append(' ')
            .append(string.value)
            .append('\n')
        return stringBuilder.toString()
    }

    private fun parseText(text: String) {
        val lines = text.split('\n')
        for (line in lines) {
            val tokens = line.split(' ')
            when(tokens[0]) {
                Type.INT.string -> ints[tokens[1]] = tokens[2]
                Type.FLOAT.string -> floats[tokens[1]] = tokens[2]
                Type.BOOL.string -> booleans[tokens[1]] = tokens[2]
                Type.TEXT.string -> strings[tokens[1]] = line.substring(tokens[0].length + tokens[1].length + 2)
                Type.LIST.string -> lists[tokens[1]] = line.substring(tokens[0].length + tokens[1].length + 2)
            }
        }
    }

    operator fun get(key: String, default: Int): Int {
        checkKey(key)
        return if (ints[key] != null) ints[key]!!.toInt() else default
    }

    operator fun get(key: String, default: Float): Float {
        checkKey(key)
        return if (floats[key] != null) floats[key]!!.toFloat() else default
    }

    operator fun get(key: String, default: Boolean): Boolean {
        checkKey(key)
        return if (booleans[key] != null) booleans[key] != "0" else default
    }

    operator fun get(key: String, default: String): String = get(key) ?: default

    operator fun get(key: String): String? {
        checkKey(key)
        return strings[key]
    }

    fun getInts(key: String, default: Array<Int>): Array<Int> {
        checkKey(key)
        if (lists[key] == null) return default
        val stringList = lists[key]!!.split(' ')
        if (stringList[0] != Type.INT.string) return default
        return Array(stringList.size - 1) { stringList[it].toInt() }
    }

    fun getFloats(key: String, default: Array<Float>): Array<Float> {
        checkKey(key)
        if (lists[key] == null) return default
        val stringList = lists[key]!!.split(' ')
        if (stringList[0] != Type.FLOAT.string) return default
        return Array(stringList.size - 1) { stringList[it].toFloat() }
    }

    fun getBools(key: String, default: Array<Boolean>): Array<Boolean> {
        checkKey(key)
        if (lists[key] == null) return default
        val stringList = lists[key]!!.split(' ')
        if (stringList[0] != Type.BOOL.string) return default
        return Array(stringList.size - 1) { stringList[it] != "0" }
    }


    private fun checkKey(key: String) {
        when {
            key.contains(' ') -> throw IllegalArgumentException(
                    "The ULB key can't have spaces! You should use '_' instead")
            key.contains('\n') -> throw IllegalArgumentException(
                    "The ULB key can't have \\n characters! " +
                    "It will cause syntax errors when reading the data")
        }
    }

    fun init() {
        val dir = Tools.getDataDir()
        val file = File(dir + File.separator + "settings")
        if (!file.exists()) {
            File(dir).mkdirs()
            when (Tools.distro) {
                "elementary" -> set(THEME, "elementary")
            }
            try {
                Files.write(file.toPath(), generateText().lines(), StandardOpenOption.CREATE)
            } catch (e: IOException) {
                println("Couldn't create settings file")
                e.printStackTrace()
            }
        } else parseText(file.readText())
    }
}


/*
int universal:dock_color 0235634
float universal:dock_corner_radius_all 43.3f
bool posidon:enable_blur 1
text posidon:search_bar_hint Search...
text posidon.anim:app_open_animation none
text randomlauncher:some_random_long_text blabla bla bla blabla bla blablabla
list bla:bla_bla_bla int 1 3 4 75 456 74784568 568456 345 3455 5 3 74 574 4
list bla:bla_bla float 35.5f 44.7f 3.6f
list bla:bla bool 0 1 0 0 0 1 1 0 0 0 0 0 1 1 0 1 0 1 0 0 1 1 1 0 0
*/

/*
syntax: <data type> <key> <value>
(only the value can have spaces, otherwise the data is impossible to interpret)


data types = int, float, bool, text, list
list is a space type because its value can have spaces
text is too and it's kinda like an list of words, but without declaring the list type at the beginning of the value

booleans have 1/0 instead of true/false (0 = false, everything else = true)

this syntax should use less data than SharedPreferences which to my knowledge is just xml

bool and text instead of boolean and string because it's shorter and uses less storage, and it's also more readable
in a more efficient version, [int, float, bool, text] could be replaced by shorter names like: [I, F, B, S] (S for string or T for text)
*/