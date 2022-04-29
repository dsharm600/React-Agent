package learn.field_agent.domain;

import learn.field_agent.data.AgencyRepository;
import learn.field_agent.models.Agency;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class AgencyServiceTest {

    @Autowired
    AgencyService service;

    @MockBean
    AgencyRepository agencyRepository;

    @Test
    void shouldAdd() {
        Agency agency = new Agency(0, "TEST", "Long Name Test");
        Agency mockOut = new Agency(5, "TEST", "Long Name Test");

        when(agencyRepository.add(agency)).thenReturn(mockOut);

        Result<Agency> actual = service.add(agency);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(mockOut, actual.getPayload());
    }

    @Test
    void shouldNotAddWhenInvalid() {

        Agency agency = new Agency(35, "TEST", "Long Name Test");

        Result<Agency> actual = service.add(agency);
        assertEquals(ResultType.INVALID, actual.getType());

        agency.setAgencyId(0);
        agency.setShortName(null);
        actual = service.add(agency);
        assertEquals(ResultType.INVALID, actual.getType());

        agency.setShortName("TEST");
        agency.setLongName("   ");
        actual = service.add(agency);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldUpdate() {
        Agency agency = new Agency(5, "TEST", "Long Name Test");

        when(agencyRepository.update(agency)).thenReturn(true);
        Result<Agency> actual = service.update(agency);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldNotUpdateMissing() {
        Agency agency = new Agency(35, "TEST", "Long Name Test");

        when(agencyRepository.update(agency)).thenReturn(false);
        Result<Agency> actual = service.update(agency);
        assertEquals(ResultType.NOT_FOUND, actual.getType());
    }

    @Test
    void shouldNotUpdateWhenInvalid() {
        Agency agency = new Agency(35, null, "Long Name Test");

        Result<Agency> actual = service.update(agency);
        assertEquals(ResultType.INVALID, actual.getType());

        agency.setShortName("TEST");
        agency.setLongName(" ");
        actual = service.update(agency);
        assertEquals(ResultType.INVALID, actual.getType());

        agency.setAgencyId(0);
        agency.setLongName("Long Name Test");
        actual = service.update(agency);
        assertEquals(ResultType.INVALID, actual.getType());
    }

}