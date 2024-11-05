package ru.practicum.dto.mainservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.dto.mainservice.model.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    @Query(value = "SELECT c.* " +
            "FROM compilations AS c " +
            "WHERE (:pinned IS NULL OR c.pinned = :pinned) " +
            "OFFSET :from " +
            "LIMIT :size",
            nativeQuery = true)
    List<Compilation> findCompilationsBy(@Param("pinned") Boolean pinned,
                                         @Param("from") Integer from,
                                         @Param("size") Integer size);
}
