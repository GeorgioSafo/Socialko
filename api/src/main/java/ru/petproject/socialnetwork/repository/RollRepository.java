package ru.petproject.socialnetwork.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import ru.petproject.socialnetwork.domain.Person;
import ru.petproject.socialnetwork.domain.Roll;

public interface RollRepository extends PagingAndSortingRepository<Roll, Long> {

    @EntityGraph(value = "some.entity.graph", type = EntityGraph.EntityGraphType.FETCH)
    @Query(value ="SELECT r FROM Roll r",countQuery = "SELECT COUNT(1)  FROM Roll r")
    Page<Roll> getRoll(@Param("person") Person person,
                       Pageable pageRequest);
}
