package com.example.kptc_smp.entity.main;

import com.example.kptc_smp.enums.NewsImageRole;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "news_image")
public class NewsImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "news_id")
    private News news;

    @ManyToOne
    @JoinColumn(name = "image_registry_id")
    private ImageRegistry imageRegistry;

    @Enumerated(EnumType.STRING)
    @Column(name = "image_role")
    private NewsImageRole newsImageRole;
}