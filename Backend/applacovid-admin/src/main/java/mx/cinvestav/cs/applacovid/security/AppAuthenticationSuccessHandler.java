/**
 * Computer Department at Cinvestav CDMX.
 * Copyright (c) 2020
 * All rights reserved.
 */

package mx.cinvestav.cs.applacovid.security;


import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;


/**
 * This class handles authentication success.
 */
@Getter
@Setter
public class AppAuthenticationSuccessHandler implements AuthenticationSuccessHandler
{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
		HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException
	{
		this.handle(request, response, authentication);
		this.clearAuthenticationAttributes(request);
	}

	/**
	 * Processes the first phase of authentication success.
	 * @param request The {@link HttpServletRequest} instance.
	 * @param response The {@link HttpServletResponse} instance.
	 * @param authentication The {@link Authentication} instance.
	 * @throws IOException Thrown if an error occurs.
	 */
	protected void handle(HttpServletRequest request,
		HttpServletResponse response,
		Authentication authentication)
		throws IOException
	{
		String targetUrl = determineTargetUrl(authentication);

		if (response.isCommitted())
		{
			logger.debug("Response has already been committed. Unable to redirect to {}",
				targetUrl);
			return;
		}

		redirectStrategy.sendRedirect(request, response, targetUrl);
	}

	/**
	 * Determines the target URL based on the logged in user.
	 * @param authentication The {@link Authentication} instance.
	 * @return The URI for the target URL.
	 */
	protected String determineTargetUrl(Authentication authentication)
	{
		String targetURL = "/";;
		Collection<? extends GrantedAuthority> authorities =
			authentication.getAuthorities();

		// TODO: review this part
		for (GrantedAuthority grantedAuthority : authorities)
		{
			if (grantedAuthority.getAuthority().equals("ROLE_USER"))
			{
				targetURL = "/console/";
				break;
			}
			else if (grantedAuthority.getAuthority().equals("ROLE_ADMIN"))
			{
				targetURL = "/console/newrecord";
				break;
			}
		}

		//targetURL = "/";

		return targetURL;
	}

	/**
	 * Clears authentication attribute.
	 * @param request The {@link HttpServletRequest}.
	 */
	protected void clearAuthenticationAttributes(HttpServletRequest request)
	{
		HttpSession session = request.getSession(false);

		if (session == null)
		{
			return;
		}

		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}


	Logger logger = LoggerFactory.getLogger(AppAuthenticationSuccessHandler.class);

	/**
	 * The {@link RedirectStrategy} field.
	 */
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
}
