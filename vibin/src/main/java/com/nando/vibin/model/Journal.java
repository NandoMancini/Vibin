package com.nando.vibin.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "journal")
public class Journal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @CreationTimestamp                  // (optional) auto-fills a non-null date
    @Column(nullable = false)
    private Date date;
    private String title;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String body;
    private String emotion;

    @Column(name = "userId", nullable = false)
    private Long userId;

}
