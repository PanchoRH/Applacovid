/**
 * Computer Department at Cinvestav CDMX.
 * Copyright (c) 2020
 * All rights reserved.
 */

package mx.cinvestav.cs.applacovid.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;


/**
 * Home Controller.
 */
@Controller
public class HomeController
{
	@GetMapping("/")
	public String index(Principal principal)
	{
		String homeView;

		if (principal != null)
		{
			homeView = "redirect:/console/";
		}
		else
		{
			homeView = "home/signedin";
		}

		return homeView;
	}
}
