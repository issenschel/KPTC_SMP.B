package com.example.kptc_smp.repository.main;

import com.example.kptc_smp.entity.main.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {
}
