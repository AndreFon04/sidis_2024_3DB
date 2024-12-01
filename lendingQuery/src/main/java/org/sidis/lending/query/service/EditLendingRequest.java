package org.sidis.lending.query.service;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditLendingRequest {

    @Column(nullable = true)
    @Getter
    @Setter
    private LocalDate returnDate;

    @Column(nullable = true)
    @Getter
    @Setter
    private String notes;
}
