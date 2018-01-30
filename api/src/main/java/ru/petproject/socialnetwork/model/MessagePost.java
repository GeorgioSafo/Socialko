package ru.petproject.socialnetwork.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessagePost implements Serializable{

	@NotNull
	private Long sender;

	@NotNull
	private Long recipient;

	@NotEmpty
	@Size(min = 1, max = 1000)
	private String body;

}
