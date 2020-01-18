package posidon.texter.ui

import java.awt.*
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import java.util.*
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.SwingUtilities
import javax.swing.border.Border
import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder
import javax.swing.event.CaretEvent
import javax.swing.event.CaretListener
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.text.BadLocationException
import javax.swing.text.JTextComponent
import javax.swing.text.StyleConstants
import javax.swing.text.Utilities
import kotlin.math.max

class TextLineNumber(
    private val component: JTextComponent,
    private val scrollPane: JScrollPane,
    minimumDisplayDigits: Int = 3
) : JPanel(), CaretListener, DocumentListener, PropertyChangeListener {
    //  Properties that can be changed
    var updateFont = true

    /**
     * The border gap is used in calculating the left and right insets of the
     * border. Default value is 5.
     *
     * @param borderGap  the gap in pixels
     */
    var borderGap = 0
        set(borderGap) {
            field = borderGap
            val inner: Border = EmptyBorder(0, borderGap, 0, borderGap)
            border = CompoundBorder(sideBorder, inner)
            lastDigits = 0
            setPreferredWidth()
        }

    /**
     * The Color used to render the current line digits.
     *
     * @param currentLineForeground  the Color used to render the current line
     */
    var currentLineForeground: Color? = null
        get() = if (field == null) foreground else field
    private var digitAlignment = 0f
    private var minimumDisplayDigits = 0
    //  Keep history information to reduce the number of times the component
//  needs to be repainted
    private var lastDigits = 0
    private var lastHeight = 0
    private var lastLine = 0
    private var fonts: HashMap<String, FontMetrics?>? = null

    /**
     * Gets the digit alignment
     *
     * @return the alignment of the painted digits
     */
    fun getDigitAlignment(): Float {
        return digitAlignment
    }

    /**
     * Specify the horizontal alignment of the digits within the component.
     * Common values would be:
     *
     *  * TextLineNumber.LEFT
     *  * TextLineNumber.CENTER
     *  * TextLineNumber.RIGHT (default)
     *
     * @param currentLineForeground  the Color used to render the current line
     */
    fun setDigitAlignment(digitAlignment: Float) {
        this.digitAlignment = if (digitAlignment > 1.0f) 1.0f else if (digitAlignment < 0.0f) -1.0f else digitAlignment
    }

    /**
     * Gets the minimum display digits
     *
     * @return the minimum display digits
     */
    fun getMinimumDisplayDigits(): Int {
        return minimumDisplayDigits
    }

    /**
     * Specify the mimimum number of digits used to calculate the preferred
     * width of the component. Default is 3.
     *
     * @param minimumDisplayDigits  the number digits used in the preferred
     * width calculation
     */
    fun setMinimumDisplayDigits(minimumDisplayDigits: Int) {
        this.minimumDisplayDigits = minimumDisplayDigits
        setPreferredWidth()
    }

    /**
     * Calculate the width needed to display the maximum line number
     */
    private fun setPreferredWidth() {
        val root = component.document.defaultRootElement
        val lines = root.elementCount
        val digits = max(lines.toString().length, minimumDisplayDigits)
        //  Update sizes when number of digits in the line number changes
        if (lastDigits != digits) {
            lastDigits = digits
            val fontMetrics = getFontMetrics(font)
            val width = fontMetrics.charWidth('0') * digits
            val insets = insets
            val preferredWidth = insets.left + insets.right + width
            val d = preferredSize
            d.setSize(preferredWidth, HEIGHT)
            preferredSize = d
            size = d
        }
    }

    override fun setVisible(aFlag: Boolean) {
        super.setVisible(aFlag)
        scrollPane.setRowHeaderView(if (aFlag) this else null)
    }

    /**
     * Draw the line numbers
     */
    public override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        if (g is Graphics2D) g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        //	Determine the width of the space available to draw the line number
        val fontMetrics = component.getFontMetrics(component.font)
        val insets = insets
        val availableWidth = size.width - insets.left - insets.right
        //  Determine the rows to draw within the clipped bounds.
        val clip = g.clipBounds
        var rowStartOffset = component.viewToModel(Point(0, clip.y))
        val endOffset = component.viewToModel(Point(0, clip.y + clip.height))
        while (rowStartOffset <= endOffset) {
            try {
                if (isCurrentLine(rowStartOffset)) g.color = currentLineForeground else g.color = foreground
                //  Get the line number as a string and then determine the
//  "X" and "Y" offsets for drawing the string.
                val lineNumber = getTextLineNumber(rowStartOffset)
                val stringWidth = fontMetrics.stringWidth(lineNumber)
                val x = getOffsetX(availableWidth, stringWidth) + insets.left
                val y = getOffsetY(rowStartOffset, fontMetrics)
                g.drawString(lineNumber, x, y)
                //  Move to the next row
                rowStartOffset = Utilities.getRowEnd(component, rowStartOffset) + 1
            } catch (e: Exception) {
                break
            }
        }
    }

    /*
	 *  We need to know if the caret is currently positioned on the line we
	 *  are about to paint so the line number can be highlighted.
	 */
    private fun isCurrentLine(rowStartOffset: Int): Boolean {
        val caretPosition = component.caretPosition
        val root = component.document.defaultRootElement
        return root.getElementIndex(rowStartOffset) == root.getElementIndex(caretPosition)
    }

    /*
	 *	Get the line number to be drawn. The empty string will be returned
	 *  when a line of text has wrapped.
	 */
    protected fun getTextLineNumber(rowStartOffset: Int): String {
        val root = component.document.defaultRootElement
        val index = root.getElementIndex(rowStartOffset)
        val line = root.getElement(index)
        return if (line.startOffset == rowStartOffset) (index + 1).toString() else ""
    }

    /*
	 *  Determine the X offset to properly align the line number when drawn
	 */
    private fun getOffsetX(availableWidth: Int, stringWidth: Int): Int {
        return ((availableWidth - stringWidth) * digitAlignment).toInt()
    }

    /*
	 *  Determine the Y offset for the current row
	 */
    @Throws(BadLocationException::class)
    private fun getOffsetY(
        rowStartOffset: Int,
        fontMetrics: FontMetrics
    ): Int { //  Get the bounding rectangle of the row
        val r = component.modelToView(rowStartOffset)
        val lineHeight = fontMetrics.height
        val y = r.y + r.height
        var descent = 0
        //  The text needs to be positioned above the bottom of the bounding
        //  rectangle based on the descent of the font(s) contained on the row.
        if (r.height == lineHeight) {
            descent = fontMetrics.descent
        } else {
            if (fonts == null) fonts = HashMap()
            val root = component.document.defaultRootElement
            val index = root.getElementIndex(rowStartOffset)
            val line = root.getElement(index)
            for (i in 0 until line.elementCount) {
                val child = line.getElement(i)
                val `as` = child.attributes
                val fontFamily = `as`.getAttribute(StyleConstants.FontFamily) as String
                val fontSize = `as`.getAttribute(StyleConstants.FontSize) as Int
                val key = fontFamily + fontSize
                var fm = fonts!![key]
                if (fm == null) {
                    val font = Font(fontFamily, Font.PLAIN, fontSize)
                    fm = component.getFontMetrics(font)
                    fonts!![key] = fm
                }
                descent = Math.max(descent, fm!!.descent)
            }
        }
        return y - descent
    }

    //
//  Implement CaretListener interface
//
    override fun caretUpdate(e: CaretEvent) { //  Get the line the caret is positioned on
        val caretPosition = component.caretPosition
        val root = component.document.defaultRootElement
        val currentLine = root.getElementIndex(caretPosition)
        //  Need to repaint so the correct line number can be highlighted
        if (lastLine != currentLine) {
            repaint()
            lastLine = currentLine
        }
    }

    //
//  Implement DocumentListener interface
//
    override fun changedUpdate(e: DocumentEvent) = documentChanged()
    override fun insertUpdate(e: DocumentEvent) = documentChanged()
    override fun removeUpdate(e: DocumentEvent) = documentChanged()

    /*
	 *  A document change may affect the number of displayed lines of text.
	 *  Therefore the lines numbers will also change.
	 */
    private fun documentChanged() { //  View of the component has not been updated at the time
    // the DocumentEvent is fired
        SwingUtilities.invokeLater {
            try {
                val endPos = component.document.length
                val rect = component.modelToView(endPos)
                if (rect != null && rect.y != lastHeight) {
                    setPreferredWidth()
                    repaint()
                    lastHeight = rect.y
                }
            } catch (e: BadLocationException) {}
        }
    }

    //
//  Implement PropertyChangeListener interface
//
    override fun propertyChange(evt: PropertyChangeEvent) {
        if (evt.newValue is Font) {
            if (updateFont) {
                val newFont = evt.newValue as Font
                font = newFont
                lastDigits = 0
                setPreferredWidth()
            } else repaint()
        }
    }

    companion object {
        const val LEFT = 0.0f
        const val CENTER = 0.5f
        const val RIGHT = 1.0f
        private const val HEIGHT = Int.MAX_VALUE - 1000000
    }

    var sideBorder: Border? = null
        set(value) {
            field = value
            borderGap = borderGap
        }

    init {
        font = component.font
        borderGap = 5
        setDigitAlignment(RIGHT)
        setMinimumDisplayDigits(minimumDisplayDigits)
        component.document.addDocumentListener(this)
        component.addCaretListener(this)
        component.addPropertyChangeListener("font", this)
    }
}