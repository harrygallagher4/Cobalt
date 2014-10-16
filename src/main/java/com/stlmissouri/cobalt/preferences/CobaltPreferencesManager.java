package com.stlmissouri.cobalt.preferences;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.events.system.DisplayCreateEvent;
import com.stlmissouri.cobalt.events.system.ShutdownEvent;
import com.stlmissouri.cobalt.info.InfoSet;
import com.stlmissouri.cobalt.util.CobaltManager;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Stl
 * Date: 7/13/2014
 * Time: 5:23 AM
 * Use:  Manages serializable preferences objects
 */
public class CobaltPreferencesManager implements CobaltManager {

    private final File configPath;
    private final File configFile;

    private final Cobalt cobalt;
    private final Map<String, PreferencesSerializable> prefsObjects = new HashMap<>();
    private Map<String, Object> loadedMap = new HashMap<>();
    private JsonObject loadedObject;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public CobaltPreferencesManager(Cobalt cobalt) {
        this.cobalt = cobalt;
        this.configPath = new File(cobalt.mc.mcDataDir, "cobalt");
        this.configFile = new File(this.configPath, "config.json");
        try {
            if (!this.configPath.exists())
                this.configPath.mkdir();
            if (!this.configFile.exists())
                this.configFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        EventManager.register(this);
    }

    public void registerPrefs(String key, PreferencesSerializable prefs) {
        key = key.toLowerCase();
        this.prefsObjects.put(key, prefs);
        if (this.loadedMap.containsKey(key)) {
            //noinspection unchecked
            prefs.load(this.gson.fromJson(this.loadedObject.get(key), prefs.getType()));
        }
    }

    @Override
    public void updateInfo(InfoSet infoSet) {

    }

    @EventTarget
    public void onInitialized(DisplayCreateEvent event) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.configFile));
            JsonParser parser = new JsonParser();
            this.loadedObject = parser.parse(reader).getAsJsonObject();
            this.loadedMap = this.gson.fromJson(this.loadedObject, new TypeToken<Map<String, Object>>(){}.getType());
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventTarget
    public void onShutdown(ShutdownEvent event) {
        try {
            Map<String, Object> toSave = new HashMap<>();
            for (String s : this.prefsObjects.keySet()) {
                PreferencesSerializable prefs = this.prefsObjects.get(s);
                toSave.put(s, prefs.getSaveObject());
            }
            FileWriter fileWriter = new FileWriter(this.configFile);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            this.gson.toJson(toSave, writer);
            System.out.println(this.gson.toJson(toSave));
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
