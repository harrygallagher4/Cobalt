package com.stlmissouri.cobalt.plugins.friends;

/**
 * User: Stl
 * Date: 7/5/2014
 * Time: 2:29 AM
 * Use:  Stores properties for a given player based on username
 */
public class PlayerProperties {

    public static final int COLOR_FRIEND = 0xbeef, COLOR_NEUTRAL = 0xc0ff1e;

    private final String username;
    private String displayName;
    private boolean isFriend;
    private int color = COLOR_NEUTRAL;

    public PlayerProperties(String username) {
        this.username = username;
        this.displayName = this.username;
    }

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        if (displayName == null)
            this.displayName = this.username;
        else
            this.displayName = displayName;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean isFriend) {
        this.isFriend = isFriend;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        if (color < 0)
            this.color = this.isFriend ? COLOR_FRIEND : COLOR_NEUTRAL;
        else
            this.color = color;
    }
}
