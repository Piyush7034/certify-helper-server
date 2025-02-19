package io.mosip.certify.service;

import io.mosip.certify.entity.TravelPass;
import io.mosip.certify.repository.TravelPassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TravelPassServiceImpl implements TravelPassService {
    @Autowired
    private TravelPassRepository travelPassRepository;

    @Override
    public TravelPass getPassById(String id) {
        Optional<TravelPass> t = travelPassRepository.findById(id);
        TravelPass travelPass = t.orElseThrow(() -> new RuntimeException("Travel pass not found"));
        return travelPass;
    }

    @Override
    public TravelPass createPass(TravelPass tp) {
        TravelPass  pass = travelPassRepository.save(tp);
        return pass;
    }
}
