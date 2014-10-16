package com.stlmissouri.cobalt.util;

import java.util.Random;

/**
 * User: Stl
 * Date: 5/18/2014
 * Time: 2:44 PM
 * Use:
 */
public enum ChatColor {

    BLACK('0'),
    DARK_BLUE('1'),
    DARK_GREEN('2'),
    DARK_AQUA('3'),
    DARK_RED('4'),
    DARK_PURPLE('5'),
    GOLD('6'),
    GRAY('7'),
    DARK_GRAY('8'),
    BLUE('9'),
    GREEN('A'),
    AQUA('B'),
    RED('C'),
    PURPLE('D'),
    YELLOW('E'),
    WHITE('F'),
    OBFUSCATED('K'),
    BOLD('L'),
    STRIKETHROUGH('M'),
    UNDERLINE('N'),
    ITALIC('O'),
    RESET('R');

    private static final String COLOR_CHAR = "\u00A7";
    private char colorCode;

    public static final ChatColor[] colors  = new ChatColor[]{BLACK, DARK_BLUE, DARK_GREEN, DARK_AQUA, DARK_RED, DARK_PURPLE, GOLD, GRAY, DARK_GRAY, BLUE, GREEN, AQUA, RED, PURPLE, YELLOW, WHITE};
    public static final ChatColor[] formats = new ChatColor[]{OBFUSCATED, BOLD, STRIKETHROUGH, UNDERLINE, ITALIC};

    private static final Random rng = new Random();

    private ChatColor(char colorCode) {
        this.colorCode = colorCode;
    }

    public char getColorCode() {
        return this.colorCode;
    }

    public int getIndex() {
        return "0123456789abcdefklmnor".indexOf(Character.toLowerCase(this.colorCode));
    }

    public String toString() {
        return String.valueOf(COLOR_CHAR + this.colorCode);
    }

    public static String translateColorCodes(String input, String from) {
        return input.replaceAll(from, COLOR_CHAR);
    }

    public static ChatColor randomColor() {
        return colors[rng.nextInt(colors.length - 1)];
    }

    public static ChatColor randomFormat() {
        return formats[rng.nextInt(formats.length - 1)];
    }

}
