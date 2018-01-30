package ru.petproject.socialnetwork.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.petproject.socialnetwork.domain.Person;
import ru.petproject.socialnetwork.domain.Roll;
import ru.petproject.socialnetwork.repository.RollRepository;

import java.util.List;


@Service
public class RollService {

    private RollRepository rollRepository;

    @Autowired
    public RollService(RollRepository rollRepository) {
        this.rollRepository = rollRepository;
    }

    @Transactional(readOnly = true)
    public List<Roll> getCurrentRoll(Person person) {
        return rollRepository.getRoll(person.getId());
    }

}
