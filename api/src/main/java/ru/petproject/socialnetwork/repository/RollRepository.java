package ru.petproject.socialnetwork.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import ru.petproject.socialnetwork.domain.Roll;

import java.util.List;

public interface RollRepository extends PagingAndSortingRepository<Roll, Long> {

    @Query(nativeQuery = true, value = "SELECT roll.*," +
            "(SELECT COUNT(*) FROM likes where likes.roll_id = roll.id) AS likes" +
            " FROM roll" +
            " where roll.person_id in (SELECT friends.friend_id FROM friends" +
            " WHERE friends.person_id =:person) or roll.person_id =:person")
    List<Roll> getRoll(@Param("person") Long id);
}
