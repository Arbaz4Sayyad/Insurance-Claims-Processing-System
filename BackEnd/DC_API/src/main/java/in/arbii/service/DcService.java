package in.arbii.service;

import java.util.Map;

import in.arbii.bindings.ChildRequest;
import in.arbii.bindings.DcSummary;
import in.arbii.bindings.Education;
import in.arbii.bindings.Income;
import in.arbii.bindings.PlanSelection;


public interface DcService {
	
	public Long loadCaseNum(Integer appId);
	
	public Map<Integer, String> getPlanNames();
	
	public Long savePlanSelection(PlanSelection planSelection );
	
	public Long saveIncomeData(Income income);
	
	public Long saveEducation(Education education);
	
	public Long saveChildrens(ChildRequest request);
	
	public DcSummary getSummary(Long caseNumber);
}
