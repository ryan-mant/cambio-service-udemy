package com.ryan.cambio_service.controller;

import com.ryan.cambio_service.model.Cambio;
import com.ryan.cambio_service.repository.CambioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RestController
@RequestMapping("/api/cambio")
@CrossOrigin("*")
@RequiredArgsConstructor
public class CambioController {
    private final Environment environment;
    private final CambioRepository repository;

    @GetMapping
    public ResponseEntity<Cambio> getCambio(
            @RequestParam(name = "amount", defaultValue = "1.0") BigDecimal amount,
            @RequestParam(name = "from", defaultValue = "USD") String from,
            @RequestParam(name = "to", defaultValue = "BRL") String to
    ) {
        var cambio = repository.findByFromAndTo(from, to);
        if (cambio == null) throw new RuntimeException("Currency not found");

        var port = environment.getProperty("local.server.port");

        BigDecimal conversionFactor = cambio.getConversionFactor();
        BigDecimal convertedValue = conversionFactor.multiply(amount);
        cambio.setConvertedValue(convertedValue.setScale(2, RoundingMode.CEILING));
        cambio.setEnvironment(port);

        return ResponseEntity.ok(cambio);
    }
}
