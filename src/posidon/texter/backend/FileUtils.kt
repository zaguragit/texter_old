package posidon.texter.backend

import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

object FileUtils {
    fun openFile(path: Path): TextFile? {
        try { return TextFile(path, Files.readAllLines(path)) }
        catch (ignore: IOException) {}
        return null
    }

    fun saveFile(file: TextFile): Boolean {
        return try {
            Files.write(file.path, file.content, StandardOpenOption.CREATE)
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }
}