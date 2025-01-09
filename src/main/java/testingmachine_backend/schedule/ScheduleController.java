//package testingmachine_backend.schedule;
//
//@RestController
//public class ScheduleController {
//
//    @Autowired
//    private UserService userService;
//
//    // Өгөгдлийг хадгалах
//    @PostMapping("/save")
//    public String saveUserData(@RequestParam String username,
//                               @RequestParam String password,
//                               @RequestParam String unitName,
//                               @RequestParam String moduleId,
//                               @RequestParam String scheduleTime) {
//        userService.storeUserData(username, password, unitName, moduleId, scheduleTime);
//        return "User data saved successfully!";
//    }
//
//    // Өгөгдлийг авах
//    @GetMapping("/get/{username}")
//    public UserData getUserData(@PathVariable String username) {
//        return userService.getUserData(username);
//    }
//}
