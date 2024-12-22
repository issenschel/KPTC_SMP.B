package com.example.kptc_smp.repository.postgreSQL;

import com.example.kptc_smp.entity.postgreSQL.News;
import org.springframework.data.repository.CrudRepository;

public interface NewsRepository extends CrudRepository<News, Integer> {
}
