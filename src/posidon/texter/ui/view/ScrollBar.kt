package posidon.texter.ui.view

import posidon.texter.Window
import java.awt.Color
import java.awt.Graphics
import java.awt.Rectangle
import javax.swing.JComponent
import javax.swing.JScrollBar
import javax.swing.LookAndFeel
import javax.swing.UIManager
import javax.swing.plaf.basic.BasicArrowButton
import javax.swing.plaf.basic.BasicScrollBarUI
import kotlin.math.max
import kotlin.math.min

class ScrollBar : BasicScrollBarUI() {
    override fun configureScrollBarColors() {
        LookAndFeel.installColors(scrollbar, "ScrollBar.background", "ScrollBar.foreground")
        this.thumbColor = Window.theme.scrollBarFG
        this.trackColor = Window.theme.scrollBarBG
        this.scrollBarWidth = 10
    }

    override fun installComponents() {
        incrButton = BasicArrowButton(0, trackColor, trackColor, trackColor, trackColor)
        decrButton = BasicArrowButton(0, trackColor, trackColor, trackColor, trackColor)
        scrollbar.add(incrButton)
        scrollbar.add(decrButton)
        scrollbar.isEnabled = scrollbar.isEnabled
    }

    override fun paintThumb(g: Graphics, c: JComponent, thumbBounds: Rectangle) {
        if (!thumbBounds.isEmpty && scrollbar.isEnabled) {
            g.translate(thumbBounds.x, thumbBounds.y)
            g.color = thumbColor
            g.fillRect(0, 0, thumbBounds.width - 1, thumbBounds.height - 1)
        }
    }

    /*override fun layoutVScrollbar(sb: JScrollBar) {
        val sbSize = sb.size
        val sbInsets = sb.insets
        val itemW = sbSize.width - (sbInsets.left + sbInsets.right)
        val itemX = sbInsets.left
        val sbInsetsH = sbInsets.top + sbInsets.bottom
        val trackH = (sbSize.height - sbInsetsH - decrGap - incrGap).toFloat()
        val min = sb.minimum.toFloat()
        val extent = sb.visibleAmount.toFloat()
        val range = sb.maximum.toFloat() - min
        val value = sb.value.toFloat()
        var thumbH = if (range <= 0.0f) getMaximumThumbSize().height else (trackH * (extent / range)).toInt()
        thumbH = max(thumbH, getMinimumThumbSize().height)
        thumbH = min(thumbH, getMaximumThumbSize().height)
        var thumbY = -incrGap - thumbH
        if (value < (sb.maximum - sb.visibleAmount).toFloat()) {
            val thumbRange = trackH - thumbH.toFloat()
            thumbY = (0.5f + thumbRange * ((value - min) / (range - extent))).toInt()
            thumbY += decrGap
        }
        val itrackY = decrGap
        val itrackH = - incrGap - itrackY
        trackRect.setBounds(itemX, itrackY, itemW, itrackH)
        if (thumbH >= trackH.toInt()) {
            if (UIManager.getBoolean("ScrollBar.alwaysShowThumb"))
                setThumbBounds(itemX, itrackY, itemW, itrackH)
            else setThumbBounds(0, 0, 0, 0)
        } else {
            if (thumbY + thumbH > -incrGap) thumbY = -incrGap - thumbH
            if (thumbY < decrGap) thumbY = decrGap + 1
            setThumbBounds(itemX, thumbY, itemW, thumbH)
        }
    }

    override fun layoutHScrollbar(sb: JScrollBar) {
        val sbSize = sb.size
        val sbInsets = sb.insets
        val itemH = sbSize.height - (sbInsets.top + sbInsets.bottom)
        val itemY = sbInsets.top
        val ltr = sb.componentOrientation.isLeftToRight
        val leftGap = if (ltr) decrGap else incrGap
        val rightGap = if (ltr) incrGap else decrGap
        val sbInsetsW = sbInsets.left + sbInsets.right
        val trackW = (sbSize.width - sbInsetsW - (leftGap + rightGap)).toFloat()
        val min = sb.minimum.toFloat()
        val max = sb.maximum.toFloat()
        val extent = sb.visibleAmount.toFloat()
        val range = max - min
        val value = sb.value.toFloat()
        var thumbW = if (range <= 0.0f) getMaximumThumbSize().width else (trackW * (extent / range)).toInt()
        thumbW = max(thumbW, getMinimumThumbSize().width)
        thumbW = min(thumbW, getMaximumThumbSize().width)
        var thumbX = if (ltr) -rightGap - thumbW else leftGap
        if (value < max - sb.visibleAmount.toFloat()) {
            val thumbRange = trackW - thumbW.toFloat()
            thumbX = if (ltr) (0.5f + thumbRange * ((value - min) / (range - extent))).toInt()
            else (0.5f + thumbRange * ((max - extent - value) / (range - extent))).toInt()
            thumbX += leftGap
        }
        val itrackW = -rightGap - leftGap
        trackRect.setBounds(leftGap, itemY, itrackW, itemH)
        if (thumbW >= trackW.toInt()) {
            if (UIManager.getBoolean("ScrollBar.alwaysShowThumb"))
                setThumbBounds(leftGap, itemY, itrackW, itemH)
            else setThumbBounds(0, 0, 0, 0)
        } else {
            if (thumbX + thumbW > -rightGap) thumbX = -rightGap - thumbW
            if (thumbX < leftGap) thumbX = leftGap + 1
            setThumbBounds(thumbX, itemY, thumbW, itemH)
        }
    }*/



    /*
    protected void layoutVScrollbar(JScrollBar sb) {
        Dimension sbSize = sb.getSize();
        Insets sbInsets = sb.getInsets();
        int itemW = sbSize.width - (sbInsets.left + sbInsets.right);
        int itemX = sbInsets.left;
        boolean squareButtons = DefaultLookup.getBoolean(this.scrollbar, this, "ScrollBar.squareButtons", false);
        int decrButtonH = squareButtons ? itemW : this.decrButton.getPreferredSize().height;
        int decrButtonY = sbInsets.top;
        int incrButtonH = squareButtons ? itemW : this.incrButton.getPreferredSize().height;
        int incrButtonY = sbSize.height - (sbInsets.bottom + incrButtonH);
        int sbInsetsH = sbInsets.top + sbInsets.bottom;
        int sbButtonsH = decrButtonH + incrButtonH;
        int gaps = this.decrGap + this.incrGap;
        float trackH = (float)(sbSize.height - (sbInsetsH + sbButtonsH) - gaps);
        float min = (float)sb.getMinimum();
        float extent = (float)sb.getVisibleAmount();
        float range = (float)sb.getMaximum() - min;
        float value = (float)this.getValue(sb);
        int thumbH = range <= 0.0F ? this.getMaximumThumbSize().height : (int)(trackH * (extent / range));
        thumbH = Math.max(thumbH, this.getMinimumThumbSize().height);
        thumbH = Math.min(thumbH, this.getMaximumThumbSize().height);
        int thumbY = incrButtonY - this.incrGap - thumbH;
        if (value < (float)(sb.getMaximum() - sb.getVisibleAmount())) {
            float thumbRange = trackH - (float)thumbH;
            thumbY = (int)(0.5F + thumbRange * ((value - min) / (range - extent)));
            thumbY += decrButtonY + decrButtonH + this.decrGap;
        }

        int sbAvailButtonH = sbSize.height - sbInsetsH;
        if (sbAvailButtonH < sbButtonsH) {
            incrButtonH = decrButtonH = sbAvailButtonH / 2;
            incrButtonY = sbSize.height - (sbInsets.bottom + incrButtonH);
        }

        this.decrButton.setBounds(itemX, decrButtonY, itemW, decrButtonH);
        this.incrButton.setBounds(itemX, incrButtonY, itemW, incrButtonH);
        int itrackY = decrButtonY + decrButtonH + this.decrGap;
        int itrackH = incrButtonY - this.incrGap - itrackY;
        this.trackRect.setBounds(itemX, itrackY, itemW, itrackH);
        if (thumbH >= (int)trackH) {
            if (UIManager.getBoolean("ScrollBar.alwaysShowThumb")) {
                this.setThumbBounds(itemX, itrackY, itemW, itrackH);
            } else {
                this.setThumbBounds(0, 0, 0, 0);
            }
        } else {
            if (thumbY + thumbH > incrButtonY - this.incrGap) {
                thumbY = incrButtonY - this.incrGap - thumbH;
            }

            if (thumbY < decrButtonY + decrButtonH + this.decrGap) {
                thumbY = decrButtonY + decrButtonH + this.decrGap + 1;
            }

            this.setThumbBounds(itemX, thumbY, itemW, thumbH);
        }

    }

    protected void layoutHScrollbar(JScrollBar sb) {
        Dimension sbSize = sb.getSize();
        Insets sbInsets = sb.getInsets();
        int itemH = sbSize.height - (sbInsets.top + sbInsets.bottom);
        int itemY = sbInsets.top;
        boolean ltr = sb.getComponentOrientation().isLeftToRight();
        boolean squareButtons = DefaultLookup.getBoolean(this.scrollbar, this, "ScrollBar.squareButtons", false);
        int leftButtonW = squareButtons ? itemH : this.decrButton.getPreferredSize().width;
        int rightButtonW = squareButtons ? itemH : this.incrButton.getPreferredSize().width;
        int leftButtonX;
        if (!ltr) {
            leftButtonX = leftButtonW;
            leftButtonW = rightButtonW;
            rightButtonW = leftButtonX;
        }

        leftButtonX = sbInsets.left;
        int rightButtonX = sbSize.width - (sbInsets.right + rightButtonW);
        int leftGap = ltr ? this.decrGap : this.incrGap;
        int rightGap = ltr ? this.incrGap : this.decrGap;
        int sbInsetsW = sbInsets.left + sbInsets.right;
        int sbButtonsW = leftButtonW + rightButtonW;
        float trackW = (float)(sbSize.width - (sbInsetsW + sbButtonsW) - (leftGap + rightGap));
        float min = (float)sb.getMinimum();
        float max = (float)sb.getMaximum();
        float extent = (float)sb.getVisibleAmount();
        float range = max - min;
        float value = (float)this.getValue(sb);
        int thumbW = range <= 0.0F ? this.getMaximumThumbSize().width : (int)(trackW * (extent / range));
        thumbW = Math.max(thumbW, this.getMinimumThumbSize().width);
        thumbW = Math.min(thumbW, this.getMaximumThumbSize().width);
        int thumbX = ltr ? rightButtonX - rightGap - thumbW : leftButtonX + leftButtonW + leftGap;
        if (value < max - (float)sb.getVisibleAmount()) {
            float thumbRange = trackW - (float)thumbW;
            if (ltr) {
                thumbX = (int)(0.5F + thumbRange * ((value - min) / (range - extent)));
            } else {
                thumbX = (int)(0.5F + thumbRange * ((max - extent - value) / (range - extent)));
            }

            thumbX += leftButtonX + leftButtonW + leftGap;
        }

        int sbAvailButtonW = sbSize.width - sbInsetsW;
        if (sbAvailButtonW < sbButtonsW) {
            rightButtonW = leftButtonW = sbAvailButtonW / 2;
            rightButtonX = sbSize.width - (sbInsets.right + rightButtonW + rightGap);
        }

        (ltr ? this.decrButton : this.incrButton).setBounds(leftButtonX, itemY, leftButtonW, itemH);
        (ltr ? this.incrButton : this.decrButton).setBounds(rightButtonX, itemY, rightButtonW, itemH);
        int itrackX = leftButtonX + leftButtonW + leftGap;
        int itrackW = rightButtonX - rightGap - itrackX;
        this.trackRect.setBounds(itrackX, itemY, itrackW, itemH);
        if (thumbW >= (int)trackW) {
            if (UIManager.getBoolean("ScrollBar.alwaysShowThumb")) {
                this.setThumbBounds(itrackX, itemY, itrackW, itemH);
            } else {
                this.setThumbBounds(0, 0, 0, 0);
            }
        } else {
            if (thumbX + thumbW > rightButtonX - rightGap) {
                thumbX = rightButtonX - rightGap - thumbW;
            }

            if (thumbX < leftButtonX + leftButtonW + leftGap) {
                thumbX = leftButtonX + leftButtonW + leftGap + 1;
            }

            this.setThumbBounds(thumbX, itemY, thumbW, itemH);
        }

    }*/
}