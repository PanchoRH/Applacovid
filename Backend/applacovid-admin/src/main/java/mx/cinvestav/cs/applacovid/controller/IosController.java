package mx.cinvestav.cs.applacovid.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import mx.cinvestav.cs.applacovid.jpa.Device;
import mx.cinvestav.cs.applacovid.service.DeviceService;

@Controller
@RequestMapping("/ios")
public class IosController {
	
	@Autowired
	private DeviceService deviceService;

	@GetMapping("/form")
	public String from(HttpServletRequest request, ModelMap model) {	
		return "ios/registerForm";
	}
	
	@PostMapping("/add")
	public String add(@Valid Device device, BindingResult result, ModelMap model) {
		
		if(result.hasErrors()) {
			
			return "ios/registerForm";
		}
		
		deviceService.addDevice(device);
		
		return "ios/success";
	}
}
