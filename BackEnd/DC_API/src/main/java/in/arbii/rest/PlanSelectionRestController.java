package in.arbii.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import in.arbii.bindings.PlanSelection;
import in.arbii.service.DcService;

@RestController
public class PlanSelectionRestController {
	
	@Autowired
	private DcService service;
	
	@PostMapping("/plansel")
	public ResponseEntity<Long> planSelection(PlanSelection planSel) {
		
		Long caseNum = service.savePlanSelection(planSel);
		
		return new ResponseEntity<>(caseNum, HttpStatus.CREATED);
		
	}
}
