package ru.petproject.socialnetwork.model;

import lombok.Getter;
import lombok.ToString;
import ru.petproject.socialnetwork.domain.Person;
import ru.petproject.socialnetwork.domain.Roll;
import ru.petproject.socialnetwork.security.SecurityUtils;
import ru.petproject.socialnetwork.service.AvatarService;

import java.io.Serializable;
import java.util.Date;


@Getter
@ToString
public class RollView implements Serializable {

    private Long id;
    private Long person_id;
    private String person_name;
    private String avatar;
    private String body;
    private Date posted;
    private int likes;

    public RollView(Roll roll) {
        final Person person = SecurityUtils.currentProfile();
        final Person rollPerson = roll.getPerson();

        this.id = roll.getId();
        this.person_id = rollPerson.getId();
        this.avatar = AvatarService.getPageAvatar(rollPerson.getId());
        this.person_name = rollPerson.getFullName();
        this.posted = roll.getPosted();
        this.body = roll.getBody().replace("\n", "\\n");
        this.likes = roll.getLikes() == null ? 0 : roll.getLikes();
    }
}

