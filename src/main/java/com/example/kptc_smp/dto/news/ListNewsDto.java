package com.example.kptc_smp.dto.news;

import lombok.Data;

import java.util.List;

@Data
public class ListNewsDto {
    List<NewsResponseDto> news;
    int count;
}
