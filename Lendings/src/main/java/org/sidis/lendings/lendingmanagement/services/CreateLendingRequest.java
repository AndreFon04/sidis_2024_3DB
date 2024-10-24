package org.sidis.lendings.lendingmanagement.services;

import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CreateLendingRequest {

    @NotNull
    private Long bookID;

    @NotNull
    private String readerID;
}
