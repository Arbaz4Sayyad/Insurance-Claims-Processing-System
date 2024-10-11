package in.arbii.service;

import in.arbii.response.EligResponse;

public interface EligService {

	public EligResponse determineEligibility(Long caseNum);
}
