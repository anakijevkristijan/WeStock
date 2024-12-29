package com.example.project1.controller;

import com.example.project1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public String addUser(@RequestParam(name = "username") String username,
                          @RequestParam(name = "password") String password,
                          Model model) {
        try {
            userService.addUser(username, password);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("text", "Username already exists");
            return "register";
        }
    }
}
