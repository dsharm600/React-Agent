package learn.field_agent.controllers;

import learn.field_agent.domain.AgentService;
import learn.field_agent.domain.Result;
import learn.field_agent.models.Agent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/agent")
public class AgentController {

    private final AgentService service;

    public AgentController(AgentService service) {
        this.service = service;
    }

    @GetMapping
    public List<Agent> findAll() {
        return service.findAll();
    }

    @GetMapping("/{agentId}")
    public Agent findById(@PathVariable int agentId) {
        return service.findById(agentId);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody Agent agent) {
        Result<Agent> result = service.add(agent);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @PutMapping("/{agentId}")
    public ResponseEntity<Object> update(@PathVariable int agentId, @RequestBody Agent agent) {
        if (agentId != agent.getAgentId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<Agent> result = service.update(agent);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{agentId}")
    public ResponseEntity<Void> deleteById(@PathVariable int agentId) {
        if (service.deleteById(agentId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
