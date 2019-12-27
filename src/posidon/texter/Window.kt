package posidon.texter

import posidon.texter.backend.NewLineFilter
import posidon.texter.backend.TextFile
import posidon.texter.backend.Tools
import posidon.texter.ui.FileTree
import posidon.texter.ui.ScrollBar
import posidon.texter.ui.Theme
import posidon.texter.ui.Themes
import java.awt.*
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import java.awt.event.ActionEvent
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.io.File
import javax.swing.*
import javax.swing.text.*
import javax.swing.undo.CannotRedoException
import javax.swing.undo.CannotUndoException
import javax.swing.undo.UndoManager
import kotlin.test.todo

object Window {

    private var currentFile: TextFile? = null
    private var activeTab: JButton? = null
    private var undoManager: UndoManager? = null
    private val codeFont = Font(Font.MONOSPACED, Font.PLAIN, 15)
    private val uiFont = Font(Font.SANS_SERIF, Font.PLAIN, 15)

    private val jFrame = JFrame(AppInfo.NAME).apply {
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

    private val textArea = JTextPane().apply {
        font = codeFont
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

    private val tabs = JToolBar(JToolBar.HORIZONTAL).apply {
        border = BorderFactory.createEmptyBorder(0, 0, 0, 0)
        margin = Insets(0, 0, 0, 0)
        isFloatable = false
        jFrame.add(this, BorderLayout.NORTH)
    }

    private val scroll = JScrollPane(JPanel(BorderLayout()).apply { add(textArea); isOpaque = false }).apply {
        border = null
        isOpaque = false
        viewport.isOpaque = false
        verticalScrollBar.unitIncrement = 10
        horizontalScrollBar.unitIncrement = 10
        jFrame.add(this, BorderLayout.CENTER)
    }

    private val fileTree = FileTree(File(".")).apply {
        jFrame.add(this, BorderLayout.WEST)
    }

    public var theme: Theme = Theme()
        set(value) {
            field = value
            textArea.foreground = theme.textAreaFG
            textArea.caretColor = theme.textAreaCaret
            textArea.background = theme.textAreaBG
            jFrame.background = theme.windowBG
            jFrame.contentPane.background = theme.windowBG
            scroll.verticalScrollBar.setUI(ScrollBar())
            scroll.horizontalScrollBar.setUI(ScrollBar())
            tabs.background = theme.uiBG
            toolbar.background = theme.uiBG
            fileTree.updateColors()
            fileTree.verticalScrollBarUI = ScrollBar()
            fileTree.horizontalScrollBarUI = ScrollBar()
        }

    public fun openFile(path: String) {
        val file = TextFile.open(path)
        if (file != null) {
            val tab = JButton(file.name)
            tab.background = theme.uiBG
            tab.margin = Insets(0, 0, 0, 0)
            tab.border = BorderFactory.createEmptyBorder(8, 10, 8, 10)
            tab.foreground = theme.text
            tab.font = uiFont
            tab.isBorderPainted = false
            tab.isFocusable = false
            tab.layout = BorderLayout()
            val thisUndoManager = UndoManager()
            val document = DefaultStyledDocument()
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


            tab.addActionListener {
                if (activeTab == null) textArea.isVisible = true
                else activeTab!!.background = theme.uiBG
                tab.background = theme.uiHighlight
                activeTab = tab
                currentFile = file
                undoManager = null
                textArea.styledDocument = document
                undoManager = thisUndoManager
                title = AppInfo.NAME + " - " + file.name
            }
            val closeTabBtn = JButton(theme.iconTheme.close_tab_hover).apply {
                isEnabled = false
                disabledIcon = theme.iconTheme.close_tab
                margin = Insets(0, 0, 0, 0)
                isOpaque = false
                isContentAreaFilled = false
                background = Color(0x0)
                isBorderPainted = false
                border = BorderFactory.createEmptyBorder(0, 0, 0, 0)
                addActionListener {
                    if (activeTab == tab) {
                        activeTab = null
                        currentFile = null
                        textArea.isVisible = false
                        undoManager = null
                    }
                    tabs.remove(tab)
                    tabs.updateUI()
                }
                addMouseListener(object : MouseListener {
                    override fun mouseReleased(p0: MouseEvent?) {}
                    override fun mouseClicked(p0: MouseEvent?) {}
                    override fun mousePressed(p0: MouseEvent?) {}
                    override fun mouseEntered(p0: MouseEvent?) { isEnabled = true }
                    override fun mouseExited(p0: MouseEvent?) { isEnabled = false }
                })
            }
            tab.add(closeTabBtn, BorderLayout.EAST)

            tabs.add(tab)
            tabs.updateUI()
        }
    }


    fun init() {
        theme = Themes.midnight
        JButton(ImageIcon(theme.iconTheme.file_menu)).apply {
            border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
            isBorderPainted = false
            isOpaque = false
            isContentAreaFilled = false
            margin = Insets(0, 0, 0, 0)
            background = Color(0x0)
            foreground = theme.text
            isFocusable = false
            val popup = JPopupMenu().apply {
                border = BorderFactory.createEmptyBorder(0, 0, 0, 0)
                isBorderPainted = false
                background = theme.uiHighlight
                add(JMenuItem().apply {
                    action = object : AbstractAction() {
                        override fun actionPerformed(a: ActionEvent?) {
                            /*val chooser = FileChooser(jFrame)
                            if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return@addActionListener
                            openFile(chooser.selectedFile.path)*/
                            val chooser = FileDialog(Frame())
                            chooser.isVisible = true
                            if (chooser.file != null) openFile(chooser.directory + chooser.file)
                        }
                    }
                    text = "open"
                    border = BorderFactory.createEmptyBorder(8, 12, 8, 12)
                    isBorderPainted = false
                    isOpaque = false
                    background = Color(0x0)
                    foreground = theme.text
                })
                add(JMenuItem().apply {
                    action = object : AbstractAction() {
                        override fun actionPerformed(a: ActionEvent?) {
                            /*val chooser = FileChooser(jFrame)
                            if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return@addActionListener
                            openFile(chooser.selectedFile.path)*/
                            val chooser = FileDialog(Frame())
                            chooser.isVisible = true
                            if (chooser.file != null) openFile(chooser.directory + chooser.file)
                        }
                    }
                    text = "open folder"
                    border = BorderFactory.createEmptyBorder(8, 12, 8, 12)
                    isBorderPainted = false
                    isOpaque = false
                    background = Color(0x0)
                    foreground = theme.text
                })
                add(JMenuItem().apply {
                    action = object : AbstractAction() {
                        override fun actionPerformed(a: ActionEvent?) {
                            val chooser = FileDialog(Frame())
                            chooser.isVisible = true
                            if (chooser.file != null) {
                                TextFile.new(chooser.directory + chooser.file)
                                openFile(chooser.directory + chooser.file)
                            }
                        }
                    }
                    text = "new"
                    border = BorderFactory.createEmptyBorder(8, 12, 8, 12)
                    isBorderPainted = false
                    isOpaque = false
                    background = Color(0x0)
                    foreground = theme.text
                })
            }
            addActionListener { popup.show(this, 0, this.height) }
            toolbar.add(this)
        }

        jFrame.isLocationByPlatform = true
        jFrame.isVisible = true
    }

    private var title: String
        get() = jFrame.title
        set(value) { jFrame.title = value }
}