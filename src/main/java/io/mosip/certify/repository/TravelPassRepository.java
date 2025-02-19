package io.mosip.certify.repository;

import io.mosip.certify.entity.TravelPass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelPassRepository extends JpaRepository<TravelPass, String> {
}
