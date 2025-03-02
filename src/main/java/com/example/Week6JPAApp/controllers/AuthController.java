package com.example.Week6JPAApp.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class AuthController implements ErrorController {

    // Handle access denied errors and redirect to a custom 403 error page
    @GetMapping("/error")
    public String error403() {
        return "Auth/error403";
    }

    //Display the login page for authentication
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Handle logout and redirect to the home page
    @GetMapping("/logout")
    public String logout() {
        return "home";
    }
}
