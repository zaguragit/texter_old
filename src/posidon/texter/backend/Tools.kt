package posidon.texter.backend

import posidon.texter.Window
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.UnsupportedFlavorException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


object Tools {
    fun getClipboardContents(clipboard: Clipboard?): String? {
        var result = ""
        if (clipboard != null) { //odd: the Object param of getContents is not currently used
            val contents = clipboard.getContents(null)
            val hasTransferableText = contents != null &&
                    contents.isDataFlavorSupported(DataFlavor.stringFlavor)
            if (hasTransferableText) {
                try {
                    result = contents!!.getTransferData(DataFlavor.stringFlavor) as String
                } catch (ex: UnsupportedFlavorException) { //highly unlikely since we are using a standard DataFlavor
                    println(ex)
                    ex.printStackTrace()
                } catch (ex: IOException) {
                    println(ex)
                    ex.printStackTrace()
                }
            }
        }
        return result
    }

    fun getDataDir(): String {
        val os = System.getProperty("os.name").toUpperCase()
        println(os)
        return when {
            os.contains("WIN") -> System.getenv("APPDATA")
            os.contains("MAC") -> (System.getProperty("user.home")
                    + "/Library/Application "
                    + "Support")
            os.contains("NUX") -> System.getProperty("user.home") + "/.config/texter"
            else -> System.getProperty("user.dir")
        }
    }

    val distro: String
        get() {
            val cmd = arrayOf("/bin/sh", "-c", "cat /etc/*-release")
            try {
                val p = Runtime.getRuntime().exec(cmd)
                val bri = BufferedReader(InputStreamReader(p.inputStream))
                var line: String? = ""
                while (bri.readLine().also { line = it } != null) {
                    if (line?.startsWith("distrib_id") == true)
                        return line?.substring(10) ?: ""
                }
            } catch (e: IOException) { e.printStackTrace() }
            return ""
        }

    inline fun doWithoutUndo(methods: () -> Unit) {
        val tmp = Window.activeTab
        Window.activeTab = null
        methods()
        Window.activeTab = tmp
    }
}