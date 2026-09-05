package org.sun.racing.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.sun.racing.model.Race;
import org.sun.racing.service.RaceService;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
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
        when(raceService.createNewRace(anyInt())).thenReturn(new Race(UUID.randomUUID(), 100, Race.RaceStatus.CREATED));
        mockMvc.perform(post("/races")
                        .contentType(APPLICATION_JSON)
                        .content("{\"durationInSeconds\":100}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.durationInSeconds").value(100))
                .andExpect(jsonPath("$.raceStatus").value("CREATED"));
    }

    @Test
    void testInvalidDuration() throws Exception {
        mockMvc.perform(post("/races")
                        .contentType(APPLICATION_JSON)
                        .content("{\"durationInSeconds\":\"abc\"}"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.errorMessage").value("Race duration shall be integer between 1 and 3600 seconds"));

        mockMvc.perform(post("/races")
                        .contentType(APPLICATION_JSON)
                        .content("{\"something\":\"abc\"}"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.errorMessage").value("Race duration shall be integer between 1 and 3600 seconds"));

        mockMvc.perform(post("/races")
                        .contentType(APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.errorMessage").value("Race duration shall be integer between 1 and 3600 seconds"));
    }


}