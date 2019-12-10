package posidon.texter.ui

import java.awt.*
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.io.File
import javax.swing.*

class FileChooser(val jFrame: JFrame) : JFileChooser() {

    override fun getIcon(file: File?): Icon {
        return ImageIcon(FileChooser::class.java.getResource(if (file != null) when {
            file.isDirectory -> "/icons/files/folder.png"
            file.isFile -> when (file.extension) {
                "kt" -> "/icons/files/kotlin.png"
                "java" -> "/icons/files/java.png"
                else -> "/icons/files/file.png"
            }
            else -> "/icons/files/file.png"
        } else "/icons/files/file.png"))
    }

    override fun createDialog(var1: Component?): JDialog? {
        val title = getUI().getDialogTitle(this)
        putClientProperty("AccessibleDescription", title)
        /*val window = getWindowForComponent(var1)
        val dialog: JDialog =
            if (window is Frame) JDialog(window, title, true)
            else JDialog(window as Dialog, title, true)

        dialog.contentPane.apply {
            //background = Color(posidon.texter.Window.theme.uiBG)
            add(this@FileChooser)
        }*/

        isFileHidingEnabled = false

        //dialog.pack()
        //dialog.setLocationRelativeTo(jFrame)
        return null
    }

    override fun showDialog(var1: Component?, var2: String?): Int {
        if (var2 != null) {
            approveButtonText = var2
            dialogType = 2
        }
        createDialog(var1)
        rescanCurrentDirectory()
        jFrame.add(this, BorderLayout.CENTER)
        return -1
    }

    private fun getWindowForComponent(var0: Component?): Window? {
        return if (var0 == null) JOptionPane.getRootFrame()
        else if (var0 !is Frame && var0 !is Dialog) getWindowForComponent(var0.parent) else var0 as Window?
    }
}