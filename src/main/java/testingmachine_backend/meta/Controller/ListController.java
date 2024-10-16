package testingmachine_backend.meta.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import testingmachine_backend.meta.DTO.*;
import testingmachine_backend.meta.MetaList.*;
import testingmachine_backend.meta.Utils.CheckWorkflow;
import testingmachine_backend.meta.Utils.IsErrorList;

import java.util.List;

@RestController
public class ListController {

    @GetMapping("/list")
    public List<ErrorMessageDTO> getAlerts() {
        return IsErrorList.getListMessages();
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

    @GetMapping("/timeout")
    public List<ErrorTimeoutDTO> getTimeoutErrors() {
        return MetaLists.errorTimeoutMessages();
    }

    @GetMapping("/nodata")
    public List<NotFoundRowDTO> getNoData() {
        return CheckWorkflow.getNotFoundRowCount();
    }

    @GetMapping("/workflow")
    public List<WorkflowMessageDTO> getWorkflow() {
        return CheckWorkflow.getWorkflowMessages();
    }

}