package posidon.texter

import posidon.texter.backend.NewLineFilter
import posidon.texter.backend.Settings
import posidon.texter.backend.TextFile
import posidon.texter.backend.Tools
import posidon.texter.ui.Constants
import posidon.texter.ui.FileChooser
import posidon.texter.ui.Theme
import posidon.texter.ui.Themes
import posidon.texter.ui.settings.SettingsScreen
import posidon.texter.ui.view.*
import posidon.texter.ui.view.Button
import java.awt.*
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import java.awt.event.ActionEvent
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.io.File
import javax.swing.*
import javax.swing.border.Border
import javax.swing.plaf.basic.BasicSplitPaneDivider
import javax.swing.plaf.basic.BasicSplitPaneUI
import javax.swing.text.DefaultStyledDocument
import javax.swing.text.SimpleAttributeSet
import javax.swing.undo.UndoManager

object Window {

    var currentFile: TextFile? = null
    var activeTab: FileTab? = null
    var undoManager: UndoManager? = null

    val jFrame = JFrame(AppInfo.NAME).apply {
        size = Dimension(AppInfo.INIT_WIDTH, AppInfo.INIT_HEIGHT)
        minimumSize = Dimension(AppInfo.MIN_WIDTH, AppInfo.MIN_HEIGHT)
        isResizable = true
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        transferHandler = object : TransferHandler() {
            override fun importData(info: TransferSupport?): Boolean {
                if (info != null && info.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) try {
                    val data = info.transferable.getTransferData(DataFlavor.javaFileListFlavor) as List<File>
                    for (file in data) openFile(file.path)
                    return true
                } catch (ignore: Exception) {}
                return false
            }

            override fun canImport(info: TransferSupport?): Boolean {
                return info?.isDataFlavorSupported(DataFlavor.javaFileListFlavor) ?: false
            }
        }
    }

    val textArea = JTextPane().apply {
        font = Constants.codeFont
        isEditable = true
        border = BorderFactory.createEmptyBorder(12, 12, 12, 12)
        isVisible = false
        actionMap.put("undo", object : AbstractAction("undo") {
            override fun actionPerformed(event: ActionEvent?) {
                try { if (undoManager != null) undoManager!!.undo() } catch (e: Exception) {}
            }
        })
        actionMap.put("redo", object : AbstractAction("redo") {
            override fun actionPerformed(event: ActionEvent?) {
                try { if (undoManager != null) undoManager!!.redo() } catch (e: Exception) {}
            }
        })
        actionMap.put("cut", object : AbstractAction("cut") {
            override fun actionPerformed(event: ActionEvent?) {
                Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(selectedText), null)
                replaceSelection("")
            }
        })
        actionMap.put("copy", object : AbstractAction("copy") {
            override fun actionPerformed(event: ActionEvent?) {
                Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(selectedText), null)
            }
        })
        actionMap.put("paste", object : AbstractAction("paste") {
            override fun actionPerformed(event: ActionEvent?) {
                replaceSelection(Tools.getClipboardContents(Toolkit.getDefaultToolkit().systemClipboard))
                val tmp = undoManager
                undoManager = null
                currentFile!!.colorAll(styledDocument)
                undoManager = tmp
            }
        })
        inputMap.put(KeyStroke.getKeyStroke("control Z"), "undo")
        inputMap.put(KeyStroke.getKeyStroke("control Y"), "redo")
        inputMap.put(KeyStroke.getKeyStroke("control shift Z"), "redo")
        inputMap.put(KeyStroke.getKeyStroke("control X"), "cut")
        inputMap.put(KeyStroke.getKeyStroke("control C"), "copy")
        inputMap.put(KeyStroke.getKeyStroke("control V"), "paste")
        transferHandler = jFrame.transferHandler
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

    private val scroll = JScrollPane(JPanel(BorderLayout()).apply { add(textArea); isOpaque = false }).apply {
        border = null
        isOpaque = false
        viewport.isOpaque = false
        verticalScrollBar.unitIncrement = 10
        horizontalScrollBar.unitIncrement = 10
        verticalScrollBar.isOpaque = false
        horizontalScrollBar.isOpaque = false
        //jFrame.add(this, BorderLayout.CENTER)
    }

    private val fileTree = FileTree(File(".")).apply {
        //jFrame.add(this, BorderLayout.WEST)
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

    var theme: Theme = Theme()
        set(value) {
            field = value
            textArea.foreground = theme.textAreaFG
            textArea.caretColor = theme.textAreaCaret
            textArea.background = theme.textAreaBG
            textArea.selectionColor = theme.textSelectionBG
            textArea.selectedTextColor = null
            jFrame.background = theme.windowBG
            jFrame.contentPane.background = theme.windowBG
            scroll.verticalScrollBar.setUI(MinimalScrollBarUI())
            scroll.horizontalScrollBar.setUI(MinimalScrollBarUI())
            tabs.background = theme.uiBG
            toolbar.background = theme.uiBG
            fileTree.updateTheme()
            val tmpUndoManager = undoManager
            undoManager = null
            for (tab in tabs.components)
                if (tab is FileTab) tab.updateTheme()
            undoManager = tmpUndoManager
            actionBtnFiles.icon = theme.iconTheme.action_file_menu
            actionBtnOther.icon = theme.iconTheme.action_file_menu
        }

    fun openFile(path: String) {
        val file = TextFile.open(path)
        if (file != null) {
            val thisUndoManager = UndoManager()
            val document = DefaultStyledDocument()
            val tab = FileTab(file.name, file.icon, file, document)
            document.insertString(0, file.text, SimpleAttributeSet())
            file.colorAll(document)
            document.documentFilter = NewLineFilter()
            document.addUndoableEditListener { if (undoManager != null) {
                currentFile!!.text = document.getText(0, document.length)
                currentFile!!.save()
                undoManager!!.addEdit(it.edit)
                val tmp = undoManager
                undoManager = null
                currentFile!!.colorLine(document, textArea.caretPosition)
                undoManager = tmp
            }}

            tab.addMouseListener(object : MouseListener {
                override fun mouseReleased(p0: MouseEvent?) {}
                override fun mouseEntered(p0: MouseEvent?) {}
                override fun mouseExited(p0: MouseEvent?) {}
                override fun mousePressed(p0: MouseEvent?) {}
                override fun mouseClicked(p0: MouseEvent?) {
                    if (activeTab == null) textArea.isVisible = true
                    else activeTab!!.active = false
                    tab.active = true
                    activeTab = tab
                    currentFile = file
                    undoManager = null
                    textArea.styledDocument = document
                    undoManager = thisUndoManager
                    title = AppInfo.NAME + " - " + file.name
                }
            })

            tabs.add(tab)
            jFrame.validate()
        }
    }

    lateinit var actionBtnFiles: Button
    lateinit var actionBtnOther: Button
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
                                chooser.result?.let {
                                    fileTree.setFolder(it)
                                    fileTree.isVisible = true
                                    splitPane.dividerLocation = splitPane.minimumDividerLocation
                                }
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
                                chooser.result?.let {
                                    TextFile.new(it)
                                    openFile(it)
                                }
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

        Button(icon = theme.iconTheme.action_file_menu).apply {
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
        updateTheme(Settings.getString(Settings.THEME))
    }

    fun updateTheme(name: String?) {
        println(name)
        theme = when (name) {
            "elementary" -> Themes.elementary
            "midnight" -> Themes.midnight
            "material" -> Themes.material
            "ubuntu" -> Themes.ubuntu
            "dark" -> Themes.dark
            else -> Themes.dark
        }
    }

    var title: String
        get() = jFrame.title
        set(value) { jFrame.title = value }
}