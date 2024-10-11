package in.arbii.repo;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import in.arbii.entity.DcCaseEntity;

public interface DcCaseRepo extends JpaRepository<DcCaseEntity, Serializable> {
	
	public DcCaseEntity findByAppId(Integer appId);
}
