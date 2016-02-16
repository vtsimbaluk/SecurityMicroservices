package com.nix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.security.oauth2.resource.EnableOAuth2Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@SpringBootApplication
@EnableOAuth2Resource
public class ProductServiceApplication {

	@Configuration
	@EnableResourceServer
	protected static class ResourceServer extends ResourceServerConfigurerAdapter {
		@Override
		public void configure(HttpSecurity http) throws Exception {
//			http.antMatcher("/**").authorizeRequests().anyRequest().access("#oauth2.hasScope('openid')");
			http.antMatcher("/**").authorizeRequests().anyRequest().hasRole("USER");
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}
}
