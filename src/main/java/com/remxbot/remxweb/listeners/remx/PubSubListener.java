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

package com.remxbot.remxweb.listeners.remx;

import com.google.common.eventbus.Subscribe;
import com.remxbot.remxweb.RemxWeb;
import com.remxbot.remxweb.conf.Settings;
import com.remxbot.remxweb.objects.network.remx.ConnectedClient;
import org.dreamexposure.novautils.events.network.pubsub.PubSubReceiveEvent;

@SuppressWarnings("UnstableApiUsage")
public class PubSubListener {

    @Subscribe
    public static void onPubSubReceive(PubSubReceiveEvent event) {
        //Handle keep alive...
        if (event.getChannelName().equalsIgnoreCase(Settings.REDIS_PREFIX.get() + "/ToWeb/Heartbeat")) {
            try {
                if (RemxWeb.getNetworkInfo().clientExists(event.getClient())) {
                    //In network, update info...
                    ConnectedClient cc = RemxWeb.getNetworkInfo().getClient(event.getClient());

                    cc.setLastKeepAlive(System.currentTimeMillis());
                    cc.setConnectedServers(event.getData().getInt("server_count"));
                    cc.setUptime(event.getData().getString("uptime"));
                    cc.setMemUsed(event.getData().getDouble("mem_used"));
                } else {
                    //Not in network, add info...
                    ConnectedClient cc = new ConnectedClient(event.getClient());

                    cc.setLastKeepAlive(System.currentTimeMillis());
                    cc.setConnectedServers(event.getData().getInt("server_count"));
                    cc.setUptime(event.getData().getString("uptime"));
                    cc.setMemUsed(event.getData().getDouble("mem_used"));

                    RemxWeb.getNetworkInfo().addClient(cc);
                }
            } catch (Exception e) {
                //TODO: invalid JSON or something, send to logger...
            }
        }
    }
}
