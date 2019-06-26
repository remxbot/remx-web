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

package com.remxbot.remxweb.api.v1.endpoints;

import com.remxbot.remxweb.conf.Settings;
import com.remxbot.remxweb.network.discord.DiscordAccountHandler;
import com.remxbot.remxweb.objects.guild.WebGuild;
import okhttp3.*;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
public class AccountEndpoints {
    @SuppressWarnings("ConstantConditions")
    @GetMapping("/api/v1/account/login")
    public static String handleDiscordCode(HttpServletRequest req, HttpServletResponse res, @RequestParam(value = "code") String code) {
        OkHttpClient client = new OkHttpClient();

        try {
            RequestBody tokenBody = new FormBody.Builder()
                    .addEncoded("client_id", Settings.BOT_ID.get())
                    .addEncoded("client_secret", Settings.BOT_SECRET.get())
                    .addEncoded("grant_type", "authorization_code")
                    .addEncoded("code", code)
                    .addEncoded("redirect_uri", Settings.REDIRECT_URI.get())
                    .build();

            okhttp3.Request tokenRequest = new okhttp3.Request.Builder()
                    .url("https://discordapp.com/api/v6/oauth2/token")
                    .post(tokenBody)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .build();

            //POST request to discord for access...
            okhttp3.Response tokenResponse = client.newCall(tokenRequest).execute();

            @SuppressWarnings("ConstantConditions")
            JSONObject tokenInfo = new JSONObject(tokenResponse.body().string());

            if (tokenInfo.has("access_token")) {
                //GET request for user info...
                Request userDataRequest = new Request.Builder()
                        .url("https://discordapp.com/api/v6/users/@me")
                        .header("Authorization", "Bearer " + tokenInfo.getString("access_token"))
                        .build();
                Response userDataResponse = client.newCall(userDataRequest).execute();

                Request userGuildsRequest = new Request.Builder()
                        .url("https://discordapp.com/api/v6/users/@me/guilds")
                        .header("Authorization", "Bearer " + tokenInfo.getString("access_token"))
                        .build();
                Response userGuildsResponse = client.newCall(userGuildsRequest).execute();

                JSONObject userInfo = new JSONObject(userDataResponse.body().string());
                JSONArray jGuilds = new JSONArray(userGuildsResponse.body().string());

                //Saving session info and access info to memory until moved into the database...
                Map<String, Object> m = new HashMap<>();
                m.put("logged_in", true);
                m.put("bot_id", Settings.BOT_ID.get());
                m.put("year", LocalDate.now().getYear());
                m.put("redirect_uri", Settings.REDIRECT_URI.get());

                m.put("id", userInfo.getString("id"));
                m.put("username", userInfo.getString("username"));
                if (userInfo.has("avatar") && !userInfo.isNull("avatar")) {
                    m.put("pfp", "https://cdn.discordapp.com/avatars/"
                            + userInfo.getString("id")
                            + "/"
                            + userInfo.getString("avatar")
                            + ".png");
                } else {
                    m.put("pfp", "/assets/img/default-pfp.png");
                }
                m.put("discrim", userInfo.getString("discriminator"));

                List<WebGuild> guilds = new ArrayList<>();
                for (int i = 0; i < jGuilds.length(); i++) {
                    JSONObject jGuild = jGuilds.getJSONObject(i);

                    WebGuild wg = new WebGuild();
                    wg.setId(jGuild.getLong("id"));
                    wg.setName(jGuild.getString("name"));
                    if (jGuild.has("icon") && !jGuild.isNull("icon")) {
                        wg.setIcon("https://cdn.discordapp.com/icons/"
                                + wg.getId()
                                + "/"
                                + jGuild.getString("icon")
                                + ".png");
                    } else {
                        wg.setIcon("/assets/img/default-icon.png");
                    }
                    guilds.add(wg);
                }
                m.put("guilds", guilds);

                String newSessionId = UUID.randomUUID().toString();

                req.getSession(true).setAttribute("account", newSessionId);

                DiscordAccountHandler.getHandler().addAccount(m, req);

                //Finally redirect to the dashboard seamlessly.
                res.setStatus(200);
                return "redirect:/dashboard";
            } else {
                //Token not provided. Authentication denied or errored...
                res.setStatus(405);
                return "redirect:/account/login";
            }
        } catch (Exception e) {
            //TODO: Send to logger
            res.setStatus(500);
            return "redirect:/500";
        }
    }
}
