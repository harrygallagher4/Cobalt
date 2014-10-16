package com.stlmissouri.cobalt.plugins.friends;

import com.google.gson.reflect.TypeToken;
import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.preferences.PreferencesSerializable;
import net.minecraft.entity.player.EntityPlayer;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Stl
 * Date: 7/5/2014
 * Time: 2:33 AM
 * Use:  Manages PlayerProperties objects based on username
 */
public class PlayerManager implements PreferencesSerializable<Map<String, PlayerProperties>> {

    private Map<String, PlayerProperties> playerProperties;

    private final Cobalt cobalt;

    public PlayerManager(Cobalt cobalt) {
        this.playerProperties = new HashMap<>();
        this.cobalt = cobalt;
        this.cobalt.preferencesManager.registerPrefs("players", this);
    }

    public PlayerProperties getProperties(String username) {
        username = username.toLowerCase();
        if (!this.playerProperties.containsKey(username)) {
            PlayerProperties properties = new PlayerProperties(username);
            this.playerProperties.put(username, properties);
            return properties;
        }
        return this.playerProperties.get(username);
    }

    public PlayerProperties getProperties(EntityPlayer player) {
        return this.getProperties(player.getCommandSenderName());
    }

    @Override
    public Map<String, PlayerProperties> getSaveObject() {
        Map<String, PlayerProperties> toSave = new HashMap<>(this.playerProperties);
        for (String s : this.playerProperties.keySet()) {
            PlayerProperties props = this.playerProperties.get(s);
            if (!shouldSave(props))
                toSave.remove(s);
        }
        return toSave;
    }

    @Override
    public void load(Map<String, PlayerProperties> savedObject) {
        this.playerProperties = savedObject;
    }

    @Override
    public Type getType() {
        return new TypeToken<Map<String, PlayerProperties>>(){}.getType();
    }

    private boolean shouldSave(PlayerProperties properties) {
        return (!properties.getDisplayName().equals(properties.getUsername()) || properties.getColor() != PlayerProperties.COLOR_NEUTRAL || properties.isFriend());
    }

}
