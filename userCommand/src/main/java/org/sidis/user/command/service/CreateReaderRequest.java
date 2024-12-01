package org.sidis.user.command.service;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class CreateReaderRequest {
    @NonNull
    @NotBlank
    @Size(max=150)
    private String name;

    @NonNull
    @NotBlank
    @Size(min=8)
    private String password;

    @NonNull
    @NotBlank
    private String rePassword;

    @NonNull
    @NotBlank
    @Email
    private String email;

    @NonNull
    @NotBlank
    private String birthdate;

    @NonNull
    @NotBlank
    @Pattern(regexp = "[1-9][0-9]{8}")
    private String phoneNumber;

    private boolean GDPR;

    private Set<String> interests;

    public CreateReaderRequest(final String name, final String password, final String email, final String birthdate,
                               final String phoneNumber, final boolean GDPR, final Set<String> interests) {
        this.name = name;
        this.password = password;
        this.rePassword = password;
        this.email = email;
        this.birthdate = birthdate;
        this.phoneNumber = phoneNumber;
        this.GDPR = GDPR;
        this.interests = interests;
    }
}
