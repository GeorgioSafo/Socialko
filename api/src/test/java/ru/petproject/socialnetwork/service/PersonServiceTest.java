package ru.petproject.socialnetwork.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;
import ru.petproject.socialnetwork.AbstractApplicationTest;
import ru.petproject.socialnetwork.domain.Gender;
import ru.petproject.socialnetwork.domain.Person;

import javax.transaction.Transactional;
import java.util.GregorianCalendar;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PersonServiceTest extends AbstractApplicationTest {

    @Autowired
    private PersonService personService;

    @Test
    public void shouldFindPersonWithCorrectIdAndFields() throws Exception {
        final Person person = personService.findById(1L);

        assertThat(person.getId()).isEqualTo(1L);
        assertThat(person.getFirstName()).isEqualTo("Gevork");
        assertThat(person.getLastName()).isEqualTo("Safaryan");
        assertThat(person.getShortName()).isEqualTo("georgiosafo");
        assertThat(person.getFullName()).isEqualTo("Gevork Safaryan");
        assertThat(person.getEmail()).isEqualTo("georgiosafo@gmail.com");
        assertThat(person.getPhone()).isEqualTo("79164170990");
        assertThat(person.getBirthDate()).isEqualTo(new GregorianCalendar(1991, 1, 24).getTime());
        assertThat(person.getGender()).isEqualTo(Gender.MALE);
        //...
    }

    @Test
    public void shouldFindPersonWithCorrectEmail() throws Exception {
        final Person person = personService.findByEmail("georgiosafo@gmail.com");

        assertThat(person.getId()).isEqualTo(1L);
        assertThat(person.getEmail()).isEqualTo("georgiosafo@gmail.com");
    }

    @Test
    public void shouldFindAllPeople() throws Exception {
        final Page<Person> people = personService.getPeople("", getDefaultPageRequest());

        assertThat(people).hasSize(2);
        assertThat(people)
                .extracting("id", "fullName")
                .contains(
                        tuple(1L, "Gevork Safaryan"),
                        tuple(2L, "Il Rastorguev"));
    }

    @Test
    public void shouldFindAllFriends() throws Exception {
        final Person person = personService.findById(1L);
        final Page<Person> friends = personService.getFriends(person, "", getDefaultPageRequest());

        assertThat(friends).hasSize(1);
        assertThat(friends)
                .extracting("id", "fullName")
                .contains(
                        tuple(2L, "Il Rastorguev"));
    }

    @Test
    public void shouldFindAllFriendOf() throws Exception {
        final Person person = personService.findById(1L);
        final Page<Person> friendOf = personService.getFriendOf(person, "", getDefaultPageRequest());

        assertThat(friendOf).hasSize(1);
        assertThat(friendOf)
                .extracting("id", "fullName")
                .contains(
                        tuple(2L, "Il Rastorguev"));
    }

    @Test
    public void shouldFindAPerson() throws Exception {
        final Person person = personService.findById(1L);

        assertThat(person)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("fullName", "Gevork Safaryan");
    }

    @Test
    public void shouldUpdatePersonInformation() throws Exception {
        final Person person = personService.findById(1L);
        person.setGender(Gender.UNDEFINED);
        personService.update(person);

        final Person result = personService.findById(person.getId());

        assertThat(result)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("fullName", "Gevork Safaryan")
                .hasFieldOrPropertyWithValue("gender", Gender.UNDEFINED);
    }

    @Test
    public void shouldChangePassword() throws Exception {
        final Person person = personService.findById(1L);
        final String currentPwd = "12345";
        final String newPwd = "54321";

        assertTrue(personService.hasValidPassword(person, currentPwd));
        assertFalse(personService.hasValidPassword(person, newPwd));

        personService.changePassword(person, newPwd);

        assertFalse(personService.hasValidPassword(person, currentPwd));
        assertTrue(personService.hasValidPassword(person, newPwd));
    }

    @Test
    public void shouldCreateNewPerson() throws Exception {
        final Person actual = personService.create(
                "John",
                "Doe",
                "john.doe@gmail.com",
                "johnny");

        final Person expected = personService.findByEmail("john.doe@gmail.com");

        assertThat(actual)
                .hasFieldOrPropertyWithValue("id", expected.getId())
                .hasFieldOrPropertyWithValue("fullName", expected.getFullName())
                .hasFieldOrPropertyWithValue("email", expected.getEmail());
    }

}
