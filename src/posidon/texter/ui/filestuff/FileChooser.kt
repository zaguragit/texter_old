package posidon.texter.ui.filestuff

import posidon.texter.Window
import posidon.texter.ui.Button
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Insets
import java.io.File
import javax.swing.*
import javax.swing.tree.DefaultMutableTreeNode

class FileChooser(private val jFrame: JFrame, private val mode: Mode) {

    private lateinit var dialog: JDialog
    private lateinit var selectBtn: Button
    var result: String? = null

    fun get() {
        dialog = JDialog(jFrame, when(mode) {
            Mode.PICK_FOLDER -> "Select a folder"
            Mode.PICK_FILE -> "Select a file"
            Mode.CREATE_FILE -> "Create a file"
        }, true).also {d ->
            d.size = Dimension(FileChooserConfig.MIN_WIDTH, FileChooserConfig.MIN_WIDTH)
            d.minimumSize = Dimension(FileChooserConfig.MIN_WIDTH, FileChooserConfig.MIN_HEIGHT)
            d.isResizable = true
            d.isLocationByPlatform = true
            d.add(JPanel().apply {
                layout = BorderLayout()
                add(FileTree(File("/")).apply {
                    updateTheme()
                    background = Window.theme.windowBG
                    addSelectionListener {
                        when (mode) {
                            Mode.PICK_FOLDER -> {
                                if ((it.path.lastPathComponent as DefaultMutableTreeNode).allowsChildren) {
                                    selectBtn.isEnabled = true
                                    result = it.path.path.joinToString(File.separator)
                                } else selectBtn.isEnabled = false
                            }
                            Mode.PICK_FILE -> {
                                if ((it.path.lastPathComponent as DefaultMutableTreeNode).allowsChildren) selectBtn.isEnabled = false
                                else {
                                    selectBtn.isEnabled = true
                                    result = it.path.path.joinToString(File.separator)
                                }
                            }
                            Mode.CREATE_FILE -> {}
                        }
                    }
                    setLeafDoubleClickListener {
                        if (mode == Mode.PICK_FILE) {
                            result = it
                            d.dispose()
                        }
                    }
                }, BorderLayout.CENTER)
                add(JToolBar().apply {
                    border = BorderFactory.createEmptyBorder(0, 0, 0, 0)
                    margin = Insets(0, 0, 0, 0)
                    isFloatable = true
                    background = Window.theme.uiBG
                    add(Button("Select").apply {
                        selectBtn = this
                        isEnabled = false
                        addActionListener {
                            d.dispose()
                        }
                    })
                }, BorderLayout.SOUTH)
            })
            d.isAlwaysOnTop = true
            d.isVisible = true
        }
    }

    enum class Mode {
        PICK_FOLDER,
        PICK_FILE,
        CREATE_FILE
    }

    private object FileChooserConfig {
        const val MIN_WIDTH = 360
        const val MIN_HEIGHT = 390
    }
}