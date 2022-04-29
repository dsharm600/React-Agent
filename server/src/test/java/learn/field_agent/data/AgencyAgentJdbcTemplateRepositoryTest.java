package learn.field_agent.data;

import learn.field_agent.models.AgencyAgent;
import learn.field_agent.models.Agent;
import learn.field_agent.models.SecurityClearance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AgencyAgentJdbcTemplateRepositoryTest {

    @Autowired
    AgencyAgentJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldAdd() {
        AgencyAgent agencyAgent = makeAgencyAgent();
        assertTrue(repository.add(agencyAgent));

        try {
            repository.add(agencyAgent); // must fail
            fail("cannot add an agent to an agency twice.");
        } catch (DataAccessException ex) {
            // this is expected.
        }
    }

    @Test
    void shouldUpdate() {
        AgencyAgent agencyAgent = makeAgencyAgent();
        agencyAgent.setIdentifier("008"); // avoid duplicates
        agencyAgent.getAgent().setAgentId(1);
        assertTrue(repository.update(agencyAgent));

        agencyAgent.setAgencyId(12);
        assertFalse(repository.update(agencyAgent));
    }

    @Test
    void shouldDelete() {
        assertTrue(repository.deleteByKey(1, 3));
        assertFalse(repository.deleteByKey(1, 3));
    }

    AgencyAgent makeAgencyAgent() {
        AgencyAgent agencyAgent = new AgencyAgent();
        agencyAgent.setAgencyId(1);
        agencyAgent.setIdentifier("007");
        agencyAgent.setActivationDate(LocalDate.of(2010, 6, 19));
        agencyAgent.setActive(true);

        SecurityClearance securityClearance = new SecurityClearance();
        securityClearance.setSecurityClearanceId(1);
        securityClearance.setName("Secret");
        agencyAgent.setSecurityClearance(securityClearance);

        Agent agent = new Agent();
        agent.setAgentId(6);
        agent.setFirstName("Test");
        agencyAgent.setAgent(agent);
        return agencyAgent;
    }
}