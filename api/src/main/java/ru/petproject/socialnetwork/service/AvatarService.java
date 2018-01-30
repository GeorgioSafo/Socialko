package ru.petproject.socialnetwork.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.petproject.socialnetwork.config.Constants;

import java.io.File;

// TODO: 24.02.2017 Provide cache
@Service
public final class AvatarService {

	private static final Logger log = LoggerFactory.getLogger(AvatarService.class);
	private static final ClassLoader loader = AvatarService.class.getClassLoader();

	private AvatarService() {
		if (!new File(loader.getResource(Constants.AVATAR_FOLDER).getFile()).isDirectory()) {
			log.error("Avatar folder {} is not found", Constants.AVATAR_FOLDER);
		}
	}

	public static String getPageAvatar(Long id) {
		final String path = Constants.AVATAR_FOLDER + String.valueOf(id) + ".jpg";

		if (null != loader.getResource(path)) {
			return Constants.API_URL + "/" + path;
		}
		return Constants.API_URL + "/" + Constants.AVATAR_FOLDER + "anonymous.png";
	}

	public static String getAvatar(Long id, String fullName) {
		final String path = Constants.AVATAR_FOLDER + String.valueOf(id) + ".jpg";
		if (null != loader.getResource(path)) {
			return Constants.API_URL + "/" + path;
		}
		return fullName;
	}

}
