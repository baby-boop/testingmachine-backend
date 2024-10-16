package testingmachine_backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import testingmachine_backend.meta.MetaList.MetaMain;
import testingmachine_backend.metaverse.Main.MVMain;

@EnableScheduling
@SpringBootApplication
public class TestingmachineBackendApplication  implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(TestingmachineBackendApplication.class, args);
	}

	@Override
	public void run(String... args) {
		if (args.length > 0) {
			String module = args[0];
			try {
				String result = executeModule(module);
				System.out.println(result);
			} catch (Exception e) {
				System.err.println("Модуль ажиллуулахад алдаа гарлаа : " + e.getMessage());
			}
		} else {
			System.out.println("Модуль олдсонгүй .");
		}
	}
	public String executeModule(String module) {
		String result;
		switch (module) {
			case "meta":
				System.out.println("Starting module...");
				MetaMain MetaMain = new MetaMain();
				MetaMain.mainSystem();
				result = "Тест хийж дууссан";
				break;
			case "metaverse":
				System.out.println("Starting module...");
				MVMain MetaverseMain = new MVMain();
				MetaverseMain.mainSystem();
				result = "Тест хийж дууссан";
				break;
			default:
				throw new IllegalArgumentException("Модуль олдсонгүй");
		}
		return result;
	}

}

