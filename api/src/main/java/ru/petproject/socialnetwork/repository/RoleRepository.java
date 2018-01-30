package ru.petproject.socialnetwork.repository;

import org.springframework.data.repository.CrudRepository;
import ru.petproject.socialnetwork.domain.Role;

import java.util.Set;

public interface RoleRepository extends CrudRepository<Role, Long> {

    Set<Role> findAll();

    Role findByName(String name);

}
