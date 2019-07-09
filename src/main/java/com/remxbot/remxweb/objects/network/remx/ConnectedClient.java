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

package com.remxbot.remxweb.objects.network.remx;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConnectedClient {
    private final int clientIndex;

    private int expectedClients;
    private String baseUrl;
    private int connectedServers;
    private long lastKeepAlive;
    private String uptime;
    private double memUsed;

    public ConnectedClient(int _clientIndex) {
        clientIndex = _clientIndex;

        connectedServers = 0;
        lastKeepAlive = System.currentTimeMillis();

        uptime = "ERROR";
        memUsed = 0;
    }

    //Getters
    public int getClientIndex() {
        return clientIndex;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public int getExpectedClients() {
        return expectedClients;
    }

    public int getConnectedServers() {
        return connectedServers;
    }

    public long getLastKeepAlive() {
        return lastKeepAlive;
    }

    public String getLastKeepAliveHumanReadable() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        return sdf.format(new Date(lastKeepAlive));
    }

    public String getUptime() {
        return uptime;
    }

    public double getMemUsed() {
        return memUsed;
    }

    //Setters
    public void setExpectedClients(int expectedClients) {
        this.expectedClients = expectedClients;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setConnectedServers(int _connectedServers) {
        connectedServers = _connectedServers;
    }

    public void setLastKeepAlive(long _lastKeepAlive) {
        lastKeepAlive = _lastKeepAlive;
    }

    public void setUptime(String _uptime) {
        uptime = _uptime;
    }

    public void setMemUsed(double _mem) {
        memUsed = _mem;
    }
}