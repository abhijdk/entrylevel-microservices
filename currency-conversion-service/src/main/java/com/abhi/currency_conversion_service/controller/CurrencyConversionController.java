package com.abhi.currency_conversion_service.controller;

import com.abhi.currency_conversion_service.entity.CurrencyConversion;
import com.abhi.currency_conversion_service.feign.CurrencyExchangeProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;

@RestController
public class CurrencyConversionController {

    @Autowired
    private CurrencyExchangeProxy currencyExchangeProxy;

//http://localhost:8100/currency-conversion/from/{from}/to/{to}/quantity/{quantity}
@GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
public CurrencyConversion calculateCurrencyConversion(
        @PathVariable String from,
        @PathVariable String to,
        @PathVariable BigDecimal quantity) {

    HashMap<String, String> uriVariables = new HashMap<>();
    uriVariables.put("from", from);
    uriVariables.put("to", to);

    ResponseEntity<CurrencyConversion> responseEntity = new RestTemplate().getForEntity(
            "http://localhost:8000/currency-exchange/from/{from}/to/{to}",
            CurrencyConversion.class,
            uriVariables
    );

    CurrencyConversion currencyConversion = responseEntity.getBody();

    BigDecimal totalCalculatedAmount = quantity.multiply(currencyConversion.getConversionMultiple());

    return new CurrencyConversion(
            currencyConversion.getId(),
            from,
            to,
            currencyConversion.getConversionMultiple(),
            quantity,
            totalCalculatedAmount,
            currencyConversion.getEnvironment()
    );
}


    @GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateCurrencyConversionFeign(
            @PathVariable String from,
            @PathVariable String to,
            @PathVariable BigDecimal quantity) {


        CurrencyConversion currencyConversion = currencyExchangeProxy.retriveExchange(from,to);

        BigDecimal totalCalculatedAmount = quantity.multiply(currencyConversion.getConversionMultiple());

        return new CurrencyConversion(
                currencyConversion.getId(),
                from,
                to,
                currencyConversion.getConversionMultiple(),
                quantity,
                totalCalculatedAmount,
                currencyConversion.getEnvironment()+" "+"feign"
        );
    }



}
