package com.api.cardcost.services;

import com.api.cardcost.config.RestTemplateConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class BinlistClientTest {

    @Mock
    private RestTemplateConfig restTemplateConfig;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private BinlistClient binlistClient;

    private static final String BINLIST_URL = "https://lookup.binlist.net/";

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        when(restTemplateConfig.restTemplate()).thenReturn(restTemplate);
    }

    @Test
    void testGetCountryAlpha2_Success() {
        Integer cardNumber = 45717360;
        String expectedCountryCode = "US";

        Map<String, Object> country = new HashMap<>();
        country.put("alpha2", expectedCountryCode);
        Map<String, Object> response = new HashMap<>();
        response.put("country", country);

        when(restTemplate.getForObject(BINLIST_URL + cardNumber, Map.class)).thenReturn(response);

        ResponseEntity<String> result = binlistClient.getCountryAlpha2(cardNumber);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedCountryCode, result.getBody());
    }

    @Test
    void testGetCountryAlpha2_NotFound() {
        Integer cardNumber = 45717360;

        when(restTemplate.getForObject(BINLIST_URL + cardNumber, Map.class)).thenReturn(new HashMap<>());

        ResponseEntity<String> result = binlistClient.getCountryAlpha2(cardNumber);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("Country not found", result.getBody());
    }

    @Test
    void testGetCountryAlpha2_ApiReturns404() {
        Integer cardNumber = 45717360;

        when(restTemplate.getForObject(BINLIST_URL + cardNumber, Map.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, "Not Found"));

        ResponseEntity<String> result = binlistClient.getCountryAlpha2(cardNumber);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("404 Not Found", result.getBody());
    }

    @Test
    void testGetCountryAlpha2_ApiReturns429() {
        Integer cardNumber = 45717360;

        when(restTemplate.getForObject(BINLIST_URL + cardNumber, Map.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.TOO_MANY_REQUESTS, "Too Many Requests"));

        ResponseEntity<String> result = binlistClient.getCountryAlpha2(cardNumber);

        assertEquals(HttpStatus.TOO_MANY_REQUESTS, result.getStatusCode());
        assertEquals("429 Too Many Requests", result.getBody());
    }

    @Test
    void testGetCountryAlpha2_InternalServerError() {
        Integer cardNumber = 45717360;

        when(restTemplate.getForObject(BINLIST_URL + cardNumber, Map.class))
                .thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<String> result = binlistClient.getCountryAlpha2(cardNumber);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals("Unexpected error", result.getBody());
    }
}