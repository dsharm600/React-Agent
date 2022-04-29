package learn.field_agent.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import learn.field_agent.data.AgencyRepository;
import learn.field_agent.data.AppUserRepository;
import learn.field_agent.models.Agency;
import learn.field_agent.models.AppUser;
import learn.field_agent.security.JwtConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AgencyControllerTest {

    @MockBean
    AgencyRepository agencyRepository;

    @MockBean
    AppUserRepository appUserRepository;

    @Autowired
    MockMvc mvc;

    @Autowired
    JwtConverter jwtConverter;

    String token;

    @BeforeEach
    void setup() {
        AppUser appUser = new AppUser(1, "john@smith.com", "P@ssw0rd!", false,
                List.of("ADMIN"));

        when(appUserRepository.findByUsername("john@smith.com")).thenReturn(appUser);

        token = jwtConverter.getTokenFromUser(appUser);
    }

    @Test
    void addShouldReturn400WhenEmpty() throws Exception {

        var request = post("/api/agency")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token);

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void addShouldReturn400WhenInvalid() throws Exception {

        ObjectMapper jsonMapper = new ObjectMapper();

        Agency agency = new Agency();
        String agencyJson = jsonMapper.writeValueAsString(agency);

        var request = post("/api/agency")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(agencyJson);

        mvc.perform(request)
                .andExpect(status().isBadRequest());

    }

    @Test
    void addShouldReturn415WhenMultipart() throws Exception {

        ObjectMapper jsonMapper = new ObjectMapper();

        Agency agency = new Agency(0, "TST", "Test Agency");
        String agencyJson = jsonMapper.writeValueAsString(agency);

        var request = post("/api/agency")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("Authorization", "Bearer " + token)
                .content(agencyJson);

        mvc.perform(request)
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void addShouldReturn201() throws Exception {

        Agency agency = new Agency(0, "TST", "Test Agency");
        Agency expected = new Agency(1, "TST", "Test Agency");

        when(agencyRepository.add(any())).thenReturn(expected);
        ObjectMapper jsonMapper = new ObjectMapper();

        String agencyJson = jsonMapper.writeValueAsString(agency);
        String expectedJson = jsonMapper.writeValueAsString(expected);

        var request = post("/api/agency")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(agencyJson);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedJson));
    }
}