package in.arbii.repo;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import in.arbii.entity.CoTriggerEntity;

public interface CoTriggerRepo extends JpaRepository<CoTriggerEntity, Serializable>{

}
