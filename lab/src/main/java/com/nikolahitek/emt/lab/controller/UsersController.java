package com.nikolahitek.emt.lab.controller;

import com.nikolahitek.emt.lab.model.entity.User;
import com.nikolahitek.emt.lab.service.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Arrays;

@Controller
public class UsersController {

    private final UsersService usersService;
    private final static Logger logger = LoggerFactory.getLogger(UsersController.class);

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping
    public String home(Model model, Principal principal) {
        model.addAttribute("message", "Hello, " + principal.getName() + " :)");
        return "homepage";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(Model model,
                           @RequestParam String username,
                           @RequestParam String firstName,
                           @RequestParam String lastName,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String matchingPassword) {

        User user = new User(username, firstName, lastName, email, password, false);

        if (!usersService.isUsernameValid(username)) {
            model.addAttribute("usernameMessage", "Username null or taken.");
            model.addAttribute("user", user);
        }

        if (!usersService.isPasswordValid(password, matchingPassword)) {
            model.addAttribute("passwordMessage", "Passwords null or not matching.");
            model.addAttribute("user", user);
        }

        if (usersService.isUsernameValid(username) && usersService.isPasswordValid(password, matchingPassword)) {
            usersService.registerUser(user);
            model.addAttribute("successMessage", "User successfully registered.");
            model.addAttribute("user", new User());
        }
        return "register";
    }

    @GetMapping("/activate")
    public String activateForm() {
        return "activate-form";
    }

    @PostMapping("/activate")
    public String activate(@RequestParam String code) {
        return "redirect:/activate/" + code;
    }

    @GetMapping("/activate/{code}")
    public String activateForCode(Model model,
                           @PathVariable String code) {
        if (usersService.activateUserByCode(code)) {
           model.addAttribute("message", "Your account has been activated.");
           model.addAttribute("link", "/login");
           model.addAttribute("button", "Login");
        } else {
            model.addAttribute("message", "The activation code has expired or is non existent.");
            model.addAttribute("link", "/register");
            model.addAttribute("button", "Register");
        }
        return "activate-link";
    }

    @GetMapping("/login")
    public String login(Model model, String error) {
        if (error!=null) {
            model.addAttribute("errorMessage", "Bad credentials.");
        }
        return "login";
    }
}
