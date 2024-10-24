package org.sidis.lendings.lendingmanagement.services;

import jakarta.persistence.Column;

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
