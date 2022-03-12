package com.philvigus.urlshortener.controllers;

import com.philvigus.urlshortener.model.Url;
import com.philvigus.urlshortener.model.User;
import com.philvigus.urlshortener.services.UrlService;
import com.philvigus.urlshortener.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class UrlController {
  private final UrlService urlService;
  private final UserService userService;

  public UrlController(UrlService urlService, UserService userService) {
    this.urlService = urlService;
    this.userService = userService;
  }

  @GetMapping("/urls/{id}")
  public String view(
      @AuthenticationPrincipal UserDetails authedUserDetails,
      @PathVariable("id") long id,
      Model model) {
    Optional<Url> url = urlService.findById(id);

    if (!url.isPresent()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    User authedUser = userService.findByUsername(authedUserDetails.getUsername());

    if (url.get().getUser() != authedUser) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    model.addAttribute("url", urlService.findById(id).get());

    return "url/view";
  }

  @GetMapping("/urls/add")
  public String add(@ModelAttribute Url url, Model model) {

    model.addAttribute("url", url);

    return "url/add";
  }

  @DeleteMapping("/urls/{id}")
  public String delete(
      @AuthenticationPrincipal UserDetails authedUserDetails, @PathVariable("id") long id) {
    Optional<Url> urlToDelete = urlService.findById(id);

    if (!urlToDelete.isPresent()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    User authedUser = userService.findByUsername(authedUserDetails.getUsername());

    if (!urlToDelete.get().getUser().equals(authedUser)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    urlService.deleteById(id);

    return "redirect:/dashboard";
  }

  @PostMapping("/urls")
  public String create(
      @AuthenticationPrincipal UserDetails authedUserDetails,
      @Valid @ModelAttribute Url url,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "url/add";
    }

    User authedUser = userService.findByUsername(authedUserDetails.getUsername());

    urlService.save(url, authedUser);

    return "redirect:/dashboard";
  }

  @PutMapping("/urls/{id}")
  public String update(
      @AuthenticationPrincipal UserDetails authedUserDetails,
      @PathVariable("id") long id,
      @Valid @ModelAttribute Url url,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "url/view";
    }

    Optional<Url> urlToUpdate = urlService.findById(id);

    if (!urlToUpdate.isPresent()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    User authedUser = userService.findByUsername(authedUserDetails.getUsername());

    if (!urlToUpdate.get().getUser().equals(authedUser)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    urlService.update(url, authedUser);

    return "redirect:/dashboard";
  }
}
