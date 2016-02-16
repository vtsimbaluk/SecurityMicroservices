package com.nix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class PriceServiceApplication {

	@RequestMapping(value = "/get/price/{productId}", method = RequestMethod.GET)
	public @ResponseBody Price getGood(@PathVariable("productId") Long productId) {
		return Price.builder().GUID(productId).price(1000000D).discount(1D).build();
	}

	@Configuration
	@EnableResourceServer
	protected static class ResourceServer extends ResourceServerConfigurerAdapter
	{
		@Override
		public void configure(HttpSecurity http) throws Exception {
			http.antMatcher("/**").authorizeRequests().anyRequest().access("#oauth2.hasScope('openid')");
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(PriceServiceApplication.class, args);
	}
}
