/**
 * Computer Department at Cinvestav CDMX.
 * Copyright (c) 2020
 * All rights reserved.
 */

package mx.cinvestav.cs.applacovid.repository;


import mx.cinvestav.cs.applacovid.jpa.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface DeviceRepository extends JpaRepository<Device, Long>
{
	
}
