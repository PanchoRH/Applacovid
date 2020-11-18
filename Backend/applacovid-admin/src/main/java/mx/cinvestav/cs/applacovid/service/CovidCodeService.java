/**
 * Computer Department at Cinvestav CDMX.
 * Copyright (c) 2020
 * All rights reserved.
 */

package mx.cinvestav.cs.applacovid.service;


import mx.cinvestav.cs.applacovid.jpa.CovidCode;


public interface CovidCodeService
{
	void saveCovidCode(CovidCode covidCode);
}
