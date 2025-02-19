package io.mosip.certify.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "travelpass")
public class TravelPass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment for pass_id
    private Long passId;

    private String passName;

    @Column
    private String source;

    @Column
    private String destination;
}
