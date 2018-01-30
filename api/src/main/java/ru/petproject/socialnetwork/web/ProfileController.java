package ru.petproject.socialnetwork.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.petproject.socialnetwork.config.Constants;
import ru.petproject.socialnetwork.domain.Person;
import ru.petproject.socialnetwork.model.ChangePassword;
import ru.petproject.socialnetwork.model.ContactInformation;
import ru.petproject.socialnetwork.model.PersonView;
import ru.petproject.socialnetwork.model.SignUp;
import ru.petproject.socialnetwork.security.CurrentProfile;
import ru.petproject.socialnetwork.service.PersonService;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@Api(tags = "Profile", description = "User settings")
@RestController
@RequestMapping(
        value = Constants.URI_API_PREFIX,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileController {

    private static final Logger log = LoggerFactory.getLogger(ProfileController.class);

    private final PersonService personService;

    @Autowired
    public ProfileController(PersonService personService) {
        this.personService = personService;
    }

    @ApiOperation(value = "Sign-In")
    @GetMapping("/login")
    public ResponseEntity<PersonView> login(@ApiIgnore @CurrentProfile Person profile) {
        log.debug("REST request to get current profile: {}", profile);

        if (null == profile) {
            log.warn("Attempt getting unauthorised profile information failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(new PersonView(profile));
    }

    @ApiOperation(value = "Sign-Up")
    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUp person) throws URISyntaxException {
        log.debug("REST request to sign up a new profile: {}", person);

        final Person result = personService.findByEmail(person.getUserName());
        if (null != result) {
            log.debug("Attempt sign up email: {} failed! E-mail is already used by another contact: {}",
                    person.getUserName(), result);

            return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN).body(Constants.ERROR_SIGN_UP_EMAIL);
        }

        final Person profile = personService.create(
                person.getFirstName(),
                person.getLastName(),
                person.getUserName(),
                person.getPassword());

        return ResponseEntity.created(new URI(Constants.URI_API_PREFIX + "/person/" + profile.getId())).build();
    }

    @ApiOperation(value = "Change contact information")
    @PutMapping("/updateContact")
    public ResponseEntity<String> updatePerson(
            @ApiIgnore @CurrentProfile Person profile,
            @Valid @RequestBody ContactInformation contact) {
        log.debug("REST request to update current profile: {} contact information", profile);

        if (!profile.getId().equals(contact.getId())) {
            log.error("Updating profile: {} doesn't match the current one: {}", contact, profile);

            return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN).body(Constants.ERROR_UPDATE_PROFILE);
        }

        final String oldEmail = profile.getEmail();
        final String newEmail = contact.getEmail();
        if (!oldEmail.equals(newEmail)) {
            final Person result = personService.findByEmail(newEmail);
            if (null != result) {
                log.debug("Attempt to change email value from: {} to  {} failed! " +
                        "E-mail is already used by another contact : {}", result);

                return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN).body(Constants.ERROR_UPDATE_EMAIL);
            }
        }

        profile.setFirstName(contact.getFirstName());
        profile.setLastName(contact.getLastName());
        profile.setEmail(contact.getEmail());
        profile.setPhone(contact.getPhone());
        profile.setBirthDate(contact.getBirthDate());
        profile.setGender(contact.getGender());
        personService.update(profile);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Change password")
    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(
            @ApiIgnore @CurrentProfile Person profile,
            @Valid @RequestBody ChangePassword pwd) throws URISyntaxException {
        log.debug("REST request to change pwd: {}", pwd);

        if (null == profile) {
            log.warn("Attempt to change unauthorised profile password failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        final String currentPwd = pwd.getCurrentPassword();
        final String newPwd = pwd.getPassword();
        if (!personService.hasValidPassword(profile, currentPwd)) {
            log.warn("Current password: {} doesn't match profile's one: {}", currentPwd, profile);
            return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN).body(Constants.ERROR_PASSWORD_CONFIRMATION);
        }

        personService.changePassword(profile, newPwd);

        return ResponseEntity.ok().build();
    }

}
