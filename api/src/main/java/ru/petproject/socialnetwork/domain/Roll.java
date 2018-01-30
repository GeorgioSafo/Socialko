package ru.petproject.socialnetwork.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "roll")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"person", "posted"})
@ToString(of = {"id", "body"})
public class Roll implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id")
    @Getter
    @Setter
    private Person person;

    @Getter
    @Setter
    @Column(nullable = false)
    private String body;

    @Column(updatable = false, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Getter
    private Date posted = new Date();

    @Getter
    @Setter
    @JsonIgnore
    private Integer likes;

}
