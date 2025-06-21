package com.iscte_meta_systems.evoting_server.entities;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Election {

    @Id
    @GeneratedValue
    private Long id;
    String name;
    String description;
    LocalDateTime startDate;
    LocalDateTime endDate;
    //List<Results> results;
    //List<Vote> votes;
    //List<Hash> voted


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
