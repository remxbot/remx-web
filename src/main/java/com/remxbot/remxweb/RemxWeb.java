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
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@SpringBootApplication
public class RemxWeb {
    public static void main(String[] args) throws IOException {
        //Get settings
        Properties p = new Properties();
        p.load(new FileReader(new File("settings.properties")));
        Settings.init(p);

        try {
            //SpringController.makeModel(); //TODO: Do this!
            SpringApplication.run(RemxWeb.class, args);
        } catch (Exception e) {
            //TODO: send exception to logger and/or to discord webhooks
            System.exit(4);
        }
    }
}
