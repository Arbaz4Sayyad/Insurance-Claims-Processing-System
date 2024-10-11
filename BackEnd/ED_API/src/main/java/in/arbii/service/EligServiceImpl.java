package in.arbii.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.arbii.entity.CitizenAppEntity;
import in.arbii.entity.CoTriggerEntity;
import in.arbii.entity.DcCaseEntity;
import in.arbii.entity.DcChildrenEntity;
import in.arbii.entity.DcEducationEntity;
import in.arbii.entity.DcIncomeEntity;
import in.arbii.entity.EligDetailsEntity;
import in.arbii.entity.PlanEntity;
import in.arbii.repo.CitizenAppRepository;
import in.arbii.repo.CoTriggerRepo;
import in.arbii.repo.DcCaseRepo;
import in.arbii.repo.DcChildrenRepo;
import in.arbii.repo.DcEducationRepo;
import in.arbii.repo.DcIncomeRepo;
import in.arbii.repo.EligDetailsRepo;
import in.arbii.repo.PlanRepo;
import in.arbii.response.EligResponse;

@Service
public class EligServiceImpl implements EligService {

	@Autowired
	private DcCaseRepo dcCaseRepo;

	@Autowired
	private PlanRepo planRepo;

	@Autowired
	private DcIncomeRepo incomeRepo;

	@Autowired
	private DcChildrenRepo childRepo;

	@Autowired
	private CitizenAppRepository appRepo;

	@Autowired
	private DcEducationRepo eduRepo;

	@Autowired
	private EligDetailsRepo eligRepo;
	
	@Autowired
	private CoTriggerRepo coTrgRepo;

	@Override
	public EligResponse determineEligibility(Long caseNum) {

		Optional<DcCaseEntity> caseEntity = dcCaseRepo.findById(caseNum);
		Integer planId = null;
		String planName = null;
		Integer appId = null;

		if (caseEntity.isPresent()) {
			DcCaseEntity dcCaseEntity = caseEntity.get();
			planId = dcCaseEntity.getPlanId();
			appId = dcCaseEntity.getAppId();
		}

		Optional<PlanEntity> planEntity = planRepo.findById(planId);

		if (planEntity.isPresent()) {
			PlanEntity plan = planEntity.get();
			planName = plan.getPlanName();
		}

		Optional<CitizenAppEntity> app = appRepo.findById(appId);
		Integer age = 0;
		CitizenAppEntity citizenAppEntity = null;
		if (app.isPresent()) {
			citizenAppEntity = app.get();
			LocalDate dob = citizenAppEntity.getDob();
			LocalDate now = LocalDate.now();
			age = Period.between(dob, now).getYears();
		}

		EligResponse eligResponse = executePlanConditions(caseNum, planName, age);

		// logic to store data in db
		EligDetailsEntity eligEntity = new EligDetailsEntity();
		BeanUtils.copyProperties(eligResponse, eligEntity);

		eligEntity.setCaseNum(caseNum);
		eligEntity.setHolderName(citizenAppEntity.getFullName());
		eligEntity.setHolderSsn(citizenAppEntity.getSsn());

		eligRepo.save(eligEntity);
		
		CoTriggerEntity coEntity = new CoTriggerEntity();
		
		coEntity.setCaseNum(caseNum);
		coEntity.setTrgStatus("Pending");
		
		coTrgRepo.save(coEntity);
		
		return eligResponse;
	}

	private EligResponse executePlanConditions(Long caseNum, String planName, Integer age) {

		EligResponse response = new EligResponse();
		response.setPlanName(planName);
		
		DcIncomeEntity income = incomeRepo.findByCaseNum(caseNum);
		if("SNAP".equals(planName)) {
			
			Double empIncome = income.getEmpIncome();
			if(empIncome <= 300) {
				response.setPlanStatus("APPROVED");
			}else {
				response.setPlanStatus("DENIED");
				response.setDenialReason("Low Income");
			}
			
		}else if("CCAP".equals(planName)) {
			
			boolean ageCondition = true;
			boolean kidsCountCondition = false;
			
			List<DcChildrenEntity> childs = childRepo.findByCaseNum(caseNum);
	
			if(!childs.isEmpty()) {
				kidsCountCondition = true;
				for(DcChildrenEntity entity : childs) {
					Integer childAge = entity.getChilAge();
					if(childAge > 16) {
						ageCondition = false;
						break;
					}
				}
			}
			if(income.getEmpIncome() <= 300 && kidsCountCondition && ageCondition) {
				response.setPlanStatus("APPROVED");
			}else {
				response.setPlanStatus("DENIED");
				response.setDenialReason("Business rule not satisfied");
			}
			
		}else if("Medicaid".equals(planName)) {
			
			Double empIncome = income.getEmpIncome();
			Double propertyIncome = income.getPropertIncome();
			
			if(empIncome<=300 && propertyIncome == 0) {
				response.setPlanStatus("APPROVED");
			}else {
				response.setPlanStatus("DENIED");
				response.setDenialReason("Low Income");
			}
			
		}else if("Medicare".equals(planName)) {
			if(age >=65) {
				response.setPlanStatus("APPROVED");
			}else {
				response.setPlanStatus("DENIED");
				response.setDenialReason("High Age");
			}
		}
			
		else if("NJW".equals(planName)){
			DcEducationEntity educationEntity = eduRepo.findByCaseNum(caseNum);
			Integer graduationYear = educationEntity.getGraduationYear();
			int currYear = LocalDate.now().getYear();

			if (income.getEmpIncome() <= 0 && graduationYear < currYear) {
				response.setPlanStatus("APPROVED");
			} else {
				response.setPlanStatus("DENIED");
				response.setDenialReason("Rules not satisfied");
			}
	}

	if(response.getPlanStatus().equals("APPROVED"))
	{
		response.setPlanStartDate(LocalDate.now());
		response.setPlanEndDate(LocalDate.now().plusMonths(6));
		response.setBenefitAmt(350.00);
	}

	return response;
}}
