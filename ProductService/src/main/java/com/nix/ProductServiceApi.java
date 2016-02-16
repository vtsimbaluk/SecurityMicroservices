package com.nix;

import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.HashMap;

@RestController
@Component(value = "userRestClient")
@Log4j
public class ProductServiceApi {
  @RequestMapping(value = "/get/{productId}", method = RequestMethod.GET)
  public @ResponseBody
  Product getGood(@PathVariable("productId") Long productId,
      @RequestHeader(value="Authorization") String authorizationHeader,
      Principal currentUser) {
    log.info(String.format("ProductApi: User=%s, Auth=%s", currentUser.getName(), authorizationHeader));
    return Product.builder().GUID(productId).name("ACME Super Product").build();
  }

  @RequestMapping(value = "/getWithPrice/{productId}", method = RequestMethod.GET)
  public @ResponseBody
  Product getGoodAndPrice(@PathVariable("productId") Long productId,
      @RequestHeader(value="Authorization") String authorizationHeader,
      Principal currentUser) {
    log.info(String.format("ProductApi: User=%s, Auth=%s", currentUser.getName(), authorizationHeader));
    Price price = getPriceForGood(productId, authorizationHeader);
    return Product.builder().GUID(productId).name("ACME Expensive Product").price(price).build();
  }

  private Price getPriceForGood(
      @PathVariable("productId") Long productId,
      @RequestHeader(value = "Authorization") String authorizationHeader) {
    HashMap<String, Object> params = new HashMap<>();
    params.put("productId", productId);
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.set("Authorization", authorizationHeader);
    RestTemplate restTemplate = new RestTemplate();
    return restTemplate.exchange("http://localhost:7778/get/price/{productId}", HttpMethod.GET,
        new HttpEntity(httpHeaders), Price.class, params).getBody();
  }
}
