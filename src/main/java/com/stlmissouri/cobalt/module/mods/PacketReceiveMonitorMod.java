package com.stlmissouri.cobalt.module.mods;

import com.darkmagician6.eventapi.EventTarget;
import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.command.Command;
import com.stlmissouri.cobalt.events.packet.PacketReceiveEvent;
import com.stlmissouri.cobalt.module.CobaltModule;
import com.stlmissouri.cobalt.module.mods.PacketSendMonitorMod.PacketSendAnalyserRenderer;
import com.stlmissouri.cobalt.render.CobaltRenderManager;
import com.stlmissouri.cobalt.render.Renderer;
import net.minecraft.network.Packet;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

public class PacketReceiveMonitorMod extends CobaltModule {

	private static final String TYPE = "RPA";
	private static final String TAG = "[" + TYPE + "]";
	private static final String USAGE = "Usage: " + TYPE.toLowerCase();
	private static final String ERROR_TAG = "[" + TYPE + " Error]";
	
	private ArrayList<String> blacklist;
	private Packet[] packets;
	private int maxLength;
	private int currentIndex;
	private PacketReceiveAnalyserRenderer renderer;

	public PacketReceiveMonitorMod(Cobalt cobalt) {
		super(cobalt, "PacketReceiveMonitor");
		renderer = new PacketReceiveAnalyserRenderer();
		maxLength = 8;
		setupCommand();
		blacklist = new ArrayList<>();
		setupPacketList();
	}

	private void setupCommand() {
		Command theCommand = new Command() {
			@Override
			public void onCommand(String... args) {
				String type = "";
				if (args.length == 1) {
					type = args[0];
					if (type.equalsIgnoreCase("reset") || type.equalsIgnoreCase("clear")) {
						blacklist.clear();
						display(TAG, "Reset blacklist.");
						return;
					} else if(type.equalsIgnoreCase("resetcount")){
						renderer.count = 0;
						display(TAG, "Reset counter.");
						return;
					} else if (type.equalsIgnoreCase("display")) {
						handleRenderer();
						return;
					} else if(type.equalsIgnoreCase("start")){
						onEnable();
						return;
					} else if(type.equalsIgnoreCase("stop")){
						onDisable();
						return;
					} else {
						displayErrorHelp();
						return;
					}
				} else if (args.length == 2) {
					type = args[0];
					String val = args[1];
					if (type.equalsIgnoreCase("blacklist")) {
						if(val.equals("default")){
							addDefaultBlacklist();
						}else{
							blacklist.add(val);
						}
						display(TAG, "Added " + val + " to blacklist.");
						return;
					} else if (type.equalsIgnoreCase("unblacklist")) {
						if(val.equals("default")){
							removeDefaultBlacklist();
						}else{
							blacklist.remove(val);
						}
						display(TAG, "Removed " + val + " from blacklist.");
						return;
					} else if (type.equalsIgnoreCase("length")) {
						try {
							maxLength = Integer.parseInt(val);
							setupPacketList();
							display(TAG, "Set max packet list length to " + maxLength);
							return;
						} catch (Exception e) {
							display(ERROR_TAG, "Maxlength must be a numeric value (" + val + ")!");
							return;
						}
					} else {
						displayErrorHelp();
						return;
					}
				} else {
					displayErrorHelp();
					return;
				}
			}
		};
		cobalt.commandManager.registerCommand("receivepacketanalyser", theCommand);
		cobalt.commandManager.registerAlias("receivepacketanalyser", "rpa");
	}

	private void displayErrorHelp() {
		display(ERROR_TAG, "Unknown RPA usage!");
		display(ERROR_TAG, USAGE + " <blacklist/clear> [name/{default}] - Blacklist a packet.");
		display(ERROR_TAG, USAGE + " <unblacklist> [name/{motion}] - Unblacklist a packet.");
		display(ERROR_TAG, USAGE + " <length> [length] - Set max length.");
		display(ERROR_TAG, USAGE + " <reset> - Reset blacklist.");
		display(ERROR_TAG, USAGE + " <resetcounter> - Reset counter.");
		display(ERROR_TAG, USAGE + " <display> - Display sent packets.");
		display(ERROR_TAG, USAGE + " <start> - Start the monitor");
		display(ERROR_TAG, USAGE + " <stop> - Stop the monitor");
	}

	private void display(String prefix, String msg) {
		cobalt.displayChat(prefix + " " + msg);
	}

	private void setupPacketList() {
		packets = new Packet[maxLength];
		currentIndex = 0;
	}
	
	private void handleRenderer(){
		if(cobalt.renderManager.hasRenderer(CobaltRenderManager.RendererLocation.INGAME, PacketSendAnalyserRenderer.RENDERER_KEY)){
			cobalt.renderManager.unregisterRenderer(CobaltRenderManager.RendererLocation.INGAME, PacketSendAnalyserRenderer.RENDERER_KEY);
		}else{
			cobalt.renderManager.registerRenderer(CobaltRenderManager.RendererLocation.INGAME, PacketSendAnalyserRenderer.RENDERER_KEY, renderer);
		}
	}
	
	private void addDefaultBlacklist(){
	}
	
	private void removeDefaultBlacklist(){
	}

	@EventTarget
	public synchronized void onPacketReceive(PacketReceiveEvent event) {
		if (currentIndex >= packets.length) {
			renderer.count += currentIndex;
			currentIndex = 0;
		}
		Packet packet = event.getPacket();
		if (!blacklist.contains(packet.getClass().getSimpleName())){
			synchronized (packets) {
				packets[currentIndex++] = packet;
			}
		}
	}

	class PacketReceiveAnalyserRenderer implements Renderer {

		public static final String RENDERER_KEY = "eu.bibl.rpa.renderer";
		
		long count = 0;
		
		@Override
		public void render() {
			synchronized (packets) {
				for (int i = 0; i < maxLength; i++) {
					Packet packet = packets[i];
					String transString = translateToString(packet, i);
					mc.fontRenderer.drawStringWithShadow(transString, 2, 14 + (9 * (i + 1)), 0xFFFFFFFF);
				}
			}
		}
		
		private String translateToString(Packet packet, int pos){
			String ptr = " ";
			if(pos == currentIndex)
				ptr = " \2474";
			if(packet == null)
				return (count + pos) + "null:[]";
			String trans = ptr + (count + pos) + "\247r: " + packet.getClass().getSimpleName() + ": [";
			int vars = 0;
			for(Field f : packet.getClass().getDeclaredFields()){
				if(!Modifier.isStatic(f.getModifiers())){
					if(!f.isAccessible())
						f.setAccessible(true);
					try {
						Object obj = f.get(packet);
						String value = "null";
						if(obj != null)
							value = obj.toString();
						trans += " f" + vars + "=" + value + ",";
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
			if(trans.endsWith(",")){
				trans = trans.substring(0, trans.length() - 2);
			}
			trans += "]";
			return trans;
		}
	}
}