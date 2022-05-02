package learn.field_agent.data;

import learn.field_agent.models.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LocationJdbcTemplateRepositoryTest {

    final static int NEXT_LOCATION_ID = 7;

    @Autowired
    LocationJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindHQ() {
        Location actual = repository.findById(1);
        assertNotNull(actual);
        assertEquals(1, actual.getLocationId());
        assertEquals("HQ", actual.getName());
    }

    @Test
    void shouldAdd() {
        Location location = makeLocation();
        Location actual = repository.add(location);
        assertNotNull(actual);
        assertEquals(NEXT_LOCATION_ID, actual.getLocationId());
    }

    @Test
    void shouldUpdate() {
        Location location = makeLocation();
        location.setLocationId(6);
        assertTrue(repository.update(location));
        location.setLocationId(16);
        assertFalse(repository.update(location));
    }

    @Test
    void shouldDelete() {
        assertTrue(repository.deleteById(5));
        assertFalse(repository.deleteById(5));
    }

    Location makeLocation() {
        Location location = new Location();
        location.setName("Test Location");
        location.setAddress("123 Test Ave.");
        location.setCity("Test City");
        location.setRegion("TEST");
        location.setCountryCode("TS");
        location.setPostalCode("TS-5555");
        location.setAgencyId(1);
        return location;
    }
}