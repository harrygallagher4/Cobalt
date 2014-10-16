package com.stlmissouri.cobalt.plugins.chatfilter;

import com.darkmagician6.eventapi.EventTarget;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.command.Command;
import com.stlmissouri.cobalt.events.packet.PacketReceiveEvent;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.IChatComponent;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ChatFilterListener {

    private Map<String, ChatFilterType> filters;
    private Map<String, Pattern> compiled;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();


    private final Cobalt cobalt;

    public ChatFilterListener(final Cobalt cobalt) {
        this.filters = new HashMap<>();
        this.compiled = new HashMap<>();
        this.cobalt = cobalt;

        this.cobalt.commandManager.registerCommand("filter", new Command() {
            @Override
            public void onCommand(String... args) {
                if(args.length < 2) {
                    cobalt.displayChat("Not enough arguments");
                    return;
                }
                ChatFilterType type = ChatFilterType.CONTAINS;
                String[] filters = new String[]{"equals", "match", "contains"};
                for (int i = 0; i < filters.length; i++) {
                    String s = filters[i];
                    if(s.equalsIgnoreCase(args[0])) {
                        type = ChatFilterType.values()[i];
                    }
                }
                String filter = StringUtils.join(args, " ", 1, args.length);
                System.out.println(filter);
                addPattern(filter, type);
                cobalt.displayChat(String.format("Filter pattern added: %s (%s)", filter, type.toString()));
                return;
            }
        });
    }

    @EventTarget
    public void onChatReciceved(PacketReceiveEvent event) {
        if (!event.getPacket().getClass().equals(S02PacketChat.class))
            return;
        S02PacketChat packet = (S02PacketChat) event.getPacket();
        IChatComponent chatComponent = packet.func_148915_c();
        String msg = net.minecraft.util.StringUtils.stripControlCodes(chatComponent.getFormattedText());
        for (String s : this.filters.keySet()) {
            ChatFilterType type = this.filters.get(s);
            Pattern p = null;
            if(type.requiresRegex)
                p = this.compiled.get(s);
            switch (type) {
                case MATCHES: {
                    if (p.matcher(msg).matches())
                        event.setCancelled(true);
                }
                case CONTAINS: {
                    if (p.matcher(msg).find())
                        event.setCancelled(true);
                }
                case EQUALS: {
                    if (s.equalsIgnoreCase(msg))
                        event.setCancelled(true);
                }
            }
        }
        if(event.isCancelled())
            System.err.println("Ignored message: " + msg);
    }

    public void addPattern(String pattern, ChatFilterType filterType) {
        this.filters.put(pattern, filterType);
        if(filterType.requiresRegex)
            this.compiled.put(pattern, Pattern.compile(pattern));
    }

    public void saveConfig(File file) {
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(gson.toJson(this.filters));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadConfig(File file) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            Type type = new TypeToken<Map<String, ChatFilterType>>(){}.getType();
            Map<String, ChatFilterType> loadedFilters = gson.fromJson(bufferedReader, type);
            if(loadedFilters == null)
                return;
            for (Map.Entry<String, ChatFilterType> entry : loadedFilters.entrySet()) {
                this.addPattern(entry.getKey(), entry.getValue());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
