package in.arbii.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.arbii.request.SearchRequest;
import in.arbii.response.SearchResponse;
import in.arbii.service.ReportService;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin
public class ReportRestController {

	@Autowired
	private ReportService service;
	
	@GetMapping("/plans")
	public ResponseEntity<List<String>> getPlanNames(){
		List<String> planNames = service.getUniquePlanNames();
		return new ResponseEntity<>(planNames, HttpStatus.OK);
	}
	
	@GetMapping("/statuses")
	public ResponseEntity<List<String>> getplanStatuses(){
		List<String> planStatuses = service.getUniquePlanStatuses();
		return new ResponseEntity<>(planStatuses, HttpStatus.OK);
	}
	
	
	@PostMapping("/search")
	public ResponseEntity<List<SearchResponse>> search(@RequestBody SearchRequest request){
		List<SearchResponse> response  = service.search(request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/excel")
	public void excelReport(HttpServletResponse response) throws Exception{
		
		response.setContentType("application/octet-stream");
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment;filename=report.xls";
		
		response.setHeader(headerKey, headerValue);
		
		service.generateExcel(response);
	}
	
	 @GetMapping("/pdf")
	    public void pdfReport(HttpServletResponse response) throws Exception{
	        
		 	response.setContentType("application/pdf");
	        
	        String headerKey = "Content-Disposition";
	        String headerValue = "attachment; filename=report.pdf";
	        
	        response.setHeader(headerKey, headerValue);
	        
	        service.generatePdf(response);
	         
	    }
}
