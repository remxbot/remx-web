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

package com.remxbot.remxweb.conf;

import java.util.Properties;

public enum Settings {
    PROFILE,

    REDIS_HOSTNAME, REDIS_PORT,
    REDIS_PASSWORD, REDIS_PREFIX,

    LOG_FOLDER,

    BOT_TOKEN, BOT_SECRET, BOT_ID, REDIRECT_URI,

    PORT, ACCOUNT_TIME_OUT,

    USE_REDIS, USE_WEBHOOKS,

    DEBUG_WEBHOOK, ERROR_WEBHOOK, STATUS_WEBHOOK;

    private String val;

    Settings() {
    }

    public static void init(Properties properties) {
        for (Settings s : values()) {
            s.set(properties.getProperty(s.name()));
        }
    }

    public String get() {
        return val;
    }

    public void set(String val) {
        this.val = val;
    }
}