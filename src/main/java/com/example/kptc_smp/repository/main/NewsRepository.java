package com.example.kptc_smp.repository.main;

import com.example.kptc_smp.entity.main.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {

    @EntityGraph(attributePaths = {"images"})
    Optional<News> findWithImagesById(Integer id);

    @EntityGraph(attributePaths = {"images", "images.imageRegistry"})
    Optional<News> findWithImagesAndRegistryById(Integer id);

    @Query("SELECT n.id FROM News n ORDER BY n.id DESC")
    Page<Integer> findNewsIds(Pageable pageable);

    @EntityGraph(attributePaths = {"images", "images.imageRegistry"})
    @Query("SELECT n FROM News n WHERE n.id IN :ids ORDER BY n.id DESC")
    List<News> findFullNewsByIds(@Param("ids") List<Integer> ids);
}
