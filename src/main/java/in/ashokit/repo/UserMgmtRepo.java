package in.ashokit.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.ashokit.entity.UserMaster;

public interface UserMgmtRepo extends JpaRepository<UserMaster, Integer> {
	
}
