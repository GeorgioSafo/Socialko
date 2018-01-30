package ru.petproject.socialnetwork.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.petproject.socialnetwork.AbstractApplicationTest;
import ru.petproject.socialnetwork.domain.Message;
import ru.petproject.socialnetwork.domain.Person;

import javax.transaction.Transactional;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MessageServiceTest extends AbstractApplicationTest {

    @Autowired
    private MessageService messageService;

    private final Person person = getDefaultPerson();

    @Test
    public void shouldFindAllDialogMessagesWithPerson() throws Exception {
        final Person interlocutor = Person.builder()
                .id(1L)
                .build();
        final Collection<Message> messages = messageService.getDialog(person, interlocutor);

        assertThat(messages).hasSize(0);
    }

    @Test
    public void shouldFindAllLastMessagesByPerson() throws Exception {
        final Collection<Message> messages = messageService.getLastMessages(person);

        assertThat(messages).hasSize(1);
        assertThat(messages)
                .extracting("id", "body")
                .contains(
                        tuple(8L, "Привет, Витамин!"));
    }

    @Test
    public void shouldSaveMessage() throws Exception {
        final Collection<Message> before = messageService.getDialog(person, person);

        messageService.send(getDefaultMessage());

        final Collection<Message> after = messageService.getDialog(person, person);

        assertThat(before.size()).isEqualTo(after.size() - 1);
        assertThat(after)
                .extracting("body")
                .contains(DEFAULT_MESSAGE_TEXT);
    }

}
