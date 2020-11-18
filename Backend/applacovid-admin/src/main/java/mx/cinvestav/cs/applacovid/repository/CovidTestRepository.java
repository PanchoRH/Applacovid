/**
 * Computer Department at Cinvestav CDMX.
 * Copyright (c) 2020
 * All rights reserved.
 */

package mx.cinvestav.cs.applacovid.repository;


import mx.cinvestav.cs.applacovid.jpa.CovidTestImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CovidTestRepository extends JpaRepository<CovidTestImpl, Long>
{
	List<CovidTestImpl> findAllByIsExposedIsFalse();


	List<CovidTestImpl> findAllByIsExposedIsTrue();


	Optional<CovidTestImpl> findByKd(String kd);
}
