package learn.field_agent.data;

import learn.field_agent.models.Agency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AgencyJdbcTemplateRepositoryTest {

    @Autowired
    AgencyJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindAgencies() {
        List<Agency> agencies = repository.findAll();
        assertNotNull(agencies);
        assertTrue(agencies.size() > 0);
    }

    @Test
    void shouldFindAcme() {
        Agency acme = repository.findById(1);
        assertEquals("ACME", acme.getShortName());
    }

    @Test
    void shouldAddAgency() {
        Agency agency = new Agency();
        agency.setShortName("TEST");
        agency.setLongName("Test Agency");
        Agency actual = repository.add(agency);
        assertNotNull(actual);
        assertEquals(4, actual.getAgencyId());
    }

    @Test
    void shouldUpdateAgency() {

        Agency agency = new Agency();
        agency.setAgencyId(3);
        agency.setShortName("TEST");
        agency.setLongName("Test Agency");

        assertTrue(repository.update(agency));
    }

    @Test
    void shouldDeleteAgency() {
        assertTrue(repository.deleteById(2));
        assertFalse(repository.deleteById(2));
    }
}