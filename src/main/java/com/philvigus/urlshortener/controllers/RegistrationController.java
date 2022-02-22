package com.philvigus.urlshortener.controllers;

import com.philvigus.urlshortener.model.User;
import com.philvigus.urlshortener.services.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {
  private final UserService userService;

  public RegistrationController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/register")
  public String view(Model model) {
    model.addAttribute("user", new User());
    return "auth/register";
  }

  @PostMapping("/register")
  public String create(User user) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    String encodedPassword = encoder.encode(user.getPassword());

    user.setPassword(encodedPassword);

    userService.save(user);

    return "redirect:login";
  }
}
