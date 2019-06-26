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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@Profile({"prod", "dev"})
@EnableRedisHttpSession
public class LettuceConfig {
    @SuppressWarnings("deprecation")
    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        final LettuceConnectionFactory factory = new LettuceConnectionFactory();

        factory.setHostName(Settings.REDIS_HOSTNAME.get());
        factory.setPort(Integer.valueOf(Settings.REDIS_PORT.get()));
        factory.setPassword(Settings.REDIS_PASSWORD.get());

        return factory;
    }
}
