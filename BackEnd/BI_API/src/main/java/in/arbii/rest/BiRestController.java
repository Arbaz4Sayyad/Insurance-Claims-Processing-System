package in.arbii.rest;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.arbii.entity.BiEntity;
import in.arbii.repo.BiRepository;
import in.arbii.service.BiService;

@RestController
@RequestMapping("/benefit-issuance")
public class BiRestController {

	@Autowired
	private BiService service;
	
	 @Autowired
	 private BiRepository citizenRepository;

	@GetMapping("/approved")
	public ResponseEntity<List<BiEntity>> getApprovedCitizens() {
		List<BiEntity> citizens = service.getApprovedCitizens();
		return ResponseEntity.ok(citizens);
	}

	@PostMapping("/process-benefit")
	public ResponseEntity<String> processBenefits() {
		try {
			service.processBenefits();
			return ResponseEntity.ok("Benefit issuance process started successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error occurred while processing benefits: " + e.getMessage());
		}
	}
	
	@GetMapping("/file")
	public void getFile() throws IOException {
		service.generateCsvFile());
	}
}
