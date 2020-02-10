package posidon.texter

import posidon.texter.backend.TextFilter
import posidon.texter.backend.Settings
import posidon.texter.backend.TextFile
import posidon.texter.backend.Tools
import posidon.texter.ui.*
import posidon.texter.ui.settings.SettingsScreen
import posidon.texter.ui.view.*
import posidon.texter.ui.view.Button
import java.awt.*
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.io.File
import javax.swing.*
import javax.swing.border.Border
import javax.swing.border.MatteBorder
import javax.swing.plaf.basic.BasicSplitPaneDivider
import javax.swing.plaf.basic.BasicSplitPaneUI
import javax.swing.text.*


object Window {

    var activeTab: FileTab? = null

    var title: String
        get() = jFrame.title
        set(value) { jFrame.title = value }

    var folder: String? = null
        set(value) {
            field = value
            if (value == null) {
                fileTree.isVisible = false
                splitPane.dividerLocation = splitPane.minimumDividerLocation
            } else {
                fileTree.setFolder(value)
                fileTree.isVisible = true
                splitPane.dividerLocation = splitPane.minimumDividerLocation
            }
        }

    val jFrame = JFrame(AppInfo.NAME).apply {
        size = Dimension(AppInfo.INIT_WIDTH, AppInfo.INIT_HEIGHT)
        minimumSize = Dimension(AppInfo.MIN_WIDTH, AppInfo.MIN_HEIGHT)
        isResizable = true
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        transferHandler = object : TransferHandler() {
            override fun importData(info: TransferSupport?): Boolean {
                if (info != null && info.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) try {
                    val data = info.transferable.getTransferData(DataFlavor.javaFileListFlavor) as List<File>
                    for (file in data) {
                        if (file.isDirectory) folder = file.path
                        else openFile(file.path)
                    }
                    return true
                } catch (ignore: Exception) {}
                return false
            }

            override fun canImport(info: TransferSupport?): Boolean {
                return info?.isDataFlavorSupported(DataFlavor.javaFileListFlavor) ?: false
            }
        }
        iconImages = ArrayList<Image>().apply {
            add(ImageIcon(Window.javaClass.getResource("/icons/appIcon/128.png")).image)
            add(ImageIcon(Window.javaClass.getResource("/icons/appIcon/64.png")).image)
            add(ImageIcon(Window.javaClass.getResource("/icons/appIcon/32.png")).image)
        }
    }

    val textArea = JTextPane().apply {
        font = Constants.codeFont
        isEditable = true
        border = BorderFactory.createEmptyBorder(12, 12, 12, 12)
        isVisible = false
        isOpaque = false
        actionMap.put("undo", object : AbstractAction("undo") {
            override fun actionPerformed(event: ActionEvent?) {
                activeTab?.let {
                    try { it.undoManager.undo() } catch (e: Exception) {}
                    it.file.text = document.getText(0, document.length)
                    it.file.save()
                }
            }
        })
        actionMap.put("redo", object : AbstractAction("redo") {
            override fun actionPerformed(event: ActionEvent?) {
                activeTab?.let {
                    try { it.undoManager.redo() } catch (e: Exception) {}
                    it.file.text = document.getText(0, document.length)
                    it.file.save()
                }
            }
        })
        actionMap.put("cut", object : AbstractAction("cut") {
            override fun actionPerformed(event: ActionEvent?) {
                Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(selectedText), null)
                replaceSelection("")
                val tmp = activeTab
                activeTab = null
                tmp?.file?.colorAll(styledDocument)
                activeTab = tmp
                activeTab?.file?.save()
            }
        })
        actionMap.put("copy", object : AbstractAction("copy") {
            override fun actionPerformed(event: ActionEvent?) {
                Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(selectedText), null)
            }
        })
        actionMap.put("paste", object : AbstractAction("paste") {
            override fun actionPerformed(event: ActionEvent?) {
                val content = Tools.getClipboardContents(Toolkit.getDefaultToolkit().systemClipboard)
                replaceSelection(content)
                val tmp = activeTab
                activeTab = null
                tmp?.file?.colorAll(styledDocument)
                activeTab = tmp
                activeTab?.file?.save()
            }
        })
        inputMap.put(KeyStroke.getKeyStroke("control Z"), "undo")
        inputMap.put(KeyStroke.getKeyStroke("control Y"), "redo")
        inputMap.put(KeyStroke.getKeyStroke("control shift Z"), "redo")
        inputMap.put(KeyStroke.getKeyStroke("control X"), "cut")
        inputMap.put(KeyStroke.getKeyStroke("control C"), "copy")
        inputMap.put(KeyStroke.getKeyStroke("control V"), "paste")
        transferHandler = jFrame.transferHandler
        addKeyListener(object : KeyListener {

            var shiftPressed = false

            override fun keyTyped(e: KeyEvent) {
                if (e.keyChar == '\t' && shiftPressed) activeTab?.unindentText(selectionStart, selectionEnd - selectionStart)
            }

            override fun keyPressed(e: KeyEvent) {
                if (e.keyCode == 16) shiftPressed = true
            }

            override fun keyReleased(e: KeyEvent) {
                if (e.keyCode == 16) shiftPressed = false
            }
        })
    }

    private val toolbar = JToolBar(JToolBar.VERTICAL).apply {
        border = BorderFactory.createEmptyBorder(0, 0, 0, 0)
        margin = Insets(0, 0, 0, 0)
        isFloatable = true
        jFrame.add(this, BorderLayout.EAST)
    }

    val tabs = JPanel().apply {
        border = BorderFactory.createEmptyBorder(0, 0, 0, 0)
        layout = BoxLayout(this, BoxLayout.X_AXIS)
    }

    private val tabsScroller = HorizontalScrollPane(tabs).apply {
        border = BorderFactory.createEmptyBorder(0, 0, 0, 0)
        horizontalScrollBarPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER
        jFrame.add(this, BorderLayout.NORTH)
    }

    val scroll = JScrollPane(JPanel().apply {
        isOpaque = false
        layout = BorderLayout()
        add(textArea, BorderLayout.CENTER)
    }).apply {
        border = null
        viewport.isOpaque = false
        verticalScrollBar.unitIncrement = 10
        horizontalScrollBar.unitIncrement = 10
        isWheelScrollingEnabled = true
        verticalScrollBar.isOpaque = false
        horizontalScrollBar.isOpaque = false
    }

    val textNumbers = TextLineNumber(textArea, scroll).apply { isVisible = false }

    private val fileTree = FileTree(null).apply {
        setLeafDoubleClickListener { openFile(it) }
        isVisible = false
    }

    private val splitPane = JSplitPane(JSplitPane.HORIZONTAL_SPLIT).apply {
        leftComponent = fileTree
        rightComponent = scroll
        isOpaque = false
        border = BorderFactory.createEmptyBorder(0, 0, 0, 0)
        dividerSize = 4
        isContinuousLayout = true
        setUI(object : BasicSplitPaneUI() {
            override fun createDefaultDivider(): BasicSplitPaneDivider {
                return object : BasicSplitPaneDivider(this) {
                    override fun setBorder(b: Border) {}
                    override fun paint(g: Graphics) {
                        g.color = theme.uiBG
                        g.fillRect(0, 0, size.width, size.height)
                        super.paint(g)
                    }
                }
            }
        })
        jFrame.add(this, BorderLayout.CENTER)
    }

    var theme: Theme = Themes.dark
        set(value) {
            field = value
            textArea.foreground = theme.textAreaFG
            textArea.caretColor = theme.textAreaCaret
            textArea.selectionColor = theme.textSelectionBG
            textArea.selectedTextColor = null
            scroll.verticalScrollBar.setUI(ScrollBarUI())
            scroll.horizontalScrollBar.setUI(ScrollBarUI())
            scroll.background = theme.textAreaBG
            jFrame.background = theme.windowBG
            jFrame.contentPane.background = theme.windowBG
            tabs.background = theme.uiBG
            toolbar.background = theme.uiBG
            fileTree.updateTheme()
            val tmpTab = activeTab
            activeTab = null
            for (tab in tabs.components)
                if (tab is FileTab) tab.updateTheme()
            activeTab = tmpTab
            actionBtnFiles.icon = theme.iconTheme.action_file_menu
            actionBtnOther.icon = theme.iconTheme.action_other_menu
            textNumbers.background = theme.textAreaNumberBG
            textNumbers.foreground = theme.textAreaNumberFG
            textNumbers.sideBorder = MatteBorder(0, 0, 0, 1, theme.uiHighlight)
            textNumbers.currentLineForeground = theme.textAreaNumberCaretLineFG
        }

    fun openFile(path: String) {
        for (tab in tabs.components) if (tab is FileTab && tab.file.path == path) {
            tab.active = true
            return
        }
        val file = TextFile.open(path)
        if (file != null) {
            val document = DefaultStyledDocument()
            val tab = FileTab(file.name, file.icon, file, document)
            document.insertString(0, file.text, SimpleAttributeSet())
            file.colorAll(document)
            document.documentFilter = TextFilter()
            document.addUndoableEditListener { edit ->
                activeTab?.let {
                    it.file.let { file ->
                        file.text = document.getText(0, document.length)
                        file.save()
                        Tools.doWithoutUndo {
                            file.colorLine(document, textArea.caretPosition)
                        }
                    }
                    it.undoManager.addEdit(edit.edit)
                }
            }
            tabs.add(tab)
            tab.active = true
            jFrame.validate()
        }
    }

    private lateinit var actionBtnFiles: Button
    private lateinit var actionBtnOther: Button
    fun init() {
        Button(icon = theme.iconTheme.action_file_menu).apply {
            border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
            isBorderPainted = false
            isOpaque = false
            isContentAreaFilled = false
            margin = Insets(0, 0, 0, 0)
            addActionListener {
                JPopupMenu().apply {
                    border = BorderFactory.createEmptyBorder(0, 0, 0, 0)
                    isBorderPainted = false
                    background = theme.uiHighlight
                    add(JMenuItem().apply {
                        action = object : AbstractAction() {
                            override fun actionPerformed(a: ActionEvent?) {
                                val chooser = FileChooser(
                                    jFrame,
                                    FileChooser.Mode.PICK_FILE
                                ).apply { get() }
                                chooser.result?.let { openFile(it) }
                            }
                        }
                        text = "open"
                        border = BorderFactory.createEmptyBorder(8, 12, 8, 12)
                        isBorderPainted = false
                        isOpaque = false
                        foreground = theme.text
                    })
                    add(JMenuItem().apply {
                        action = object : AbstractAction() {
                            override fun actionPerformed(a: ActionEvent?) {
                                val chooser = FileChooser(
                                    jFrame,
                                    FileChooser.Mode.PICK_FOLDER
                                ).apply { get() }
                                chooser.result?.let { folder = it }
                            }
                        }
                        text = "open folder"
                        border = BorderFactory.createEmptyBorder(8, 12, 8, 12)
                        isBorderPainted = false
                        isOpaque = false
                        foreground = theme.text
                    })
                    add(JMenuItem().apply {
                        action = object : AbstractAction() {
                            override fun actionPerformed(a: ActionEvent?) {
                                val chooser = FileChooser(
                                    jFrame,
                                    FileChooser.Mode.CREATE_FILE
                                ).apply { get() }
                                chooser.result?.let { openFile(it) }
                            }
                        }
                        text = "new"
                        border = BorderFactory.createEmptyBorder(8, 12, 8, 12)
                        isBorderPainted = false
                        isOpaque = false
                        foreground = theme.text
                    })
                }.show(this, 0, this.height)
            }
            toolbar.add(this)
            actionBtnFiles = this
        }

        Button(icon = theme.iconTheme.action_other_menu).apply {
            border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
            isBorderPainted = false
            isOpaque = false
            isContentAreaFilled = false
            margin = Insets(0, 0, 0, 0)
            foreground = theme.text
            addActionListener {
                JPopupMenu().apply {
                    border = BorderFactory.createEmptyBorder(0, 0, 0, 0)
                    isBorderPainted = false
                    background = theme.uiHighlight
                    add(JMenuItem().apply {
                        action = object : AbstractAction() {
                            override fun actionPerformed(a: ActionEvent?) {
                                SettingsScreen(jFrame)
                            }
                        }
                        text = "Settings"
                        border = BorderFactory.createEmptyBorder(8, 12, 8, 12)
                        isBorderPainted = false
                        isOpaque = false
                        foreground = theme.text
                    })
                }.show(this, 0, this.height)
            }
            toolbar.add(this)
            actionBtnOther = this
        }

        jFrame.isLocationByPlatform = true
        jFrame.isVisible = true
        updateTheme(Settings[Settings.THEME])
    }

    fun updateTheme(name: String?) {
        theme = when (name) {
            "elementary" -> Themes.elementary
            "midnight" -> Themes.midnight
            "material" -> Themes.material
            "ubuntu" -> Themes.ubuntu
            "dark" -> Themes.dark
            else -> Themes.dark
        }
    }

    fun bringToFront() {
        jFrame.isAlwaysOnTop = true
        jFrame.isAlwaysOnTop = false
        jFrame.requestFocus()
    }
}