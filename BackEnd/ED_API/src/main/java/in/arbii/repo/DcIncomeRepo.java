package in.arbii.repo;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import in.arbii.entity.DcIncomeEntity;

public interface DcIncomeRepo extends JpaRepository<DcIncomeEntity, Serializable>{

	public DcIncomeEntity findByCaseNum(Long caseNum);
}
