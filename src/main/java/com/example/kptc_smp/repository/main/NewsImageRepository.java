package com.example.kptc_smp.repository.main;

import com.example.kptc_smp.entity.main.NewsImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsImageRepository extends JpaRepository<NewsImage, Integer> {

}
