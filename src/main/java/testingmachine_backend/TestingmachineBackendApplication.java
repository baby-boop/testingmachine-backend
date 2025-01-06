



package testingmachine_backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import testingmachine_backend.controller.JsonController;
import testingmachine_backend.meta.MetaList.MetaMain;

import testingmachine_backend.metaWithProcess.MetaWithProcessMain;
import testingmachine_backend.process.MainProcess;

@EnableScheduling
@SpringBootApplication
public class TestingmachineBackendApplication
		implements CommandLineRunner
{

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
				System.out.println("Тест эхлэж байна... 1");
				MetaMain.mainSystem(JsonController.getJsonId());
				result = "Тест хийж дууссан";
				break;
			case "process":
				System.out.println("Тест эхлэж байна... 2");
				MainProcess.mainProcess(JsonController.getJsonId());
				result = "Тест хийж дууссан";
				break;
			case "metaWithProcess":
				System.out.println("Тест эхлэж байна... 3");
				MetaWithProcessMain.mainProcess(JsonController.getJsonId());
				result = "Тест хийж дууссан";
				break;
			default:
				throw new IllegalArgumentException("Модуль олдсонгүй");
		}
		return result;
	}
}

//Би selenium-ийг зэрэгцээ байдлаар ашиглаж чадах уу? Жишээ нь: module сонгоод цаашлаад маш олон үйлдэл хийдэг бөгөөд одоо эхний command ажиллаж байхад дараагийн command орж ирсэн тохиолдолд хамт зэрэгцээ байдлаар ажиллана. Ерөнхийдөө маш олон command ирэх бөгөөд тухай бүрийг ажлуулна. Одоо эхний command ажиллаж байхад дараагийн ирэх үед өмнөх command зогсодог. Тэгэж ажиллаж боломжтой юу?
