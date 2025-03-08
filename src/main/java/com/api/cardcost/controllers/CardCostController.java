package com.api.cardcost.controllers;

import com.api.cardcost.entities.dtos.CardNumberDto;
import com.api.cardcost.entities.dtos.ClearingCostDto;
import com.api.cardcost.entities.dtos.CountryCodeDto;
import com.api.cardcost.services.CardCostService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/card-cost")
public class CardCostController {

    private final CardCostService cardCostService;

    public CardCostController(CardCostService cardCostService) {
        this.cardCostService = cardCostService;
    }

    @PostMapping
    @Operation(summary = "Create clearing cost.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Clearing cost created successfully."),
            @ApiResponse(responseCode = "400", description = "Validation error.")
    })
    public ResponseEntity<?> createClearingCost(@RequestBody ClearingCostDto clearingCostDto) {
        cardCostService.createClearingCost(clearingCostDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping
    @Operation(summary = "Delete clearing cost.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clearing cost deleted successfully."),
            @ApiResponse(responseCode = "400", description = "Validation error."),
            @ApiResponse(responseCode = "404", description = "Clearing cost not found.")
    })
    public ResponseEntity<?> deleteClearingCost(@RequestBody CountryCodeDto countryCodeDto) {
        cardCostService.deleteClearingCost(countryCodeDto.getCountryCode());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    @Operation(summary = "Update clearing cost.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clearing cost updated successfully."),
            @ApiResponse(responseCode = "400", description = "Validation error."),
            @ApiResponse(responseCode = "404", description = "Clearing cost not found.")
    })
    public ResponseEntity<ClearingCostDto> updateClearingCost(@RequestBody ClearingCostDto cardNumberDto) {
        ClearingCostDto updatedDto = cardCostService.updateClearingCost(cardNumberDto);
        return new ResponseEntity<>(updatedDto, HttpStatus.OK);
    }

    @GetMapping("/{countryCode}")
    @Operation(summary = "Get clearing cost.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clearing cost retrieved successfully."),
            @ApiResponse(responseCode = "400", description = "Validation error."),
            @ApiResponse(responseCode = "404", description = "Clearing cost not found.")
    })
    public ResponseEntity<ClearingCostDto> getClearingCost(@PathVariable String countryCode) {
        ClearingCostDto clearingCost = cardCostService.getClearingCost(countryCode);
        return new ResponseEntity<>(clearingCost, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Get all clearing cost.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clearing costs retrieved successfully."),
            @ApiResponse(responseCode = "400", description = "Validation error."),
            @ApiResponse(responseCode = "404", description = "No clearing costs found.")
    })
    public ResponseEntity<List<ClearingCostDto>> getAllClearingCost() {
        List<ClearingCostDto> clearingCosts = cardCostService.getAllClearingCost();
        return new ResponseEntity<>(clearingCosts, HttpStatus.OK);
    }

    @PostMapping("/payment-cards-cost")
    @Operation(summary = "Get the cost of a card.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card cost retrieved successfully."),
            @ApiResponse(responseCode = "400", description = "Validation error."),
            @ApiResponse(responseCode = "404", description = "Card not found."),
            @ApiResponse(responseCode = "429", description = "Too many requests."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public ResponseEntity<ClearingCostDto> getCardCost(@RequestBody CardNumberDto cardNumberDto) throws Exception {
        ClearingCostDto cardCost = cardCostService.getCardCost(cardNumberDto.getCardNumber());
        return ResponseEntity.ok(cardCost);
    }

}
