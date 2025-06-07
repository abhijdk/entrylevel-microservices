package com.abhi.currency_conversion_service.controller;

import com.abhi.currency_conversion_service.entity.CurrencyConversion;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;

@RestController
public class CurrencyConversionController {
//http://localhost:8100/currency-conversion/from/{from}/to/{to}/quantity/{quantity}
    @GetMapping ("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateCurrencyCOnversion(
                            @PathVariable String from,
                            @PathVariable String to,
                            @PathVariable BigDecimal quantity

                                                          ){
        HashMap<String,String> uriVariable = new HashMap<>();
        uriVariable.put("from",from);
        uriVariable.put("to",to);

        ResponseEntity<CurrencyConversion> responseEntity = new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/USD/to/INR",
                CurrencyConversion.class,
                uriVariable

        );

        CurrencyConversion currencyConversion = responseEntity.getBody();

BigDecimal totalCalculatedAmount=quantity.multiply(currencyConversion.getConversionMultiple());
        return new CurrencyConversion(currencyConversion.getId(),from,to, currencyConversion.getConversionMultiple(),quantity,totalCalculatedAmount,currencyConversion.getEnvironment());
    }


}
