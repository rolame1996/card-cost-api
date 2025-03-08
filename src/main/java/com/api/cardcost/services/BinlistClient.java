package com.api.cardcost.services;

import com.api.cardcost.config.RestTemplateConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
public class BinlistClient {

    private static final String BINLIST_URL = "https://lookup.binlist.net/";

    private final RestTemplateConfig conn;

    public BinlistClient(RestTemplateConfig conn) {
        this.conn = conn;
    }

    public ResponseEntity<String> getCountryAlpha2(Integer cardNumber) {
        try {
            RestTemplate restTemplate = conn.restTemplate();
            Map<String, Object> response = restTemplate.getForObject(BINLIST_URL + cardNumber, Map.class);
            if (response != null && response.containsKey("country")) {
                Map<String, Object> country = (Map<String, Object>) response.get("country");
                return new ResponseEntity<>(country.get("alpha2").toString(), HttpStatus.OK);
            }
            return new ResponseEntity<>("Country not found", HttpStatus.NOT_FOUND);

        } catch (HttpClientErrorException e) {
            log.error("Client error: {} - {}", e.getStatusCode(), e.getStatusText());
            if (e.getStatusCode().value() == 404) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
            } else if (e.getStatusCode().value() == 429) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.TOO_MANY_REQUESTS);
            }
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("An error occurred: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
