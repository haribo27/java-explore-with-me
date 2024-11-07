package ru.practicum.dto.mainservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.mainservice.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {


    @Query(value = "SELECT c.* " +
            "from categories as c " +
            "limit ?2 " +
            "offset ?1 ",
             nativeQuery = true)
    List<Category> findCategoryBy(Integer from, Integer size);

    Optional<Category> findByName(String name);
}
