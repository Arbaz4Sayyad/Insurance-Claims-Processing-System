package in.arbii.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.arbii.entity.BiEntity;

public interface BiRepository extends JpaRepository<BiEntity, Long> {
    static List<BiEntity> findByStatus(String string) {
		// TODO Auto-generated method stub
		return null;
	}
}
