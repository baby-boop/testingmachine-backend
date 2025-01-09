package testingmachine_backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import testingmachine_backend.controller.JsonController;
import testingmachine_backend.meta.MetaList.MetaMain;

import testingmachine_backend.metaWithProcess.MetaWithProcessMain;
import testingmachine_backend.patch.patchMain;
import testingmachine_backend.process.MainProcess;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@EnableScheduling
@SpringBootApplication
public class TestingmachineBackendApplication
		implements CommandLineRunner
{
	public static void main(String[] args) {
		SpringApplication.run(TestingmachineBackendApplication.class, args);
	}

	private final ExecutorService executor = Executors.newCachedThreadPool();

	@Override
	public void run(String... args) {
		if (args.length > 0) {
			String module = args[0];
			executor.submit(() -> {

				try {
					System.out.println("Current Thread: " + Thread.currentThread().getName() + ", ID: " + Thread.currentThread().getId());
					String result = executeModule(module);
					System.out.println("result: " + result);
				} catch (Exception e) {
					System.err.println("Модуль ажиллуулахад алдаа гарлаа: " + e.getMessage());
				}
			});
		} else {
			System.out.println("Модуль олдсонгүй.");
		}
	}
	public String executeModule(String module) {
		String result;
		switch (module) {
			case "meta":
				System.out.println("Тест эхлэж байна... meta");
				MetaMain.mainSystem(JsonController.getJsonId(), String.valueOf(Thread.currentThread().getId()), JsonController.getCustomerName(),
						JsonController.getCreatedDate(), JsonController.getModuleId(), JsonController.getDatabaseName(), JsonController.getDatabaseUsername(),
						JsonController.getSystemURL(), JsonController.getUsername(), JsonController.getPassword());
				result = "Тест хийж дууссан";
				break;
			case "process":
				System.out.println("Тест эхлэж байна... process");
				MainProcess.mainProcess(JsonController.getJsonId(), String.valueOf(Thread.currentThread().getId()), JsonController.getCustomerName(),
						JsonController.getCreatedDate(), JsonController.getModuleId(), JsonController.getDatabaseName(), JsonController.getDatabaseUsername(),
						JsonController.getSystemURL(), JsonController.getUsername(), JsonController.getPassword(), JsonController.getMetaOrPatchId());
				result = "Тест хийж дууссан";
				break;
			case "metaWithProcess":
				System.out.println("Тест эхлэж байна... metaWithProcess");
				MetaWithProcessMain.mainProcess(JsonController.getJsonId());
				result = "Тест хийж дууссан";
				break;
			case "patch":
				System.out.println("Patch тест эхлэж байна");
				patchMain.mainProcess(JsonController.getJsonId(), String.valueOf(Thread.currentThread().getId()), JsonController.getCustomerName(),
						JsonController.getCreatedDate(), JsonController.getModuleId(), JsonController.getDatabaseName(), JsonController.getDatabaseUsername(),
						JsonController.getSystemURL(), JsonController.getUsername(), JsonController.getPassword(), JsonController.getMetaOrPatchId());
				result = "Тест хийж дууссан";
				break;
			default:
				throw new IllegalArgumentException("Модуль олдсонгүй");
		}
		return result;
	}
}

