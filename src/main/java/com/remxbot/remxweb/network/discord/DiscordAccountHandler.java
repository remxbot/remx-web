/*
 * This file is part of remxweb.
 *
 * remxweb is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * remxweb is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with remxweb.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.remxbot.remxweb.network.discord;

import com.remxbot.remxweb.RemxWeb;
import com.remxbot.remxweb.conf.Settings;
import org.joda.time.LocalDate;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@SuppressWarnings({"RedundantCast", "Duplicates", "unused"})
public class DiscordAccountHandler {
    private static DiscordAccountHandler instance;
    private static Timer timer;

    private HashMap<String, Map<String, Object>> discordAccounts = new HashMap<>();

    //Instance handling
    private DiscordAccountHandler() {
    } //Prevent initialization

    public static DiscordAccountHandler getHandler() {
        if (instance == null)
            instance = new DiscordAccountHandler();

        return instance;
    }

    public void init() {
        timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                removeTimedOutAccounts();
            }
        }, 60 * 30 * 1000);
    }

    public void shutdown() {
        if (timer != null)
            timer.cancel();
    }

    //Boolean/checkers
    public boolean hasAccount(HttpServletRequest request) {
        try {
            return discordAccounts.containsKey((String) request.getSession(true).getAttribute("account"));
        } catch (Exception e) {
            return false;
        }
    }

    //Getters
    public Map<String, Object> getAccount(HttpServletRequest request) {
        if ((String) request.getSession(true).getAttribute("account") != null && discordAccounts.containsKey((String) request.getSession(true).getAttribute("account"))) {
            Map<String, Object> m = discordAccounts.get((String) request.getSession(true).getAttribute("account"));
            m.remove("last_use");
            m.put("last_use", System.currentTimeMillis());

            m.remove("status");
            m.put("status", RemxWeb.getNetworkInfo());

            return m;

        } else {
            //Not logged in...
            Map<String, Object> m = new HashMap<>();
            m.put("logged_in", false);
            m.put("bot_id", Settings.BOT_ID.get());
            m.put("year", LocalDate.now().getYear());
            m.put("redirect_uri", Settings.REDIRECT_URI.get());
            m.put("status", RemxWeb.getNetworkInfo());

            return m;
        }
    }

    public Map<String, Object> findAccount(String userId) {
        for (Map<String, Object> m : discordAccounts.values()) {
            if (m.containsKey("id")) {
                if (m.get("id").equals(userId)) {
                    m.remove("last_use");
                    m.put("last_use", System.currentTimeMillis());
                    return m;
                }
            }
        }
        return null;
    }

    public int accountCount() {
        return discordAccounts.size();
    }

    //Functions
    public void addAccount(Map<String, Object> m, HttpServletRequest request) {
        discordAccounts.remove((String) request.getSession(true).getAttribute("account"));
        m.remove("last_use");
        m.put("last_use", System.currentTimeMillis());
        discordAccounts.put((String) request.getSession(true).getAttribute("account"), m);
    }

    public void removeAccount(HttpServletRequest request) {
        if ((String) request.getSession(true).getAttribute("account") != null && hasAccount(request))
            discordAccounts.remove((String) request.getSession(true).getAttribute("account"));
    }

    private void removeTimedOutAccounts() {
        long limit = Long.valueOf(Settings.ACCOUNT_TIME_OUT.get());
        final List<String> toRemove = new ArrayList<>();
        for (String id : discordAccounts.keySet()) {
            Map<String, Object> m = discordAccounts.get(id);
            long lastUse = (long) m.get("last_use");
            if (System.currentTimeMillis() - lastUse > limit)
                toRemove.remove(id); //Timed out, remove account info and require sign in.
        }

        for (String id : toRemove) {
            discordAccounts.remove(id);
        }
    }
}