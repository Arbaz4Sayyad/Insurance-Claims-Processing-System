package in.arbii.repo;

import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaRepository;
import in.arbii.entity.CitizenAppEntity;

public interface CitizenAppRepository extends JpaRepository<CitizenAppEntity, Serializable>{

}
