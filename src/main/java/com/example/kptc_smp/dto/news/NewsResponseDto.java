package com.example.kptc_smp.dto.news;

import lombok.Data;

@Data
public class NewsResponseDto {
    private Integer id;
    private String title;
    private String content;
    private byte[] photo;
}
