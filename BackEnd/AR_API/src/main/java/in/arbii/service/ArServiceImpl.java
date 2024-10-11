package in.arbii.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
//import org.springframework.web.reactive.function.client.WebClient;

import in.arbii.bindings.CitizenApp;
import in.arbii.entity.CitizenAppEntity;
import in.arbii.repo.CitizenAppRepository;

@Service
public class ArServiceImpl implements ArService{

	@Autowired
	private CitizenAppRepository appRepo;
	
	@Override
	public Integer createApplication(CitizenApp app) {
		
		// make rest call to ssa-web api with ssn as input
		// https://www.ssnvalidator.com/ssn/{ssn}
		
		String endpointUrl = "https://ssa-web-api.herokuapp.com/ssn/{ssn}";
		
		// Using Rest Template
		
		  RestTemplate rt = new RestTemplate();
		  
		  ResponseEntity<String> resEntity = 
				  rt.getForEntity(endpointUrl, String.class, app.getSsn());
		  
		  String stateName = resEntity.getBody();
		 
		
		
		// Using Web Client
		/*
		 * WebClient webClient = WebClient.create(); String stateName = webClient.get()
		 * // it represents GET request .uri(endpointUrl, app.getSsn()) // it represents
		 * to which url request to be sent .retrieve() // to retrieve response
		 * .bodyToMono(String.class) // to specify response type .block();
		 */               // to make synchronous call

		if("New Jersey".equals(stateName)) {
			// create application 
			
			CitizenAppEntity entity = new CitizenAppEntity();
			BeanUtils.copyProperties(app, entity);
			
			entity.setStateName(stateName);
			
			CitizenAppEntity save = appRepo.save(entity);
			
			return save.getApp_id();
		}
		return 0;
	}
}


/*
package in.arbii.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import in.arbii.bindings.CitizenApp;
import in.arbii.entity.CitizenAppEntity;
import in.arbii.repo.CitizenAppRepository;

@Service
public class ArServiceImpl implements ArService{

	@Autowired
	private CitizenAppRepository appRepo;
	
	@Override
	public Integer createApplication(CitizenApp app) {
		
		// make rest call to ssa-web api with ssn as input
		// https://www.ssnvalidator.com/ssn/{ssn}
		
		String endpointUrl = "https://ssa-web-api.herokuapp.com/ssn/{ssn}";
		
		RestTemplate rt = new RestTemplate();
		
		// Using Headers
		/*
		 * HttpHeaders headers = new HttpHeader(); 
		 * headers.set("Accept", "application/json"); 
		 * HttpEntity entity = new HttpEntity<>(headers);
		 * rt.exchange(endpointUrl, HttpMethod.GET, entity, String.class, app.getSsn());
		 */
		
/*		
		ResponseEntity<String> resEntity = 
				rt.getForEntity(endpointUrl, String.class, app.getSsn());
		
		String stateName = resEntity.getBody();
		
		if("New Jersey".equals(stateName)) {
			// create application 
			
			CitizenAppEntity entity = new CitizenAppEntity();
			BeanUtils.copyProperties(app, entity);
			
			entity.setStateName(stateName);
			
			CitizenAppEntity save = appRepo.save(entity);
			
			return save.getApp_id();
		}
		return 0;
	}
}
*/