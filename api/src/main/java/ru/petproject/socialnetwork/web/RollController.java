package ru.petproject.socialnetwork.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.petproject.socialnetwork.domain.Person;
import ru.petproject.socialnetwork.domain.Roll;
import ru.petproject.socialnetwork.model.RollView;
import ru.petproject.socialnetwork.security.CurrentProfile;
import ru.petproject.socialnetwork.service.PersonService;
import ru.petproject.socialnetwork.service.RollService;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static ru.petproject.socialnetwork.config.Constants.URI_API_PREFIX;
import static ru.petproject.socialnetwork.config.Constants.URI_ROLL;

@Api(tags = "Roll", description = "Roll operations")
@RestController
@RequestMapping(value = URI_API_PREFIX + URI_ROLL,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class RollController {

    private static final Logger log = LoggerFactory.getLogger(RollController.class);

    private final PersonService personService;
    private final RollService rollService;

    @Autowired
    public RollController(RollService rollService, PersonService personService) {
        this.rollService = rollService;
        this.personService = personService;
    }

    @ApiOperation(value = "Roll of a current person")
    @GetMapping(value = "/roll")
    public List<RollView> getRoll(
            @ApiIgnore @CurrentProfile Person profile) {
        log.debug("REST request to get roll of id:{} person", profile.getId());

        final List<Roll> roll = rollService.getCurrentRoll(profile);

        return map(roll);
    }

    private List<RollView> map(List<Roll> messages) {
        return messages.stream()
                .map(RollView::new)
                .collect(toList());
    }
}
