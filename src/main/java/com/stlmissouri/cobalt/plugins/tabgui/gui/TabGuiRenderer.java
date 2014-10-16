package com.stlmissouri.cobalt.plugins.tabgui.gui;

import com.stlmissouri.cobalt.plugins.tabgui.TabGui;
import com.stlmissouri.cobalt.render.Renderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

/**
 * User: Stl
 * Date: 4/29/2014
 * Time: 7:38 PM
 * Use:
 */
public class TabGuiRenderer implements Renderer {

    private static final int LINE_HEIGHT = 10;
    private static final int TAB_SPACING = 6;
    private int offset = TAB_SPACING;
    private int depth = 0;
    private int xOffset = 0;
    private int yOffset = 0;
    private TabGui gui;

    private FontRenderer fontRenderer;

    public TabGuiRenderer(TabGui gui, Minecraft mc) {
        this.gui = gui;
        this.fontRenderer = mc.fontRenderer;
    }

    @Override
    public void render() {
        if (this.fontRenderer == null)
            this.fontRenderer = Minecraft.getMinecraft().fontRenderer;
        renderLayer(gui.getComponents());
    }

    public void renderLayer(TabNode[] nodes) {
        int width = getWidth(nodes);
        int y = LINE_HEIGHT + 4;
        int x = 2;
        int count = 0;
        for (TabNode node : nodes) {
            boolean selected = gui.isSelected(depth, count);
            this.fontRenderer.drawStringWithShadow(node.getName(), x + this.xOffset, y + this.yOffset, selected ? 0xFFFFFF : 0xAAAAAA);
            if (node instanceof TabParentNode && selected && this.gui.getDepth() > this.depth) {
                int oldX = xOffset, oldY = yOffset;
                this.xOffset += (width + TAB_SPACING);
                this.yOffset += count * LINE_HEIGHT;
                this.depth++;
                renderLayer(((TabParentNode) node).getChildren());
                this.xOffset = oldX;
                this.yOffset = oldY;
                this.depth--;
            }
            y += LINE_HEIGHT;
            count++;
        }
    }

    private int getWidth(TabNode[] nodes) {
        int max = 0;
        for (TabNode node : nodes) {
            int width = fontRenderer.getStringWidth(node.getName());
            if (width > max)
                max = width;
        }
        return max;
    }

    private int getHeight(TabNode[] nodes) {
        return 4 + nodes.length * LINE_HEIGHT;
    }

}
