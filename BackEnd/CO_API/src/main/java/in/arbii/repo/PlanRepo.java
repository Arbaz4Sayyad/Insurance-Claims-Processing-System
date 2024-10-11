package in.arbii.repo;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import in.arbii.entity.PlanEntity;

public interface PlanRepo extends JpaRepository<PlanEntity, Serializable>{

}
