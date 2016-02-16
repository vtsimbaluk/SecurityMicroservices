package com.nix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.HashMap;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@RestController
@RequestMapping("/main")
public class SsoOpenIdConnectApplication {
    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }

    @RequestMapping("/product")
    public String good() {
        return getProduct(123L, getToken());
    }

    @RequestMapping("/price")
    public String price() {
        return getPrice(123L, getToken());
    }

    @RequestMapping("/productPrice")
    public String goodWithPrice() {
        return getProductWithPrice(123L, getToken());
    }

    private String getToken() {
        OAuth2Authentication auth = (OAuth2Authentication)SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
            return String.format("%s %s", details.getTokenType(), details.getTokenValue());
        }
        return "";
    }

    private String getProduct(Long goodId, String authorizationHeader) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("goodId", goodId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", authorizationHeader);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange("http://localhost:7777/get/{goodId}", HttpMethod.GET,
            new HttpEntity(httpHeaders), String.class, params).getBody();
    }

    private String getPrice(Long goodId, String authorizationHeader)
    {
        HashMap<String, Object> params = new HashMap<>();
        params.put("goodId", goodId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", authorizationHeader);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange("http://localhost:7778/get/price/{goodId}", HttpMethod.GET,
            new HttpEntity(httpHeaders), String.class, params).getBody();
    }

    private String getProductWithPrice(Long goodId, String authorizationHeader)
    {
        HashMap<String, Object> params = new HashMap<>();
        params.put("goodId", goodId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", authorizationHeader);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange("http://localhost:7777/getWithPrice/{goodId}", HttpMethod.GET,
            new HttpEntity(httpHeaders), String.class, params).getBody();
    }

    public static void main(String[] args) {
        SpringApplication.run(SsoOpenIdConnectApplication.class, args);
    }

    @Component
    @EnableOAuth2Sso
    public static class LoginConfigurer extends WebSecurityConfigurerAdapter {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/main/**").authorizeRequests().anyRequest()
              .authenticated();
        }
    }
}
