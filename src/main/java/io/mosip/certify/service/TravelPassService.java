package io.mosip.certify.service;

import io.mosip.certify.entity.TravelPass;

public interface TravelPassService {
    TravelPass getPassById(String id);
    TravelPass createPass(TravelPass tp);
}
