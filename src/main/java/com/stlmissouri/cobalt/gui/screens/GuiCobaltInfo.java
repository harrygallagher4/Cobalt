package com.stlmissouri.cobalt.gui.screens;

import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.info.InfoNode;
import com.stlmissouri.cobalt.info.InfoSet;
import com.stlmissouri.cobalt.info.ParentInfoNode;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

/**
 * User: Stl
 * Date: 4/1/2014
 * Time: 9:40 PM
 * Use:
 */
public class GuiCobaltInfo extends GuiScreen {

    private final char[] bullets = new char[]{'\u2043', '\u2022', '\u25E6'};

    private final int INDENT_SIZE = 8;
    private final int NEWLINE_SIZE = 10;
    private final int X_DEFAULT = 2;
    private final int Y_DEFAULT = 12;

    private InfoSet infoSet;

    private int x, y, level = 0, longest = 0;

    private ScaledResolution sr;

    public GuiCobaltInfo(Cobalt cobalt, InfoSet infoSet) {
        super();
        this.mc = cobalt.mc;
        this.infoSet = infoSet;
        this.x = this.X_DEFAULT;
        this.y = this.Y_DEFAULT;
    }

    @Override
    public void initGui() {
        this.sr = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        super.drawScreen(par1, par2, par3);
        this.drawDefaultBackground();
        for(InfoNode node : this.infoSet.getNodeSet()) {
            this.renderNode(node);
        }
        this.x = this.X_DEFAULT;
        this.y = this.Y_DEFAULT;
    }

    public void renderNode(InfoNode node) {
        if(node instanceof ParentInfoNode) {
            this.draw(node.getInfoString(), 0xFFFFFFFF);
            this.x += INDENT_SIZE;
            this.y += NEWLINE_SIZE;
            this.level++;
            for (InfoNode child : ((ParentInfoNode) node).getChildren()) {
                this.renderNode(child);
            }
            this.level--;
            this.x -= INDENT_SIZE;
        } else {
            this.draw(node.getInfoString(), 0xFFFFFFFF);
            this.y += NEWLINE_SIZE;
        }
    }

    private String getBullet() {
        this.level %= this.bullets.length;
        return this.bullets[this.level] + " ";
    }

    private void draw(String str, int color) {
        int end = fontRendererObj.getStringWidth(getBullet() + str);
        if(end > this.longest)
            this.longest = this.x + end + this.INDENT_SIZE * 2;
        if(this.y + fontRendererObj.FONT_HEIGHT >= sr.getScaledHeight()) {
            this.y = 2;
            this.x = this.longest;
        }
        fontRendererObj.drawStringWithShadow(getBullet() + str, this.x, this.y, color);
    }

}
