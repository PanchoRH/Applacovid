/**
 * Computer Department at Cinvestav CDMX.
 * Copyright (c) 2020
 * All rights reserved.
 */

package mx.cinvestav.cs.applacovid.service;


import mx.cinvestav.cs.applacovid.repository.CovidCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import mx.cinvestav.cs.applacovid.jpa.CovidCode;
import org.springframework.stereotype.Service;


@Service
public class CovidCodeServiceImpl implements CovidCodeService
{
	@Override
	public void saveCovidCode(CovidCode covidCode)
	{
		covidCodeRepository.save(covidCode);
	}


	@Autowired
	CovidCodeRepository covidCodeRepository;
}
