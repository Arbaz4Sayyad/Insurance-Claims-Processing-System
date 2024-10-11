package in.arbii.repo;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import in.arbii.entity.DcEducationEntity;

public interface DcEducationRepo extends JpaRepository<DcEducationEntity, Serializable>{
	
	public DcEducationEntity findByCaseNum(Long caseNum);
}
