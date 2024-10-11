package in.arbii.repo;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.arbii.entity.CoTriggerEntity;

public interface CoTriggerRepo extends JpaRepository<CoTriggerEntity, Serializable>{

	public List<CoTriggerEntity> findByTrgStatus(String status);

	public CoTriggerEntity findByCaseNum(Long caseNum);
}
