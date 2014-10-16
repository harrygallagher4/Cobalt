package com.stlmissouri.cobalt.module.mods;

import com.darkmagician6.eventapi.EventTarget;
import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.events.render.WorldRenderEvent;
import com.stlmissouri.cobalt.module.CobaltModule;
import com.stlmissouri.cobalt.render.Render3D;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.input.Keyboard;

import static org.lwjgl.opengl.GL11.*;

public class ChestESPMod extends CobaltModule {

    public ChestESPMod(Cobalt cobalt) {
        super(cobalt, "ChestESP");
        this.updateKeybind(Keyboard.KEY_Y);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    private double interpolate(double now, double then, double percent) {
        return then + ((now - then) * percent);
    }

    @EventTarget
    public void onRenderWorld(WorldRenderEvent e) {
        Render3D.setup3DRendering();

        for (Object o : mc.theWorld.field_147482_g) {
            if (o instanceof TileEntityChest) {
                final TileEntityChest chest = (TileEntityChest) o;
                final double renderX = chest.posX - RenderManager.renderPosX;
                final double renderY = chest.posY - RenderManager.renderPosY;
                final double renderZ = chest.posZ - RenderManager.renderPosZ;
                glPushMatrix();
                glTranslated(renderX, renderY, renderZ);
                if (chest.func_145980_j() == 1){
                    glColor3f(0.7F, 0F, 0F);
                }else{
                    glColor3f(0F, 0.7F, 1F);
                }
                if (chest.adjacentChestXPos != null) {
                    AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(0.0D, 0.0D, 0.0D, 2.0D, 1.0D, 1.0D);
                    drawOutlinedBox(boundingBox);
                    //glColor4f(1F, 1F, 0F, 0.2F);
                    //drawBox(boundingBox);
                } else if (chest.adjacentChestZPos != null) {
                    AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 2.0D);
                    drawOutlinedBox(boundingBox);
                    //glColor4f(1F, 1F, 0F, 0.2F);
                    //drawBox(boundingBox);
                } else if ((chest.adjacentChestXPos == null) && (chest.adjacentChestZPos == null) && (chest.adjacentChestXNeg == null) && (chest.adjacentChestZNeg == null)) {
                    AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
                    drawOutlinedBox(boundingBox);
                    //glColor4f(1F, 1F, 0F, 0.2F);
                    //drawBox(boundingBox);
                }
                glPopMatrix();
            }
        }

        Render3D.end3DRendering();
    }

    public void drawOutlinedBox(AxisAlignedBB boundingBox) {
        if (boundingBox == null) {
            return;
        }

        glBegin(3);
        glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        glEnd();
        glBegin(3);
        glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        glEnd();
        glBegin(1);
        glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        glEnd();
    }


}