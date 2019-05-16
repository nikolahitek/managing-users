package com.nikolahitek.emt.lab.controller;

import com.nikolahitek.emt.lab.consts.Role;
import com.nikolahitek.emt.lab.model.Account;
import com.nikolahitek.emt.lab.service.AccountsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;


@Controller
public class AccountsController {

    private final AccountsService accountsService;

    public AccountsController(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    @PreAuthorize("hasAnyRole('USER, EMPLOYEE, MANAGER, ADMIN')")
    @GetMapping("/profile")
    public String profileForm(Model model, Principal principal) {
        model.addAttribute("message", "Hello, " + principal.getName() + " :)");
        model.addAttribute("account", accountsService.getAccountByUsername(principal.getName()));
        return "profile";
    }

    @PreAuthorize("hasAnyRole('USER, EMPLOYEE, MANAGER, ADMIN')")
    @PostMapping("/profile")
    public String profile(Model model, Principal principal,
                          @RequestParam String firstName,
                          @RequestParam String lastName,
                          @RequestParam String email) {

        model.addAttribute("message", "Hello, " + principal.getName() + " :)");

        Account updatedAccount = null;

        if (firstName != null && lastName != null) {
            updatedAccount = accountsService.updateAccountFirstAndLastName(principal.getName(), firstName, lastName);
        }
        if (email != null) {
            updatedAccount = accountsService.updateAccountEmail(principal.getName(), email);
        }

        if (updatedAccount != null) {
            model.addAttribute("account", updatedAccount);
            model.addAttribute("successMessage", "Info successfully updated.");
        } else {
            model.addAttribute("account", new Account());
            model.addAttribute("successMessage", "Problem while updating. Try again.");
        }
        return "profile";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("account", new Account());
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

        Account user = new Account(username, firstName, lastName, email, password, false, Role.USER);

        if (!accountsService.isUsernameValid(username)) {
            model.addAttribute("usernameMessage", "Username null or taken.");
            model.addAttribute("account", user);
        }

        if (!accountsService.isPasswordValid(password, matchingPassword)) {
            model.addAttribute("passwordMessage", "Passwords null or not matching.");
            model.addAttribute("account", user);
        }

        if (accountsService.isUsernameValid(username) && accountsService.isPasswordValid(password, matchingPassword)) {
            accountsService.registerAccount(user);
            model.addAttribute("successMessage", "Account successfully registered.");
            model.addAttribute("account", new Account());
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
        if (accountsService.activateAccountByCode(code)) {
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

    @PreAuthorize("hasAnyRole('USER, EMPLOYEE, MANAGER, ADMIN')")
    @PostMapping("/change/password")
    public String changePassword(Model model, Principal principal,
                                 @RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confNewPassword) {

        Account user = accountsService.getAccountByUsername(principal.getName());
        model.addAttribute("message", "Hello, " + principal.getName() + " :)");
        model.addAttribute("account", user);
        model.addAttribute("changePassMessage",
                accountsService.changePassword(user, currentPassword, newPassword, confNewPassword));
        return "profile";
    }

    @GetMapping("/reset")
    public String resetPasswordForm() {
        return "reset-password";
    }

    @PostMapping("/reset")
    public String resetPassword(Model model, @RequestParam String email) {

        if (accountsService.resetPasswordForMail(email)) {
           model.addAttribute("message", "New password sent to your email.");
        } else {
            model.addAttribute("message", "No account with that email.");
        }

        return "reset-password";
    }
}
