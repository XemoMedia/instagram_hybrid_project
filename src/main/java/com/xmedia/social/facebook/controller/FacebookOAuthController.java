
package com.xmedia.social.facebook.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xmedia.social.facebook.service.FacebookOAuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/oauth/facebook")
@RequiredArgsConstructor
public class FacebookOAuthController {

  private final FacebookOAuthService service;

  @GetMapping("/callback")
  public String callback(@RequestParam String code) {
    service.processOAuth(code);
    return "Instagram Connected Successfully";
  }
}
