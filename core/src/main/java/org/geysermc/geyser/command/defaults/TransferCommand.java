/*
 * Copyright (c) 2019-2022 GeyserMC. http://geysermc.org
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * @author GeyserMC
 * @link https://github.com/GeyserMC/Geyser
 */

 package org.geysermc.geyser.command.defaults;

 import java.util.List;

import org.geysermc.geyser.GeyserImpl;
 import org.geysermc.geyser.command.GeyserCommand;
 import org.geysermc.geyser.command.GeyserCommandSource;
 import org.geysermc.geyser.session.GeyserSession;
 //import org.geysermc.geyser.text.GeyserLocale;
import org.geysermc.geyser.session.SessionManager;
 
 //import java.util.stream.Collectors;
 
 public class TransferCommand extends GeyserCommand {
 
     private final GeyserImpl geyser;
 
     public TransferCommand(GeyserImpl geyser, String name, String description, String permission) {
         super(name, description, permission);
 
         this.geyser = geyser;
     }
 
     @Override
     public void execute(GeyserSession session, GeyserCommandSource sender, String[] args) {
        if (args.length != 3) {
            sender.sendMessage("bad args: expected xuid addr port");
            return;
        }

        String playerId = args[0];
        String addr = args[1];
        int    port = Integer.parseInt(args[2]);

        GeyserSession playerSession = findSession(playerId);
        if (playerSession == null) {
            sender.sendMessage(String.format("player %s: session not found", playerId));
            return;
        }

        try {
            playerSession.transfer(addr, port);
        } catch (Exception e) {
            sender.sendMessage(String.format("%s", e.toString()));
            return;
        }

        sender.sendMessage(String.format("player %s transferred to %s:%d", playerSession.bedrockUsername(), addr, port));
     }
 
     private GeyserSession findSession(String playerId) {
        SessionManager manager = geyser.getSessionManager();

        GeyserSession session = manager.sessionByXuid(playerId);
        if(session != null) {
            return session;
        }

        List<GeyserSession> sessions = manager.getAllSessions();

        for (GeyserSession s : sessions) {
            if (s == null) {
                continue;
            }
            if (playerId == s.bedrockUsername()) {
                return s;
            }

            if (playerId == s.xuid()) {
                    return s;
            }
        }

        return null;
     }
     @Override
     public boolean isSuggestedOpOnly() {
         return true;
     }
 }
 