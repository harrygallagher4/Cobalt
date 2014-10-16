package com.stlmissouri.cobalt.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.opengl.GL13.GL_SAMPLE_ALPHA_TO_COVERAGE;

public class Render3D {

    public static void setup3DRendering() {
        setup3DRendering(false);
    }

    public static void setup3DRendering(boolean b) {
        glPushMatrix();
        glEnable(GL_BLEND);
        if (!b)
            glDisable(GL_DEPTH_TEST);
        glDisable(GL_LIGHTING);
        glEnable(GL_LINE_SMOOTH);
        glDisable(GL_TEXTURE_2D);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_MULTISAMPLE);
        glEnable(GL_SAMPLE_ALPHA_TO_COVERAGE);
        glShadeModel(GL_SMOOTH);
    }

    public static void end3DRendering() {
        glDisable(GL_BLEND);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_LINE_SMOOTH);
        glDisable(GL_MULTISAMPLE);
        glDisable(GL_SAMPLE_ALPHA_TO_COVERAGE);
        glEnable(GL_DEPTH_TEST);
        glPopMatrix();
    }

    public static void setupLineSmooth() {
        glEnable(GL_BLEND);
        glDisable(GL_LIGHTING);
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_LINE_SMOOTH);
        glDisable(GL_TEXTURE_2D);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public static void drawLine(double startX, double startY, double startZ,
                                double endX, double endY, double endZ, float thickness) {
        glPushMatrix();
        setupLineSmooth();
        glLineWidth(thickness);
        glBegin(GL_LINES);
        glVertex3d(startX, startY, startZ);
        glVertex3d(endX, endY, endZ);
        glEnd();
        glDisable(GL_BLEND);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_LINE_SMOOTH);
        glEnable(GL_DEPTH_TEST);
        glPopMatrix();
    }

}
