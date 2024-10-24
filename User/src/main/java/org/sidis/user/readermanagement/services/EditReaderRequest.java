package org.sidis.user.readermanagement.services;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditReaderRequest {

	@Size(max=150)
	private String name;

	@Size(min=8)
	private String password;

	private String rePassword;

	@Email
	private String email; // serves as username

	private String birthdate;

	@Pattern(regexp = "[1-9][0-9]{8}")
	private String phoneNumber;

	private boolean GDPR;

	private Set<String> interests;
}
