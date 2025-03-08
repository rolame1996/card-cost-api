package com.api.cardcost.services;

import com.api.cardcost.entities.ClearingCost;
import com.api.cardcost.entities.dtos.ClearingCostDto;
import com.api.cardcost.exceptions.NotFoundException;
import com.api.cardcost.exceptions.TooManyRequestsException;
import com.api.cardcost.repositories.ClearingCostRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardCostServiceTest {

    @Mock
    private ClearingCostRepository clearingCostRepository;

    @Mock
    private BinlistClient binlistClient;

    @InjectMocks
    private CardCostService cardCostService;

    @Test
    public void createClearingCost_SuccessfullyCreates() {
        ClearingCostDto clearingCostDto = new ClearingCostDto("US", 5.0);
        ClearingCost clearingCost = new ClearingCost(1L, "US", 5.0);
        when(clearingCostRepository.save(any(ClearingCost.class))).thenReturn(clearingCost);

        cardCostService.createClearingCost(clearingCostDto);

        verify(clearingCostRepository, times(1)).save(any(ClearingCost.class));
    }

    @Test
    public void createClearingCost_NullCountryCode_ThrowsException() {
        ClearingCostDto clearingCostDto = new ClearingCostDto(null, 5.0);

        assertThrows(IllegalArgumentException.class, () -> {
            cardCostService.createClearingCost(clearingCostDto);
        });
    }

    @Test
    public void createClearingCost_InvalidCountryCodeLength_ThrowsException() {
        ClearingCostDto clearingCostDto = new ClearingCostDto("USA", 5.0);

        assertThrows(IllegalArgumentException.class, () -> {
            cardCostService.createClearingCost(clearingCostDto);
        });
    }

    @Test
    public void createClearingCost_NegativeCost_ThrowsException() {
        ClearingCostDto clearingCostDto = new ClearingCostDto("US", -5.0);

        assertThrows(IllegalArgumentException.class, () -> {
            cardCostService.createClearingCost(clearingCostDto);
        });
    }

    @Test
    public void createClearingCost_NullCost_ThrowsException() {
        ClearingCostDto clearingCostDto = new ClearingCostDto("US", null);

        assertThrows(IllegalArgumentException.class, () -> {
            cardCostService.createClearingCost(clearingCostDto);
        });
    }

    @Test
    public void deleteClearingCost_SuccessfullyDeletes() {
        doNothing().when(clearingCostRepository).delete(any(ClearingCost.class));
        when(clearingCostRepository.findByCountryCode("US")).thenReturn(Optional.of(new ClearingCost("US", 5.0)));

        cardCostService.deleteClearingCost("US");

        verify(clearingCostRepository, times(1)).delete(any(ClearingCost.class));
    }

    @Test
    public void deleteClearingCost_CountryNotFound_ThrowsException() {
        when(clearingCostRepository.findByCountryCode("XX")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            cardCostService.deleteClearingCost("XX");
        });
    }

    @Test
    public void deleteClearingCost_NullCountryCode_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            cardCostService.deleteClearingCost(null);
        });
    }

    @Test
    public void deleteClearingCost_InvalidCountryCodeLength_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            cardCostService.deleteClearingCost("USA");
        });
    }

    @Test
    public void updateClearingCost_SuccessfullyUpdates() {
        ClearingCostDto clearingCostDto = new ClearingCostDto("US", 6.0);
        ClearingCost existingClearingCost = new ClearingCost(1L,"US", 5.0);
        when(clearingCostRepository.findByCountryCode("US")).thenReturn(Optional.of(existingClearingCost));

        ClearingCostDto result = cardCostService.updateClearingCost(clearingCostDto);

        assertEquals("US", result.getCountryCode());
        assertEquals(6.0, result.getCost());
        verify(clearingCostRepository, times(1)).save(existingClearingCost);
    }

    @Test
    public void updateClearingCost_CountryNotFound_ThrowsException() {
        ClearingCostDto clearingCostDto = new ClearingCostDto("XX", 6.0);
        when(clearingCostRepository.findByCountryCode("XX")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            cardCostService.updateClearingCost(clearingCostDto);
        });
    }

    @Test
    public void updateClearingCost_NullCountryCode_ThrowsException() {
        ClearingCostDto clearingCostDto = new ClearingCostDto(null, 6.0);

        assertThrows(IllegalArgumentException.class, () -> {
            cardCostService.updateClearingCost(clearingCostDto);
        });
    }

    @Test
    public void updateClearingCost_InvalidCountryCodeLength_ThrowsException() {
        ClearingCostDto clearingCostDto = new ClearingCostDto("USA", 6.0);

        assertThrows(IllegalArgumentException.class, () -> {
            cardCostService.updateClearingCost(clearingCostDto);
        });
    }

    @Test
    public void updateClearingCost_NegativeCost_ThrowsException() {
        ClearingCostDto clearingCostDto = new ClearingCostDto("US", -6.0);

        assertThrows(IllegalArgumentException.class, () -> {
            cardCostService.updateClearingCost(clearingCostDto);
        });
    }

    @Test
    public void updateClearingCost_NullCost_ThrowsException() {
        ClearingCostDto clearingCostDto = new ClearingCostDto("US", null);

        assertThrows(IllegalArgumentException.class, () -> {
            cardCostService.updateClearingCost(clearingCostDto);
        });
    }

    @Test
    public void getClearingCost_SuccessfullyRetrieves() {
        ClearingCost clearingCost = new ClearingCost(1L, "US", 5.0);
        when(clearingCostRepository.findByCountryCode("US")).thenReturn(Optional.of(clearingCost));

        ClearingCostDto result = cardCostService.getClearingCost("US");

        assertEquals("US", result.getCountryCode());
        assertEquals(5.0, result.getCost());
    }

    @Test
    public void getClearingCost_CountryNotFound_ThrowsException() {
        when(clearingCostRepository.findByCountryCode("XX")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            cardCostService.getClearingCost("XX");
        });
    }

    @Test
    public void getClearingCost_NullCountryCode_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            cardCostService.getClearingCost(null);
        });
    }

    @Test
    public void getClearingCost_InvalidCountryCodeLength_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            cardCostService.getClearingCost("USA");
        });
    }

    @Test
    public void getAllClearingCost_SuccessfullyRetrieves() {
        List<ClearingCost> clearingCosts = new ArrayList<>();
        clearingCosts.add(new ClearingCost(1L,"US", 5.0));
        clearingCosts.add(new ClearingCost(2L, "CA", 7.0));
        doReturn(clearingCosts).when(clearingCostRepository).findAll();

        List<ClearingCostDto> result = cardCostService.getAllClearingCost();

        assertEquals(2, result.size());
        assertEquals("US", result.get(0).getCountryCode());
        assertEquals(5.0, result.get(0).getCost());
        assertEquals("CA", result.get(1).getCountryCode());
        assertEquals(7.0, result.get(1).getCost());
    }

    @Test
    public void getAllClearingCost_NoClearingCostsFound_ThrowsException() {
        when(clearingCostRepository.findAll()).thenReturn(List.of());

        assertThrows(NotFoundException.class, () -> {
            cardCostService.getAllClearingCost();
        });
    }

    @Test
    public void getCardCost_ValidCardNumber_ReturnsClearingCostDto() throws Exception {
        int cardNumber = 12345678;
        String countryCode = "US";
        ClearingCost clearingCost = new ClearingCost(countryCode, 10.0);
        when(binlistClient.getCountryAlpha2(cardNumber)).thenReturn(new ResponseEntity<>(countryCode, HttpStatus.OK));
        when(clearingCostRepository.findByCountryCode(countryCode)).thenReturn(Optional.of(clearingCost));

        ClearingCostDto result = cardCostService.getCardCost(cardNumber);

        assertEquals(countryCode, result.getCountryCode());
        assertEquals(10.0, result.getCost());
    }

    @Test
    public void getCardCost_CardNumberNotFound_ThrowsNotFoundException() {
        int cardNumber = 12345678;
        when(binlistClient.getCountryAlpha2(cardNumber)).thenReturn(new ResponseEntity<>("Country not found", HttpStatus.NOT_FOUND));

        assertThrows(NotFoundException.class, () -> {
            cardCostService.getCardCost(cardNumber);
        });
    }

    @Test
    public void getCardCost_TooManyRequests_ThrowsTooManyRequestsException() {
        int cardNumber = 12345678;
        when(binlistClient.getCountryAlpha2(cardNumber)).thenReturn(new ResponseEntity<>("Too many requests", HttpStatus.TOO_MANY_REQUESTS));

        assertThrows(TooManyRequestsException.class, () -> {
            cardCostService.getCardCost(cardNumber);
        });
    }

    @Test
    public void getCardCost_NullCardNumber_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            cardCostService.getCardCost(null);
        });
    }

    @Test
    public void getCardCost_InvalidCardNumberLength_ThrowsIllegalArgumentException() {
        int shortCardNumber = 1234567;

        assertThrows(IllegalArgumentException.class, () -> {
            cardCostService.getCardCost(shortCardNumber);
        });
    }

}