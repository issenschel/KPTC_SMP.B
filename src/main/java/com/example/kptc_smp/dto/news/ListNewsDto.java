package com.example.kptc_smp.dto.news;

import com.example.kptc_smp.entity.postgreSQL.News;
import lombok.Data;

import java.util.List;

@Data
public class ListNewsDto {
    List<News> news;
    int count;
}
