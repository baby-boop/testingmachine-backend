package testingmachine_backend.metaverse.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import testingmachine_backend.metaverse.DTO.MVCountDTO;
import testingmachine_backend.metaverse.DTO.MVErrorTimeoutDTO;
import testingmachine_backend.metaverse.DTO.MVTotalDTO;
import testingmachine_backend.metaverse.DTO.MVerrorMessageDTO;
import testingmachine_backend.metaverse.Main.MVLists;
import testingmachine_backend.metaverse.Utils.isErrorMv;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MVController {

    @GetMapping("/mv-message")
    public List<MVerrorMessageDTO> getAlerts() {
        return isErrorMv.getMvMessages();
    }

    @GetMapping("/metaverse")
    public MVCountDTO displayCount() {
        int inticatorCount = MVLists.getCheckMvCount();
        return new MVCountDTO(inticatorCount);
    }

    @GetMapping("/mv-total")
    public MVTotalDTO displayTotal() {
        int totalCount = MVLists.getMvTotalCount();
        return new MVTotalDTO(totalCount);
    }

    @GetMapping("/mv-timeout")
    public List<MVErrorTimeoutDTO> getMvTimeoutErrors() {
        return MVLists.MVErrorTimeoutMessages();
    }
}
