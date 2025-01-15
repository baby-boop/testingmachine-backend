package testingmachine_backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import testingmachine_backend.controller.JsonController;
import testingmachine_backend.meta.MetaList.MetaMain;

import testingmachine_backend.metaWithProcess.MetaWithProcessMain;
import testingmachine_backend.patch.patchMain;

import testingmachine_backend.process.Controller.SystemData;
import testingmachine_backend.process.MainProcess;

import java.util.concurrent.*;

@EnableScheduling
@EnableAsync
@SpringBootApplication
public class TestingmachineBackendApplication
		implements CommandLineRunner
{

	private final int corePoolSize = Runtime.getRuntime().availableProcessors(); /* Системийн хүчин чадлаас хамаарч автоматаар зохицуулалт хийнэ */
	private final int maximumPoolSize = corePoolSize * 2;
	private final int queueCapacity = 100;

	private final ThreadPoolExecutor executor = new ThreadPoolExecutor(
			corePoolSize,
			maximumPoolSize,
			60, TimeUnit.SECONDS,
			new LinkedBlockingQueue<>(queueCapacity),
			new ThreadPoolExecutor.CallerRunsPolicy()
	);

	public static void main(String[] args) {
		SpringApplication.run(TestingmachineBackendApplication.class, args);
	}

	@Override
	public void run(String... args) {
		if (args.length > 0) {
			String module = args[0];
			SystemData systemData = new SystemData();
			executor.submit(() -> {
				try {
					System.out.println("Current Thread: " + Thread.currentThread().getName() + ", ID: " + Thread.currentThread().getId());

					String result = executeModule(module, systemData);
					System.out.println("result: " + result);
				} catch (Exception e) {
					System.err.println("Модуль ажиллуулахад алдаа гарлаа: " + e.getMessage());
				}
			});
		} else {
			System.out.println("Модуль олдсонгүй.");
		}
	}
	public String executeModule(String module, SystemData systemData) {
		String result;
		switch (module) {
			case "meta":
				System.out.println("Тест эхлэж байна... meta");
				MetaMain.mainSystem(systemData.getGeneratedId(), String.valueOf(Thread.currentThread().getId()), systemData.getCustomerName(),
						systemData.getCreatedDate(), systemData.getModuleId(), systemData.getDatabaseName(), systemData.getDatabaseUsername(),
						systemData.getSystemURL(), systemData.getUsername(), systemData.getPassword());
				result = "Тест хийж дууссан";
				break;
			case "process":
				System.out.println("Тест эхлэж байна... process");
				MainProcess.mainProcess(systemData.getGeneratedId(), String.valueOf(Thread.currentThread().getId()), systemData.getCustomerName(),
						systemData.getCreatedDate(), systemData.getModuleId(), systemData.getDatabaseName(), systemData.getDatabaseUsername(),
						systemData.getSystemURL(), systemData.getUsername(), systemData.getPassword(), systemData.getMetaOrPatchId());
				result = "Тест хийж дууссан";
				break;
			case "metaWithProcess":
				System.out.println("Тест эхлэж байна... metaWithProcess");
				MetaWithProcessMain.mainProcess(systemData.getGeneratedId());
				result = "Тест хийж дууссан";
				break;
			case "patch":
				System.out.println("Patch тест эхлэж байна");
				patchMain.mainProcess(systemData.getGeneratedId(), String.valueOf(Thread.currentThread().getId()), systemData.getCustomerName(),
						systemData.getCreatedDate(), systemData.getModuleId(), systemData.getDatabaseName(), systemData.getDatabaseUsername(),
						systemData.getSystemURL(), systemData.getUsername(), systemData.getPassword(), systemData.getMetaOrPatchId());
				result = "Тест хийж дууссан";
				break;
			default:
				throw new IllegalArgumentException("Модуль олдсонгүй");
		}
		return result;
	}
}

