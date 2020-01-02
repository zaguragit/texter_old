package posidon.texter.ui

import posidon.texter.AppInfo
import posidon.texter.Window
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JFrame

class SettingsWindow(mainWindow: JFrame) {
    private val jFrame = JFrame(AppInfo.NAME + " - Settings").apply {
        mainWindow.isVisible = false
        size = mainWindow.size
        minimumSize = Dimension(AppInfo.MIN_WIDTH, AppInfo.MIN_HEIGHT)
        location = mainWindow.location
        isResizable = true
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                mainWindow.size = size
                mainWindow.location = location
                mainWindow.isVisible = true
            }
        })
        layout = BorderLayout()
        contentPane.background = Window.theme.windowBG
    }

    init {
        jFrame.isVisible = true
    }
}