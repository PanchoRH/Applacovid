/**
 * Computer Department at Cinvestav CDMX.
 * Copyright (c) 2020
 * All rights reserved.
 */

package mx.cinvestav.cs.applacovid.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.MalformedURLException;


/**
 * Security controller.
 */
@Controller
public class SecurityController
{
	/**
	 * Handler method for Spring Security's custom Login page.
	 *
	 * @return the login view name.
	 */
	@RequestMapping("/security/signin")
	String toSignIn(Model model, @RequestParam(required = false) boolean failed)
		throws MalformedURLException
	{
		return "home/signedin";
	}
}
