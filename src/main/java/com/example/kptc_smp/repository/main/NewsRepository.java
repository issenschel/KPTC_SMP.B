package com.example.kptc_smp.repository.main;

import com.example.kptc_smp.entity.main.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Integer> {
}
