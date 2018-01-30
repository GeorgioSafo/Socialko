package ru.petproject.socialnetwork.model;

import lombok.Getter;
import lombok.ToString;
import ru.petproject.socialnetwork.domain.Message;
import ru.petproject.socialnetwork.domain.Person;
import ru.petproject.socialnetwork.security.SecurityUtils;
import ru.petproject.socialnetwork.service.AvatarService;

import java.io.Serializable;
import java.util.Date;

@Getter
@ToString
public class MessageView implements Serializable {

	private Long senderId;
	private String senderName;
	private Long recipientId;
	private String recipientName;
	private String body;
	private Long interlocutor;
	private String avatar;
	private Date posted;

	public MessageView(Message message) {
		final Person profile = SecurityUtils.currentProfile();
		final Person sender = message.getSender();
		final Person recipient = message.getRecipient();

		this.senderId = sender.getId();
		this.senderName = sender.getFullName();
		this.recipientId = recipient.getId();
		this.recipientName = recipient.getFullName();
		this.body = message.getBody().replace("\n", "\\n");
		this.posted = message.getPosted();
		if (profile.getId().equals(sender.getId())) {
			this.interlocutor = recipient.getId();
			this.avatar = AvatarService.getAvatar(recipient.getId(), recipient.getFullName());
		} else {
			this.interlocutor = sender.getId();
			this.avatar = AvatarService.getAvatar(sender.getId(), sender.getFullName());
		}
	}

}
