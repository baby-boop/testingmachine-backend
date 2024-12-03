package testingmachine_backend.process.Controller;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemRepository extends JpaRepository<SystemData, Long> {
}