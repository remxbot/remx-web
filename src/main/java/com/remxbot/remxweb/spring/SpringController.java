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

package com.remxbot.remxweb.spring;

import com.remxbot.remxweb.network.discord.DiscordAccountHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class SpringController {

    //Main pages
    @RequestMapping(value = {"/", "/home"})
    public String home(Map<String, Object> model, HttpServletRequest req) {
        model.putAll(DiscordAccountHandler.getHandler().getAccount(req));
        return "index";
    }

    @RequestMapping("/about")
    public String about(Map<String, Object> model, HttpServletRequest req) {
        model.clear();
        model.putAll(DiscordAccountHandler.getHandler().getAccount(req));
        return "about";
    }

    @RequestMapping("/commands")
    public String commands(Map<String, Object> model, HttpServletRequest req) {
        model.clear();
        model.putAll(DiscordAccountHandler.getHandler().getAccount(req));
        return "commands";
    }

    @RequestMapping("/setup")
    public String setup(Map<String, Object> model, HttpServletRequest req) {
        model.clear();
        model.putAll(DiscordAccountHandler.getHandler().getAccount(req));
        return "setup";
    }

    @RequestMapping("/status")
    public String status(Map<String, Object> model, HttpServletRequest req) {
        model.clear();
        model.putAll(DiscordAccountHandler.getHandler().getAccount(req));
        return "status";
    }

    //Policy pages
    @RequestMapping("/policy/privacy")
    public String privacyPolicy(Map<String, Object> model, HttpServletRequest req) {
        model.clear();
        model.putAll(DiscordAccountHandler.getHandler().getAccount(req));
        return "policy/privacy";
    }

    @RequestMapping("/policy/tos")
    public String termsOfService(Map<String, Object> model, HttpServletRequest req) {
        model.clear();
        model.putAll(DiscordAccountHandler.getHandler().getAccount(req));
        return "policy/tos";
    }

    //Account pages...
    @RequestMapping("/account/login")
    public String accountLogin(Map<String, Object> model, HttpServletRequest req) {
        if (DiscordAccountHandler.getHandler().hasAccount(req))
            return "redirect:/dashboard";
        model.clear();
        model.putAll(DiscordAccountHandler.getHandler().getAccount(req));
        return "account/login";
    }

    @RequestMapping("/account/logout")
    public String accountLogout(Map<String, Object> model, HttpServletRequest req) {
        if (!DiscordAccountHandler.getHandler().hasAccount(req))
            return "redirect:/account/login";
        DiscordAccountHandler.getHandler().removeAccount(req);
        model.clear();
        model.putAll(DiscordAccountHandler.getHandler().getAccount(req));
        return "redirect:/";
    }

    //Dashboard pages..
    @RequestMapping("/dashboard")
    public String mainDashboard(Map<String, Object> model, HttpServletRequest req) {
        if (!DiscordAccountHandler.getHandler().hasAccount(req))
            return "redirect:/account/login";
        model.clear();
        model.putAll(DiscordAccountHandler.getHandler().getAccount(req));
        return "dashboard/dashboard";
    }


    //Error pages
    @RequestMapping("/400")
    public String badRequest(Map<String, Object> model, HttpServletRequest req) {
        model.clear();
        model.putAll(DiscordAccountHandler.getHandler().getAccount(req));
        return "error/400";
    }

    @RequestMapping("/404")
    public String notFound(Map<String, Object> model, HttpServletRequest req) {
        model.clear();
        model.putAll(DiscordAccountHandler.getHandler().getAccount(req));
        return "error/404";
    }

    @RequestMapping("/500")
    public String internalError(Map<String, Object> model, HttpServletRequest req) {
        model.clear();
        model.putAll(DiscordAccountHandler.getHandler().getAccount(req));
        return "error/500";
    }
}