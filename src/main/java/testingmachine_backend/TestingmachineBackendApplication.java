package testingmachine_backend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import testingmachine_backend.controller.JsonController;
import testingmachine_backend.schedule.JsonFileCleanerSchedule;

import java.io.File;

@EnableScheduling
@EnableAsync
@SpringBootApplication
@Slf4j
public class TestingmachineBackendApplication
{
	public static void main(String[] args) {
		SpringApplication.run(TestingmachineBackendApplication.class, args);
	}

}

