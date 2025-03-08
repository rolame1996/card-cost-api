package com.api.cardcost.services;

import com.api.cardcost.entities.ClearingCost;
import com.api.cardcost.entities.dtos.ClearingCostDto;
import com.api.cardcost.exceptions.NotFoundException;
import com.api.cardcost.exceptions.TooManyRequestsException;
import com.api.cardcost.repositories.ClearingCostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CardCostService {

    private final BinlistClient binlistClient;
    private final ClearingCostRepository clearingCostRepository;

    public CardCostService(BinlistClient binlistClient, ClearingCostRepository clearingCostRepository) {
        this.binlistClient = binlistClient;
        this.clearingCostRepository = clearingCostRepository;
    }

    public ClearingCostDto getCardCost(Integer cardNumber) throws Exception {
        validateCardNumber(cardNumber);

        ResponseEntity<?> response = binlistClient.getCountryAlpha2(cardNumber);
        if (response.getStatusCode() == HttpStatus.OK) {
            return createResponse(response.getBody().toString());
        } else {
            handleErrorResponse(response);
            return null;
        }
    }

    private void validateCardNumber(Integer cardNumber) {
        if (cardNumber == null) {
            throw new IllegalArgumentException("Card number cannot be null.");
        } else if (cardNumber.toString().length() < 8 || cardNumber.toString().length() > 19) {
            throw new IllegalArgumentException("Card number must have between 8 and 19 digits.");
        }
    }

    private ClearingCostDto createResponse(String countryCode) {
        Optional<ClearingCost> clearingCost = clearingCostRepository.findByCountryCode(countryCode);
        double cost = clearingCost.map(ClearingCost::getCost).orElse(10.0);
        return new ClearingCostDto(countryCode, cost);
    }

    private void handleErrorResponse(ResponseEntity<?> response) throws Exception {
        HttpStatus status = (HttpStatus) response.getStatusCode();
        String message = "Error: " + response.getBody();
        switch (status) {
            case NOT_FOUND -> throw new NotFoundException("Country not found. " + message);
            case TOO_MANY_REQUESTS -> throw new TooManyRequestsException("Too many requests. " + message);
            default -> throw new Exception("An error occurred. " + message);
        }
    }

    public void createClearingCost(ClearingCostDto clearingCostDto) {
        validateClearingCostDto(clearingCostDto);
        clearingCostRepository.save(new ClearingCost(clearingCostDto.getCountryCode(), clearingCostDto.getCost()));
    }

    private void validateClearingCostDto(ClearingCostDto clearingCostDto) {
        if (clearingCostDto.getCountryCode() == null || clearingCostDto.getCountryCode().length() != 2) {
            throw new IllegalArgumentException("Country code must have 2 characters.");
        }
        if (clearingCostDto.getCost() == null || clearingCostDto.getCost() < 0) {
            throw new IllegalArgumentException("Cost cannot be null or negative.");
        }
    }

    public void deleteClearingCost(String countryCode) {
        validateCountryCode(countryCode);

        Optional<ClearingCost> clearingCost = clearingCostRepository.findByCountryCode(countryCode);
        if (clearingCost.isPresent()) {
            clearingCostRepository.delete(clearingCost.get());
        } else {
            throw new NotFoundException("Country not found.");
        }
    }

    private void validateCountryCode(String countryCode) {
        if (countryCode == null || countryCode.length() != 2) {
            throw new IllegalArgumentException("Country code must have 2 characters.");
        }
    }

    public ClearingCostDto updateClearingCost(ClearingCostDto clearingCostDto) {
        validateClearingCostDto(clearingCostDto);

        ClearingCostDto updatedDto = new ClearingCostDto();
        Optional<ClearingCost> clearingCost = clearingCostRepository.findByCountryCode(clearingCostDto.getCountryCode());

        if (clearingCost.isPresent()) {
            clearingCost.get().setCost(clearingCostDto.getCost());
            clearingCostRepository.save(clearingCost.get());
            updatedDto.setCountryCode(clearingCost.get().getCountryCode());
            updatedDto.setCost(clearingCost.get().getCost());
        } else {
            throw new NotFoundException("Country not found.");
        }

        return updatedDto;
    }

    public ClearingCostDto getClearingCost(String countryCode) {
        validateCountryCode(countryCode);

        Optional<ClearingCost> clearingCost = clearingCostRepository.findByCountryCode(countryCode);
        if (clearingCost.isPresent()) {
            return new ClearingCostDto(clearingCost.get().getCountryCode(), clearingCost.get().getCost());
        } else {
            throw new NotFoundException("Country not found.");
        }
    }

    public List<ClearingCostDto> getAllClearingCost() {
        List<ClearingCost> clearingCosts = clearingCostRepository.findAll();
        if (clearingCosts.isEmpty()) {
            throw new NotFoundException("No clearing costs found.");
        }

        return clearingCosts.stream()
                .map(clearingCost -> new ClearingCostDto(clearingCost.getCountryCode(), clearingCost.getCost()))
                .toList();
    }
}
