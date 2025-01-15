package org.sidis.suggestion.command.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReaderDTO {

    private String name;
    private String readerID;
    private String email;
    private String birthdate;
    private String phoneNumber;

    public ReaderDTO() {
    }

    public ReaderDTO(final String name, final String email, final String birthdate,
                     final String phoneNumber, final String readerID) {
        this.name = name;
        this.email = email;
        this.birthdate = birthdate;
        this.phoneNumber = phoneNumber;
        this.readerID = readerID;
    }
}
