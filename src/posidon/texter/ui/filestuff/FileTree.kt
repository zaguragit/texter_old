package posidon.texter.ui.filestuff

import posidon.texter.Window
import posidon.texter.ui.ScrollBar
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.io.File
import java.util.*
import javax.swing.*
import javax.swing.event.TreeExpansionEvent
import javax.swing.event.TreeSelectionEvent
import javax.swing.event.TreeWillExpandListener
import javax.swing.plaf.basic.BasicTreeUI
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeCellRenderer
import javax.swing.tree.MutableTreeNode
import javax.swing.tree.TreePath


class FileTree(dir: File) : JPanel() {

    private val scrollPane = JScrollPane()
    private val tree = JTree(addNodes(null, dir), true)
    private val renderer = Renderer()
    private val treeUI = DecentTreeUI()

    private fun addNodes(curTop: DefaultMutableTreeNode?, dir: File): DefaultMutableTreeNode {
        val curPath = dir.path
        val curDir = curTop ?: DefaultMutableTreeNode(dir.name)
        val ol = Vector<String>()
        val tmp = dir.list()
        if (tmp != null) for (s in tmp) ol.addElement(s)
        ol.sortWith(String.CASE_INSENSITIVE_ORDER)
        val files = Vector<String>()

        for (thisObject in ol) {
            files.addElement(
                if (File(curPath + File.separator + thisObject).isDirectory) thisObject + File.separator
                else thisObject)
        }

        for (i in files.indices) curDir.add(if (files.elementAt(i).endsWith(File.separator)) {
            DefaultMutableTreeNode(files.elementAt(i).substring(0, files.elementAt(i).length - 1), true)
        } else DefaultMutableTreeNode(files.elementAt(i), false))
        //for (i in files.indices) curDir.add(DefaultMutableTreeNode(files.elementAt(i), false))
        return curDir
    }

    fun setFolder(path: String) {
        with(tree.model.root as DefaultMutableTreeNode) {
            removeAllChildren()
            addNodes(this, File(path))
            this.userObject = path
        }
        tree.updateUI()
        tree.setUI(treeUI)
        treeUI.collapsedIcon = null
        treeUI.expandedIcon = null
    }

    fun updateTheme() {
        background = Window.theme.uiBG
        renderer.backgroundSelectionColor = Window.theme.uiHighlight
        renderer.borderSelectionColor = Window.theme.uiBG
        renderer.textSelectionColor = Window.theme.textSelected
        renderer.textNonSelectionColor = Window.theme.text
        renderer.closedIcon = Window.theme.iconTheme.folder
        renderer.openIcon = Window.theme.iconTheme.folder_open
        scrollPane.verticalScrollBar.setUI(ScrollBar())
        scrollPane.horizontalScrollBar.setUI(ScrollBar())
    }

    override fun setBackground(bg: Color?) {
        tree?.background = bg
        renderer?.backgroundNonSelectionColor = bg
    }

    override fun getMinimumSize(): Dimension { return Dimension(200, 400) }
    override fun getPreferredSize(): Dimension { return Dimension(200, 400) }

    init {
        layout = BorderLayout()
        tree.toggleClickCount = 2
        scrollPane.isOpaque = false
        scrollPane.border = null
        scrollPane.viewport.add(tree)
        add(BorderLayout.CENTER, scrollPane)
        renderer.border = BorderFactory.createEmptyBorder(4, 8, 4, 8)
        tree.cellRenderer = renderer
        tree.isRootVisible = true
        tree.setUI(treeUI)
        treeUI.collapsedIcon = null
        treeUI.expandedIcon = null
        isOpaque = false
        tree.addTreeWillExpandListener(object : TreeWillExpandListener {
            override fun treeWillCollapse(e: TreeExpansionEvent) {}

            override fun treeWillExpand(e: TreeExpansionEvent) {
                addNodes(e.path.lastPathComponent as DefaultMutableTreeNode, File(e.path.path.joinToString(File.separator)))
            }
        })
        tree.addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent) {
                val row = tree.getRowForLocation(e.x, e.y)
                if (row != -1) {
                    val path: TreePath? = tree.getPathForLocation(e.x, e.y)
                    if (e.clickCount == 2 && (path?.lastPathComponent as MutableTreeNode).isLeaf) {
                        itemDoubleClickListener?.invoke(path.path.joinToString(File.separator))
                    }
                }
            }
        })
    }

    private var itemDoubleClickListener: ((path: String) -> Unit)? = null

    fun setLeafDoubleClickListener(listener: (path: String) -> Unit) {
        itemDoubleClickListener = listener
    }

    fun addSelectionListener(listener: (e: TreeSelectionEvent) -> Unit) {
        tree.addTreeSelectionListener(listener)
    }

    private class Renderer : DefaultTreeCellRenderer() {
        override fun getTreeCellRendererComponent(
            tree: JTree?,
            value: Any?,
            selected: Boolean,
            expanded: Boolean,
            isLeaf: Boolean,
            rowIndex: Int,
            hasFocus: Boolean
        ): Component {
            if (isLeaf) try {
                val v = value.toString().toLowerCase()
                leafIcon = when {
                    v.endsWith(".kt") -> Window.theme.iconTheme.kotlin
                    v.endsWith(".java") -> Window.theme.iconTheme.java
                    v.endsWith(".xml") ||
                    v.endsWith(".iml") ||
                    v.endsWith(".html") -> Window.theme.iconTheme.xml
                    v.endsWith(".class") ||
                    v.endsWith(".elf") ||
                    v.endsWith(".exe") ||
                    v.endsWith(".efi") ||
                    v.endsWith(".so") ||
                    v.endsWith(".o") ||
                    v.endsWith(".sh") -> Window.theme.iconTheme.file_exec
                    v.endsWith(".png") ||
                    v.endsWith(".jpg") ||
                    v.endsWith(".jpeg") ||
                    v.endsWith(".tiff") ||
                    v.endsWith(".svg") ||
                    v.endsWith(".arw") ||
                    v.endsWith(".dng") -> Window.theme.iconTheme.img
                    v.endsWith(".txt") -> Window.theme.iconTheme.file_text
                    v.endsWith(".highlighter") -> Window.theme.iconTheme.file_highlighter
                    else -> Window.theme.iconTheme.file
                }
            } catch (e: Exception) {}
            return super.getTreeCellRendererComponent(tree, value, selected, expanded, isLeaf, rowIndex, hasFocus)
        }
    }

    private class DecentTreeUI : BasicTreeUI() {
        override fun paintVerticalLine(g: Graphics?, c: JComponent?, x: Int, top: Int, bottom: Int) {}
        override fun paintVerticalPartOfLeg(g: Graphics?, clipBounds: Rectangle?, insets: Insets?, path: TreePath?) {}
        override fun paintHorizontalPartOfLeg(g: Graphics?, clipBounds: Rectangle?, insets: Insets?, bounds: Rectangle?,
                                              path: TreePath?, row: Int, isExpanded: Boolean, hasBeenExpanded: Boolean, isLeaf: Boolean) {}
    }
}