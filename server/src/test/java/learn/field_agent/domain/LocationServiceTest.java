package learn.field_agent.domain;

import learn.field_agent.data.LocationRepository;
import learn.field_agent.models.Location;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class LocationServiceTest {

    @Autowired
    LocationService service;

    @MockBean
    LocationRepository repository;

    @Test
    void shouldNotAddWhenInvalid() {
        Location location = makeLocation();
        location.setName("   ");

        Result<Location> actual = service.add(location);
        assertEquals(ResultType.INVALID, actual.getType());

        location = makeLocation();
        location.setAddress(null);
        actual = service.add(location);
        assertEquals(ResultType.INVALID, actual.getType());

        location = makeLocation();
        location.setCity("\t");
        actual = service.add(location);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldAdd() {
        Location location = makeLocation();
        Location mockOut = makeLocation();
        mockOut.setLocationId(1);

        when(repository.add(location)).thenReturn(mockOut);

        Result<Location> actual = service.add(location);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(mockOut, actual.getPayload());
    }

    @Test
    void shouldNotUpdateWhenInvalid() {
        Location location = makeLocation();
        Result<Location> actual = service.update(location);
        assertEquals(ResultType.INVALID, actual.getType());

        location = makeLocation();
        location.setLocationId(1);
        location.setCountryCode("");
        actual = service.update(location);
        assertEquals(ResultType.INVALID, actual.getType());

        location = makeLocation();
        location.setLocationId(1);
        location.setPostalCode(null);
        actual = service.update(location);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldUpdate() {
        Location location = makeLocation();
        location.setLocationId(1);

        when(repository.update(location)).thenReturn(true);

        Result<Location> actual = service.update(location);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    Location makeLocation() {
        Location location = new Location();
        location.setName("HQ");
        location.setAddress("123 Oak St.");
        location.setCity("Test City");
        location.setRegion("Test Region");
        location.setCountryCode("TEST");
        location.setPostalCode("55555-PST");
        location.setAgencyId(1);
        return location;
    }
}