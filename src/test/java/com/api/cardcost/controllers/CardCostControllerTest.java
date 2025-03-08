package com.api.cardcost.controllers;

import com.api.cardcost.entities.dtos.CardNumberDto;
import com.api.cardcost.entities.dtos.ClearingCostDto;
import com.api.cardcost.entities.dtos.CountryCodeDto;
import com.api.cardcost.services.CardCostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
class CardCostControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private CardCostService cardCostService;

    @InjectMocks
    private CardCostController cardCostController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cardCostController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateClearingCost() throws Exception {
        ClearingCostDto clearingCostDto = new ClearingCostDto("US", 5.0);
        doNothing().when(cardCostService).createClearingCost(any());

        mockMvc.perform(post("/card-cost")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(clearingCostDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void testDeleteClearingCost() throws Exception {
        CountryCodeDto countryCodeDto = new CountryCodeDto("US");
        doNothing().when(cardCostService).deleteClearingCost(anyString());

        mockMvc.perform(delete("/card-cost")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(countryCodeDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateClearingCost() throws Exception {
        ClearingCostDto clearingCostDto = new ClearingCostDto("US", 6.0);
        when(cardCostService.updateClearingCost(any())).thenReturn(clearingCostDto);

        mockMvc.perform(put("/card-cost")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(clearingCostDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetClearingCost() throws Exception {
        ClearingCostDto clearingCostDto = new ClearingCostDto("US", 5.0);
        when(cardCostService.getClearingCost("US")).thenReturn(clearingCostDto);

        mockMvc.perform(get("/card-cost/US"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllClearingCost() throws Exception {
        List<ClearingCostDto> clearingCosts = Arrays.asList(
                new ClearingCostDto("US", 5.0),
                new ClearingCostDto("GR", 15.0)
        );

        when(cardCostService.getAllClearingCost()).thenReturn(clearingCosts);

        mockMvc.perform(get("/card-cost"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetCardCost() throws Exception {
        CardNumberDto cardNumberDto = new CardNumberDto(45717360);
        ClearingCostDto mockResponse = new ClearingCostDto("US", 5.0);

        when(cardCostService.getCardCost(45717360)).thenReturn(mockResponse);

        mockMvc.perform(post("/card-cost/payment-cards-cost")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(cardNumberDto)))
                .andExpect(status().isOk());
    }

}