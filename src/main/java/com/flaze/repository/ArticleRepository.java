package com.flaze.repository;

import com.flaze.entity.ArticleEntity;
import com.flaze.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {
    Boolean existsByTitle(String title);

    Optional<ArticleEntity> findByTitle(String title);

    List<ArticleEntity> findByAuthorId(Long authorId);
    List<ArticleEntity> findByAuthorUsername(String authorUsername);

}



