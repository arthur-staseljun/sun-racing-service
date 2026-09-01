package org.sun.racing.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.sun.racing.service.RaceService;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RacesController.class)
class RacesControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private RaceService raceService;

    @Test
    void testCreateRace() throws Exception {
        mockMvc.perform(post("/races")
                        .contentType(APPLICATION_JSON)
                        .content("{\"durationInSeconds\":1}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.durationInSeconds").value(1));
        mockMvc.perform(post("/races")
                        .contentType(APPLICATION_JSON)
                        .content("{\"durationInSeconds\":3600}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.durationInSeconds").value(3600));
    }

    @Test
    void testInvalidDuration() throws Exception {
        mockMvc.perform(post("/races")
                        .contentType(APPLICATION_JSON)
                        .content("{\"durationInSeconds\":\"abc\"}"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Race duration shall be integer between 1 and 3600 seconds"));

        mockMvc.perform(post("/races")
                        .contentType(APPLICATION_JSON)
                        .content("{\"something\":\"abc\"}"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Race duration shall be integer between 1 and 3600 seconds"));

        mockMvc.perform(post("/races")
                        .contentType(APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Race duration shall be integer between 1 and 3600 seconds"));

        mockMvc.perform(post("/races")
                        .contentType(APPLICATION_JSON)
                        .content("{\"durationInSeconds\":0}"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Race duration shall be integer between 1 and 3600 seconds"));

        mockMvc.perform(post("/races")
                        .contentType(APPLICATION_JSON)
                        .content("{\"durationInSeconds\":-100}"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Race duration shall be integer between 1 and 3600 seconds"));

        mockMvc.perform(post("/races")
                        .contentType(APPLICATION_JSON)
                        .content("{\"durationInSeconds\":3601}"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Race duration shall be integer between 1 and 3600 seconds"));

    }


}