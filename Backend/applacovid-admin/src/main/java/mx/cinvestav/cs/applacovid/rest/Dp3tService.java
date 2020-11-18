/**
 * Computer Department at Cinvestav CDMX.
 * Copyright (c) 2020
 * All rights reserved.
 */

package mx.cinvestav.cs.applacovid.rest;


import mx.cinvestav.cs.applacovid.jpa.ExposeeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


@Service
public class Dp3tService
{
	public Dp3tService(@Autowired RestTemplate restTemplate)
	{
		this.restTemplate = restTemplate;
	}

	public boolean postExposed(ExposeeRequest exposeeRequest)
	{
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		boolean response;

		try
		{
			final ResponseEntity<String> responseEntity =
				this.restTemplate.exchange(
					exposedUrl,
					HttpMethod.POST,
					new HttpEntity<>(exposeeRequest, headers),
					String.class);

			HttpStatus resStatus = responseEntity.getStatusCode();

			logger.debug(
				"Exposing user with key {} and keyDate {} to end-point {} : Response status {}.",
				exposeeRequest.getKey(), exposeeRequest.getKeyDate(), exposedUrl,
				resStatus);

			response = true;
		}
		catch (HttpClientErrorException e)
		{
			logger.debug(
				"Failed exposing key {} and keyDate {}. Response status {} - {}.",
				exposeeRequest.getKey(), exposeeRequest.getKeyDate(), e.getStatusCode(), e.getMessage());

			response = false;
		}

		return response;
	}


	Logger logger = LoggerFactory.getLogger(Dp3tService.class);

	@Value("${dp3t.exposed.url:}")
	String exposedUrl;

	protected final RestTemplate restTemplate;
}
