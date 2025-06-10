package com.nando.vibin.repository;

import com.nando.vibin.model.Journal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JournalRepository extends CrudRepository<Journal, Long> {
    Optional<Journal> findById(Long id);
    boolean existsById(Long id);
    void deleteById(Long id);
    List<Journal> findAllByUserId(Long userId);

    List<Journal> getById(Long id);

}
