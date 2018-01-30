package ru.petproject.socialnetwork.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.petproject.socialnetwork.domain.Person;
import ru.petproject.socialnetwork.repository.PersonRepository;

@Service
public class PersonService {

    private PasswordEncoder passwordEncoder;

	private PersonRepository personRepository;

	@Autowired
	public PersonService(PasswordEncoder passwordEncoder, PersonRepository personRepository) {
		this.passwordEncoder = passwordEncoder;
		this.personRepository = personRepository;
	}

	@Transactional(readOnly = true)
	public Person findById(Long id) {
		return personRepository.findOne(id);
	}

	@Transactional(readOnly = true)
	public Person findByEmail(String email) {
		return personRepository.findByEmail(email);
	}

	@Transactional(readOnly = true)
	public Page<Person> getPeople(String searchTerm, Pageable pageRequest) {
		return personRepository.findPeople(searchTerm, pageRequest);
	}

	@Transactional(readOnly = true)
	public Page<Person> getFriends(Person person, String searchTerm, Pageable pageRequest) {
		return personRepository.findFriends(person, searchTerm, pageRequest);
	}

	@Transactional(readOnly = true)
	public Page<Person> getFriendOf(Person person, String searchTerm, Pageable pageRequest) {
		return personRepository.findFriendOf(person, searchTerm, pageRequest);
	}

	@Transactional
	public void addFriend(Person person, Person friend) {
		if (!person.hasFriend(friend)) {
			person.addFriend(friend);
		}
	}

	@Transactional
	public void removeFriend(Person person, Person friend) {
		if (person.hasFriend(friend)) {
			person.removeFriend(friend);
        }
	}

	@Transactional
	public void update(Person person) {
		personRepository.save(person);
	}

	@Transactional
	public Person create(String firstName, String lastName, String email, String password) {
		final Person person = Person.builder()
				.firstName(firstName)
				.lastName(lastName)
				.email(email)
				.password(passwordEncoder.encode(password))
				.build();

		return personRepository.save(person);
	}

	public boolean hasValidPassword(Person person, String pwd) {
	    return passwordEncoder.matches(pwd, person.getPassword());
	}

    public void changePassword(Person person, String pwd) {
	    person.setPassword(passwordEncoder.encode(pwd));
        personRepository.save(person);
    }

}
