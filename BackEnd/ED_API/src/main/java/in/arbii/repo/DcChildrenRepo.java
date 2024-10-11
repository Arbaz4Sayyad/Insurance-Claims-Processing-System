package in.arbii.repo;


import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.arbii.entity.DcChildrenEntity;

public interface DcChildrenRepo extends JpaRepository<DcChildrenEntity, Serializable> {

	public List<DcChildrenEntity> findByCaseNum(Long caseNum);
}

