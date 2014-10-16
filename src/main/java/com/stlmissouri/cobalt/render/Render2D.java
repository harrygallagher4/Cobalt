package com.stlmissouri.cobalt.render;

import static org.lwjgl.opengl.GL11.*;

/**
 * User: Stl Date: 2/2/14 Time: 5:59 PM Use: Quick rendering util
 */
public class Render2D {

    public static void setup2dRendering() {
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
        glEnable(GL_BLEND);
        glDisable(GL_LIGHTING);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public static void end2dRendering() {
        glEnable(GL_TEXTURE_2D);
    }

    public static void drawRect(double startX, double startY, double endX, double endY, int color) {
        double var5;

        if (startX < endX) {
            var5 = startX;
            startX = endX;
            endX = var5;
        }

        if (startY < endY) {
            var5 = startY;
            startY = endY;
            endY = var5;
        }
        setColor(color);
        glBegin(GL_QUADS);
        glVertex2d(startX, endY);
        glVertex2d(endX, endY);
        glVertex2d(endX, startY);
        glVertex2d(startX, startY);
        glEnd();
    }

    public static void setColor(int rgba) {
        float var10 = (float) (rgba >> 24 & 255) / 255.0F;
        float var6 = (float) (rgba >> 16 & 255) / 255.0F;
        float var7 = (float) (rgba >> 8 & 255) / 255.0F;
        float var8 = (float) (rgba & 255) / 255.0F;
        glColor4f(var6, var7, var8, var10);
    }

    public static void setColor(int rgba, float alpha) {
        float var6 = (float) (rgba >> 16 & 255) / 255.0F;
        float var7 = (float) (rgba >> 8 & 255) / 255.0F;
        float var8 = (float) (rgba & 255) / 255.0F;
        glColor4f(var6, var7, var8, alpha);
    }
}