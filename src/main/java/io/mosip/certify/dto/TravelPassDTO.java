package io.mosip.certify.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TravelPassDTO {
    String passName;
    String source;
    String destination;
}
