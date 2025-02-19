package io.mosip.certify.controller;

import io.mosip.certify.dto.ParsedAccessToken;
import io.mosip.certify.dto.TravelPassDTO;
import io.mosip.certify.entity.TravelPass;
import io.mosip.certify.service.TravelPassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EntityController {
    @Autowired
    TravelPassService travelPassService;
    @Autowired
    ParsedAccessToken parsedAccessToken;
    @PostMapping("/pass")
    public ResponseEntity<?> createEntity(@RequestBody TravelPassDTO t) {
        TravelPass tp = new TravelPass();
        tp.setPassName(t.getPassName());
        tp.setDestination(t.getDestination());
        tp.setSource(t.getSource());
        TravelPass resp = travelPassService.createPass(tp);
        return new ResponseEntity<>(resp, HttpStatus.CREATED);
    }

    @GetMapping("/pass/{id}")
    public ResponseEntity<?> getEntity(@PathVariable String id) {
        TravelPass tp;
        try {
            tp = travelPassService.getPassById(id);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(tp, HttpStatus.OK);
    }
}
