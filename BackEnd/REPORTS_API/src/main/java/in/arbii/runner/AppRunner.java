package in.arbii.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import in.arbii.entity.EligibilityDetails;
import in.arbii.repo.EligibilityDetailsRepo;

@Component
public class AppRunner implements ApplicationRunner {

	@Autowired
	private EligibilityDetailsRepo repo;

	@Override
	public void run(ApplicationArguments args) throws Exception {

		EligibilityDetails entity1 = new EligibilityDetails();
		entity1.setEligId(1);
		entity1.setName("John");
		entity1.setEmail("john@gmail.com");
		entity1.setMobile(9834239709l);
		entity1.setGender('M');
		entity1.setSsn(68686868l);
		entity1.setPlanName("SNAP");
		entity1.setPlanStatus("Approved");
		repo.save(entity1);

		EligibilityDetails entity2 = new EligibilityDetails();
		entity2.setEligId(2);
		entity2.setName("Smith");
		entity2.setEmail("smith@gmail.com");
		entity2.setMobile(9689249243l);
		entity2.setGender('M');
		entity2.setSsn(78787878l);
		entity2.setPlanName("CCAP");
		entity2.setPlanStatus("Denied");
		repo.save(entity2);
		
		EligibilityDetails entity3 = new EligibilityDetails();
		entity3.setEligId(3);
		entity3.setName("Robert");
		entity3.setEmail("robert@gmail.com");
		entity3.setMobile(9021396605l);
		entity3.setGender('M');
		entity3.setSsn(88888888l);
		entity3.setPlanName("Medicaid");
		entity3.setPlanStatus("Closed");
		repo.save(entity3);

	}
}
