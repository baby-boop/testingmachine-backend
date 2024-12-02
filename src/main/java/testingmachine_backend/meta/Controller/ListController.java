package testingmachine_backend.meta.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import testingmachine_backend.meta.DTO.*;
import testingmachine_backend.meta.MetaList.*;
import testingmachine_backend.meta.Service.MetaMessageStatusService;
import testingmachine_backend.meta.Utils.CheckWorkflow;
import testingmachine_backend.meta.Utils.IsErrorList;
import testingmachine_backend.process.Service.ProcessMessageStatusService;

import java.util.List;

@RestController
public class ListController {

    @GetMapping("/metalist")
    public List<ErrorMessageDTO> getAlerts() {
         return MetaMessageStatusService.getMetaStatuses();
    }

    @GetMapping("/meta")
    public ListDTO displayList() {
        int metaCount = MetaLists.getCheckCount();
        int workflowCount = CheckWorkflow.getWorkflowCount();
        return new ListDTO(metaCount, workflowCount);
    }

    @GetMapping("/meta-total")
    public TotalDTO displayTotal() {
        int totalCount = MetaLists.getTotalCount();
        return new TotalDTO(totalCount);
    }

}