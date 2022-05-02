package learn.field_agent.controllers;

import learn.field_agent.domain.AgencyService;
import learn.field_agent.domain.Result;
import learn.field_agent.models.AgencyAgent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/agency/agent")
public class AgencyAgentController {

    private final AgencyService service;

    public AgencyAgentController(AgencyService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody AgencyAgent agencyAgent) {
        Result<Void> result = service.addAgent(agencyAgent);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody AgencyAgent agencyAgent) {
        Result<Void> result = service.updateAgent(agencyAgent);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{agencyId}/{agentId}")
    public ResponseEntity<Void> deleteByKey(@PathVariable int agencyId, @PathVariable int agentId) {
        if (service.deleteAgentByKey(agencyId, agentId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
