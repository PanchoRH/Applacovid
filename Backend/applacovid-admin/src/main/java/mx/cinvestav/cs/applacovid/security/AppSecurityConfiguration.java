/**
 * Computer Department at Cinvestav CDMX.
 * Copyright (c) 2020
 * All rights reserved.
 */

package mx.cinvestav.cs.applacovid.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


/**
 * The security configuration for the application.
 */
@Configuration
public class AppSecurityConfiguration extends WebSecurityConfigurerAdapter
{
	public AppSecurityConfiguration(UserDetailsService userDetailsService,
		PasswordEncoder passwordEncoder)
	{
		this.userDetailsService = userDetailsService;
		this.passwordEncoder = passwordEncoder;
	}


	/**
	 * This method returns the authentication success handler.
	 *
	 * @return The {@link AuthenticationSuccessHandler} instance for this application.
	 */
	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler()
	{
		return new mx.cinvestav.cs.applacovid.security.AppAuthenticationSuccessHandler();
	}


	/**
	 * Sets up the authentication service to use our account service and password encoder.
	 * @param auth The {@link AuthenticationManagerBuilder} instance.
	 * @throws Exception Thrown if an error occurs.
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception
	{
		auth.eraseCredentials(false);
		auth.userDetailsService(this.userDetailsService).
			passwordEncoder(this.passwordEncoder);
	}


	/**
	 * Configures the {@link HttpSecurity} instance.
	 *
	 * @param httpSecurity The {@link HttpSecurity} instance to configure.
	 * @throws Exception Thrown if an error occurs configuring security.
	 */
	protected void configure(HttpSecurity httpSecurity) throws Exception
	{
		httpSecurity.
			authorizeRequests().
			antMatchers(
				//Resources
				"/images/**",
				"/js/**",
				"/css/**",
				"/webjars/**",
				"/",
				"/ios/**",
				"/api/**").
			permitAll().
			anyRequest().
			authenticated().
			and().
			formLogin().
			loginPage("/security/signin").permitAll().
			loginProcessingUrl("/security/signin").
			failureUrl("/security/signin?failed=true").
			// TODO: Review how to pass parameters through handler.
			//successHandler(this.authenticationSuccessHandler()).
			and().
			logout().
			logoutUrl("/security/logout").permitAll().
			logoutSuccessUrl("/security/signin").
			deleteCookies("JSESSIONID").
			and().
			exceptionHandling().
			accessDeniedPage("/security/accessdenied").
			and().
			headers().frameOptions().disable();
	}


	/**
	 * The {@link UserDetailsService} for the application.
	 */
	UserDetailsService userDetailsService;

	/**
	 * The {@link PasswordEncoder} for the application.
	 */
	PasswordEncoder passwordEncoder;
}
