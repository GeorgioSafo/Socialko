package ru.petproject.socialnetwork;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import ru.petproject.socialnetwork.domain.Message;
import ru.petproject.socialnetwork.domain.Person;
import ru.petproject.socialnetwork.model.MessagePost;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.GregorianCalendar;

public abstract class AbstractApplicationTest {

	protected final static String DEFAULT_MESSAGE_TEXT = "Lorem ipsum dolor sit amet...";

	protected static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
			MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(),
			Charset.forName("utf8"));

	protected static Person getDefaultPerson() {
		return Person.builder()
				.id(1L)
				.firstName("Gevork")
				.lastName("Safaryan")
				.shortName("georgiosafo")
				.email("georgiosafo@gmail.com")
				.password("12345")
				.birthDate(new GregorianCalendar(1991, 1, 24).getTime())
				.phone("79164170990")
				.build();
	}

	protected static Message getDefaultMessage() {
		final Person person = getDefaultPerson();
		final Message msg = new Message();
		msg.setSender(person);
		msg.setRecipient(person);
		msg.setBody(DEFAULT_MESSAGE_TEXT);
		return msg;
	}

	protected static MessagePost getDefaultMessagePost(Person person) {
		return new MessagePost(
				person.getId(),
				person.getId(),
				DEFAULT_MESSAGE_TEXT);
	}

	protected static Pageable getDefaultPageRequest() {
		return new PageRequest(0, 20);
//	    return new PageRequest(1,
//			    10,
//			    new Sort(Sort.Direction.DESC, "description")
//					    .and(new Sort(Sort.Direction.ASC, "title")));
	}

	protected static byte[] convertObjectToJsonBytes(Object object) throws IOException {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper.writeValueAsBytes(object);
	}

}
