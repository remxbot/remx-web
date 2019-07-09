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

import org.joda.time.Interval;
import org.joda.time.Period;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
public class NetworkInfo {
    private List<ConnectedClient> clients = new ArrayList<>();

    //Getters
    public List<ConnectedClient> getClients() {
        return new ArrayList<>(clients);
    }

    public boolean clientExists(int clientIndex) {
        for (ConnectedClient cc : clients) {
            if (cc.getClientIndex() == clientIndex)
                return true;
        }
        return false;
    }

    public ConnectedClient getClient(int clientIndex) {
        for (ConnectedClient cc : clients) {
            if (cc.getClientIndex() == clientIndex)
                return cc;
        }
        return null;
    }

    public void addClient(ConnectedClient client) {
        clients.add(client);
        //TODO: Log client connected
    }

    public void removeClient(int clientIndex) {
        if (clientExists(clientIndex)) {
            clients.remove(getClient(clientIndex));
            //TODO: Log client disconnected
        }
    }

    public int getTotalGuildCount() {
        int count = 0;
        for (ConnectedClient cc : clients) {
            count += cc.getConnectedServers();
        }

        return count;
    }

    public int getClientCount() {
        return clients.size();
    }

    public int getExpectedClients() {
        if (!clients.isEmpty())
            return clients.get(0).getExpectedClients();
        return 1;
    }

    public String getUptime() {
        RuntimeMXBean mxBean = ManagementFactory.getRuntimeMXBean();
        Interval interval = new Interval(mxBean.getStartTime(), System.currentTimeMillis());
        Period period = interval.toPeriod();

        return String.format("%d months, %d days, %d hours, %d minutes, %d seconds%n", period.getMonths(), period.getDays(), period.getHours(), period.getMinutes(), period.getSeconds());
    }
}
