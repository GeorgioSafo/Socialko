package ru.petproject.socialnetwork.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import ru.petproject.socialnetwork.domain.Gender;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactInformation implements Serializable{

	@NotNull
	private Long id;

	@NotEmpty
	@Size(min = 2, max = 50)
	private String firstName;

	@NotEmpty
	@Size(min = 2, max = 50)
	private String lastName;

	@NotEmpty
	@Email
	@Size(min = 5, max = 50)
	private String email;

	@NotEmpty
	@Pattern(regexp = "[0-9]{5,15}")
	private String phone;

	@NotNull
	@DateTimeFormat(pattern="MM/dd/yyyy") @Past
	private Date birthDate;

	@NotNull
	private Gender gender;

}
