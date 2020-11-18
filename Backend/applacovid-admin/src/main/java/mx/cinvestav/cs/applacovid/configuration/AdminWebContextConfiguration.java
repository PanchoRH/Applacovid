/**
 * Computer Department at Cinvestav CDMX.
 * Copyright (c) 2020
 * All rights reserved.
 */

package mx.cinvestav.cs.applacovid.configuration;


import mx.cinvestav.cs.applacovid.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.util.concurrent.TimeUnit;


/**
 * Provides auto configuration of web application server container resources for a
 * Web Application in a Spring friendly manner.
 */
@Configuration
public class AdminWebContextConfiguration
{
	/**
	 * Enable Spring Security session registry notifications.
	 * @return {@link HttpSessionEventPublisher} instance.
	 */
	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher()
	{
		return new HttpSessionEventPublisher();
	}


	@Bean
	public RestTemplate restTemplate()
	{
		final int timeout = (int) TimeUnit.SECONDS.toMillis(3);
		HttpComponentsClientHttpRequestFactory httpRequestFactory =
			new HttpComponentsClientHttpRequestFactory();
		httpRequestFactory.setConnectionRequestTimeout(timeout);
		httpRequestFactory.setConnectTimeout(timeout);
		httpRequestFactory.setReadTimeout(timeout);

		RestTemplate restTemplate = new RestTemplate(httpRequestFactory);

		return restTemplate;
	}


	@Bean
	public ValidationUtils dpptValidationUtils()
	{
		return new ValidationUtils(keySizeBytes);
	}


	/**
	 * Injected {@link WebApplicationContext} field.
	 */
	@Autowired
	WebApplicationContext webApplicationContext;


	@Value("${ws.app.key_size: 32}")
	int keySizeBytes;
}
