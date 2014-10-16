package com.stlmissouri.cobalt.api;

import com.darkmagician6.eventapi.EventManager;
import com.stlmissouri.cobalt.events.system.ShutdownEvent;

public class CobaltShutdownThread implements Runnable{

	@Override
	public void run() {
		ShutdownEvent event = new ShutdownEvent();
		EventManager.call(event);
	}
}