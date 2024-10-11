package in.arbii.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import in.arbii.entity.Plan;

public interface PlanRepo extends JpaRepository<Plan, Integer>{

}
