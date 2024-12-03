package testingmachine_backend.process.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SystemService {

    @Autowired
    private SystemRepository repository;

    public List<SystemData> getAllSystemData() {
        return repository.findAll();
    }

    public SystemData addSystemData(SystemData data) {
        return repository.save(data);
    }

    public void deleteSystemData(Long id) {
        repository.deleteById(id);
    }
}
