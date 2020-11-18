package mx.cinvestav.cs.applacovid.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.cinvestav.cs.applacovid.jpa.Device;
import mx.cinvestav.cs.applacovid.repository.DeviceRepository;

@Service
public class DeviceServiceImpl implements DeviceService {
	
	@Autowired
	private DeviceRepository deviceRepository;

	@Override
	public Long addDevice(Device device) {
		return deviceRepository.save(device).getId();
	}

}
