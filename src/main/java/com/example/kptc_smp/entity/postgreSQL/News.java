package com.example.kptc_smp.entity.postgreSQL;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "news")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "photo")
    private String photo;
}
