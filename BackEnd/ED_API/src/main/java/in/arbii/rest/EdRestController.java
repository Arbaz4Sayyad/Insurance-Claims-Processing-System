package in.arbii.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import in.arbii.response.EligResponse;
import in.arbii.service.EligService;

@RestController
public class EdRestController {
	
	@Autowired
	private EligService service;
	
	@GetMapping("/eligibility/{caseNum}")
	public EligResponse determineEligibility(@PathVariable Long caseNum) {
		
		EligResponse eligResponse = service.determineEligibility(caseNum);
		return eligResponse;
	}
}
