/**
 * Computer Department at Cinvestav CDMX.
 * Copyright (c) 2020
 * All rights reserved.
 */

package mx.cinvestav.cs.applacovid.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;


/**
 * Shared security configuration for all services.
 */
@Configuration
public class ApplacovidSecurityConfiguration
{
	/**
	 * Defines the password encoder used by the application.
	 *
	 * @return The password encoder used by the application.
	 */
	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}


	/**
	 * Temporary default simple user details service.
	 *
	 * @return The user details service.
	 * @throws Exception Throws an error if required.
	 */
	@Bean
	public UserDetailsService inMemoryUserDetailsService() throws Exception
	{
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

		manager.createUser(User.withUsername("admin").password(passwordEncoder().
			encode("adM1#ni23!")).roles("ADMIN").build());

		manager.createUser(User.withUsername("areyes").password(passwordEncoder().
			encode("areyes123!")).roles("ADMIN").build());

		return manager;
	}
}
