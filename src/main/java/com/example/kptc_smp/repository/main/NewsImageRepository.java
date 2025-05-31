package com.example.kptc_smp.repository.main;

import com.example.kptc_smp.model.main.NewsImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsImageRepository extends CrudRepository<NewsImage, Integer> {

}
