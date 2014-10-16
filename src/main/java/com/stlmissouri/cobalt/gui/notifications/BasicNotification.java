package com.stlmissouri.cobalt.gui.notifications;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.util.ArrayList;
import java.util.List;

import static com.stlmissouri.cobalt.render.Render2D.*;

/**
 * User: Stl
 * Date: 2/2/14
 * Time: 5:57 PM
 * Use:  Basic Notification implementation
 */
public class BasicNotification extends Notification {

    private int width = 110;
    private int height = 40;

    private FontRenderer fontRenderer;

    public BasicNotification(String title, String message) {
        super(title, splitMessage(message, 110 - 6));
        int lineTime = this.message.size() * 20;
        if (lineTime > this.time)
            this.time = lineTime;
        this.fontRenderer = Minecraft.getMinecraft().fontRenderer;
        adjust();
    }

    @Override
    public void draw() {
        setup2dRendering();
        drawRect(0, 0, width, height, color(0xE0E0E0));
        end2dRendering();
        fontRenderer.drawString("\247n" + title, 4, 4, color(0x0099FF));
        drawList(message, 6, 16, color(0x7C7C7C));
    }

    private int color(int input) {
        if (this.time > 20)
            return 0xFF << 24 | input;
        float opac = (((float) this.time - Minecraft.getMinecraft().timer.renderPartialTicks) / 20.0F) * 255.0F;
        return (int) opac << 24 | input;
    }

    private void drawList(List<String> stringList, int xi, int yi, int color) {
        int offset = 0;
        for (String s : stringList) {
            fontRenderer.drawString(s, xi, yi + offset, color);
            offset += 12;
        }
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    private static List<String> splitMessage(String message, int width) {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        List<String> messages = new ArrayList<>();
        String[] split = message.split(" ");
        StringBuilder builder = new StringBuilder();
        int currentLength = 0;
        for (String word : split) {
            currentLength += fontRenderer.getStringWidth(word);
            if (currentLength + 10 >= width) {
                messages.add(builder.toString());
                builder = new StringBuilder(word).append(" ");
                currentLength = fontRenderer.getStringWidth(word);
            } else {
                builder.append(word).append(" ");
            }
        }
        messages.add(builder.toString());
        return messages;
    }

    private void adjust() {
        int initialHeight = 16;
        for (String aMessage : message) {
            initialHeight += 12;
        }
        if (initialHeight > this.height)
            this.height = initialHeight;
    }

}
