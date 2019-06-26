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

package com.remxbot.remxweb;

import com.remxbot.remxweb.conf.Settings;
import com.remxbot.remxweb.network.discord.DiscordAccountHandler;
import com.remxbot.remxweb.objects.network.remx.NetworkInfo;
import okhttp3.MediaType;
import org.dreamexposure.novautils.network.pubsub.PubSubManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@SpringBootApplication(exclude = SessionAutoConfiguration.class)
public class RemxWeb {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static NetworkInfo networkInfo = new NetworkInfo();

    public static void main(String[] args) throws IOException {
        //Get settings
        Properties p = new Properties();
        p.load(new FileReader(new File("settings.properties")));
        Settings.init(p);

        //Start redis pub/sub listeners -- client ID is -1 as this is the website and not bot.
        if (Settings.USE_REDIS.get().equalsIgnoreCase("true")) {
            PubSubManager.get().init(Settings.REDIS_HOSTNAME.name(), Integer.valueOf(Settings.REDIS_PORT.get()), Settings.REDIS_PREFIX.get(), Settings.REDIS_PASSWORD.get());
            PubSubManager.get().register(-1, Settings.REDIS_PREFIX.get() + "/ToWeb/Heartbeat");
        }


        //Start up spring
        try {
            SpringApplication app = new SpringApplication(RemxWeb.class);
            app.setAdditionalProfiles(Settings.PROFILE.get());
            app.run(args);
        } catch (Exception e) {
            //TODO: send exception to logger and/or to discord webhooks
            System.exit(4);
        }

        //Init everything else
        DiscordAccountHandler.getHandler().init();
    }

    public static NetworkInfo getNetworkInfo() {
        return networkInfo;
    }
}
