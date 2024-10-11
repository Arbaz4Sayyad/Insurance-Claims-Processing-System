package in.arbii.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.arbii.bindings.CitizenApp;
import in.arbii.service.ArService;

@RestController
public class ArRestController {
	
	@Autowired
	private ArService service;
	
	@PostMapping("/app")
	public ResponseEntity<String> createCitizenApp(@RequestBody CitizenApp app){
		
		Integer appId = service.createApplication(app);
		
		if(appId > 0) {
			return new ResponseEntity<>("App created with App Id : " + appId, HttpStatus.OK);
		}else {
			return new ResponseEntity<>("Invalid SSN : ", HttpStatus.BAD_REQUEST);
		}	
	}
}
