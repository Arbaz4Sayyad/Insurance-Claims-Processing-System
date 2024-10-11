package in.arbii.repo;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import in.arbii.entity.EligDetailsEntity;

public interface EligDetailsRepo extends JpaRepository<EligDetailsEntity, Serializable>{

	public EligDetailsEntity findByCaseNum(Long caseNum);
}
