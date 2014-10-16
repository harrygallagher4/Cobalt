package com.stlmissouri.cobalt.util.auth;

import com.darkmagician6.eventapi.EventManager;
import com.mojang.authlib.Agent;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.stlmissouri.cobalt.gui.notifications.NotificationEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import java.net.Proxy;
import java.util.UUID;

/**
 * User: Stl
 * Date: 2/2/14
 * Time: 4:48 PM
 * Use:  Thread to log in to Minecraft
 */
public class AuthThread implements Runnable {

    private final UserAuthentication ua;

    public AuthThread(String username, String password) {
        YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, UUID.randomUUID().toString());
        Agent agent = Agent.MINECRAFT;
        this.ua = service.createUserAuthentication(agent);
        this.ua.setUsername(username);
        this.ua.setPassword(password);
    }

    @Override
    public void run() {
        try {
            ua.logIn();
            Minecraft.getMinecraft().setSession(new Session(ua.getSelectedProfile().getName(), ua.getSelectedProfile().getId(), ua.getAuthenticatedToken()));
            EventManager.call(new NotificationEvent("Logged in!", "Successfully logged in as " + ua.getSelectedProfile().getName()));
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
    }

}
