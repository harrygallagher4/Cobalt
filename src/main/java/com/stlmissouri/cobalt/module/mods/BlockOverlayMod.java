package com.stlmissouri.cobalt.module.mods;

import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.command.Command;
import com.stlmissouri.cobalt.module.CobaltModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.AxisAlignedBB;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * Project: cobalt
 * Package: com.stlmissouri.cobalt.module.mods
 * Created by SabourQ on 09/03/14.
 * Use: Applies a block overlay which can be customised by the user.
 */
public class BlockOverlayMod extends CobaltModule {

    private boolean shouldRender;
    private static Color color;
    private static double opacity = 0;

    public BlockOverlayMod(final Cobalt cobalt) {
        super(cobalt, "BlockOverlay");
        cobalt.commandManager.registerCommand("boverlay", new Command() {
            @Override
            public void onCommand(String... args) {
                if(args.length == 1) {
                    if(args[0].equalsIgnoreCase("on")) {
                        setShouldRender(true);
                        cobalt.displayChat("Block overlay turned on.");
                    }else if(args[0].equalsIgnoreCase("off")) {
                        setShouldRender(false);
                        cobalt.displayChat("Block overlay turned off");
                    } else {
                        cobalt.displayChat("Unrecognised argument: " + args[0] + ". Usage: .boverlay [on/off] [color] [color]");
                    }
                }else if(args.length == 2) {
                    if(args[0].equals("on")) {
                        color = new Color(Integer.parseInt(args[1], 16));
                        int hex = Integer.parseInt(args[1], 16);
                        setShouldRender(true);
                        cobalt.displayChat("Block overlay turned on. Color set to: " + hex);
                    }else if(args[0].equalsIgnoreCase("off")) {
                        setShouldRender(false);
                        cobalt.displayChat("Block overlay turned off");
                    } else {
                        cobalt.displayChat("Unrecognised argument: " + args[0] + ". Usage: .boverlay [on/off] [color] [opacity]");
                    }
                }else if(args.length == 3) {
                    if(args[0].equals("on")) {
                        color = new Color(Integer.parseInt(args[1], 16));
                        int hex = Integer.parseInt(args[1], 16);
                        opacity = Double.parseDouble(args[2]);
                        setShouldRender(true);
                        cobalt.displayChat("Block overlay turned on. Color set to: " + hex + ". Opacity set to: " + opacity);
                    }else if(args[0].equalsIgnoreCase("off")) {
                        setShouldRender(false);
                        cobalt.displayChat("Block overlay turned off");
                    } else {
                        cobalt.displayChat("Unrecognised argument: " + args[0] + ". Usage: .boverlay [on/off] [color] [opacity]");
                    }
                }
                else {
                    cobalt.displayChat("Too many arguments/too little arguments. Usage: .boverlay [on/off] [color] [opacity]");
                }
                return;
            }
        });
    }
    private static void drawOutline(AxisAlignedBB par1AxisAlignedBB) {
        Tessellator tess = Tessellator.instance;
        //GL_LINE_STRIP
        tess.startDrawing(3);
            tess.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
            tess.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
            tess.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
            tess.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
            tess.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
        tess.draw();
        //GL_LINE_STRIP
        tess.startDrawing(3);
            tess.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
            tess.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
            tess.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
            tess.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
            tess.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
        tess.draw();
        //GL_LINE
        tess.startDrawing(1);
            tess.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
            tess.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
            tess.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
            tess.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
            tess.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
            tess.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
            tess.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
            tess.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
        tess.draw();
        //GL_LINES
        tess.startDrawing(1);
            tess.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
            tess.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
            tess.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
            tess.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
            tess.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
            tess.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
            tess.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
            tess.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
        tess.draw();
    }
    private static void drawBox(AxisAlignedBB alignedBB) {
        Tessellator tess = Tessellator.instance;

        double minX = alignedBB.minX;
        double minY = alignedBB.minY;
        double minZ = alignedBB.minZ;

        double maxX = alignedBB.maxX;
        double maxY = alignedBB.maxY;
        double maxZ = alignedBB.maxZ;
        tess.startDrawingQuads();
            tess.addVertex(minX, minY, minZ);
            tess.addVertex(minX, maxY, minZ);
            tess.addVertex(maxX, minY, minZ);
            tess.addVertex(maxX, maxY, minZ);
            tess.addVertex(maxX, minY, maxZ);
            tess.addVertex(maxX, maxY, maxZ);
            tess.addVertex(minX, minY, maxZ);
            tess.addVertex(minX, maxY, maxZ);
        tess.draw();
        tess.startDrawingQuads();
            tess.addVertex(maxX, maxY, minZ);
            tess.addVertex(maxX, minY, minZ);
            tess.addVertex(minX, maxY, minZ);
            tess.addVertex(minX, minY, minZ);
            tess.addVertex(minX, maxY, maxZ);
            tess.addVertex(minX, minY, maxZ);
            tess.addVertex(maxX, maxY, maxZ);
            tess.addVertex(maxX, minY, maxZ);
        tess.draw();
        tess.startDrawingQuads();
            tess.addVertex(minX, maxY, minZ);
            tess.addVertex(maxX, maxY, minZ);
            tess.addVertex(maxX, maxY, maxZ);
            tess.addVertex(minX, maxY, maxZ);
            tess.addVertex(minX, maxY, minZ);
            tess.addVertex(minX, maxY, maxZ);
            tess.addVertex(maxX, maxY, maxZ);
            tess.addVertex(maxX, maxY, minZ);
        tess.draw();
        tess.startDrawingQuads();
            tess.addVertex(minX, minY, minZ);
            tess.addVertex(maxX, minY, minZ);
            tess.addVertex(maxX, minY, maxZ);
            tess.addVertex(minX, minY, maxZ);
            tess.addVertex(minX, minY, minZ);
            tess.addVertex(minX, minY, maxZ);
            tess.addVertex(maxX, minY, maxZ);
            tess.addVertex(maxX, minY, minZ);
        tess.draw();
        tess.startDrawingQuads();
            tess.addVertex(minX, minY, minZ);
            tess.addVertex(minX, maxY, minZ);
            tess.addVertex(minX, minY, maxZ);
            tess.addVertex(minX, maxY, maxZ);
            tess.addVertex(maxX, minY, maxZ);
            tess.addVertex(maxX, maxY, maxZ);
            tess.addVertex(maxX, minY, minZ);
            tess.addVertex(maxX, maxY, minZ);
        tess.draw();
        tess.startDrawingQuads();
            tess.addVertex(minX, maxY, maxZ);
            tess.addVertex(minX, minY, maxZ);
            tess.addVertex(minX, maxY, minZ);
            tess.addVertex(minX, minY, minZ);
            tess.addVertex(maxX, maxY, minZ);
            tess.addVertex(maxX, minY, minZ);
            tess.addVertex(maxX, maxY, maxZ);
            tess.addVertex(maxX, minY, maxZ);
        tess.draw();

    }
    public static void drawBlockOverlay(AxisAlignedBB alignedBB) {
    Minecraft mc = Minecraft.getMinecraft();
    double curDamage = mc.playerController.getCurBlockDamageMP();
            if(color != null)
                if(opacity == 0)
                    glColor4d(color.getRed()/ 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0, 1);
                else
                    glColor4d(color.getRed()/ 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0, opacity);

            drawOutline(alignedBB);
            glDisable(GL_DEPTH_TEST);
            glDepthMask(false);
            drawBox(alignedBB.contract(1 - curDamage, 1 - curDamage, 1 - curDamage));
    }
    public boolean shouldRender() {
        return shouldRender;
    }

    public void setShouldRender(boolean shouldRender) {
        this.shouldRender = shouldRender;
    }

    @Override
    public boolean isEnabled() {
        return shouldRender;
    }
}
