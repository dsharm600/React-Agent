package learn.field_agent.domain;

import learn.field_agent.data.AgentRepository;
import learn.field_agent.models.Agent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class AgentServiceTest {

    @Autowired
    AgentService service;

    @MockBean
    AgentRepository repository;

    @Test
    void shouldFindHazel() {
        // pass-through test, probably not useful
        Agent expected = makeAgent();
        when(repository.findById(1)).thenReturn(expected);
        Agent actual = service.findById(1);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddWhenInvalid() {
        Agent agent = makeAgent();
        Result<Agent> result = service.add(agent);
        assertEquals(ResultType.INVALID, result.getType());

        agent.setAgentId(0);
        agent.setFirstName(null);
        result = service.add(agent);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotAddWhenValid() {
        Agent expected = makeAgent();
        Agent arg = makeAgent();
        arg.setAgentId(0);

        when(repository.add(arg)).thenReturn(expected);
        Result<Agent> result = service.add(arg);
        assertEquals(ResultType.SUCCESS, result.getType());

        assertEquals(expected, result.getPayload());
    }

    Agent makeAgent() {
        //('Hazel','C','Sauven','1954-09-16',76),
        Agent agent = new Agent();
        agent.setAgentId(1);
        agent.setFirstName("Hazel");
        agent.setMiddleName("C");
        agent.setLastName("Sauven");
        agent.setDob(LocalDate.of(1954, 9, 16));
        agent.setHeightInInches(76);
        return agent;
    }
}