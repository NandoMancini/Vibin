package com.nando.vibin.service;

import com.nando.vibin.model.Journal;
import com.nando.vibin.model.User;
import com.nando.vibin.repository.JournalRepository;
import com.nando.vibin.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JournalService {

    private final JournalRepository journalRepository;

    public JournalService(
            JournalRepository journalRepository
    ) {
        this.journalRepository = journalRepository;
    }

    public Journal createAndEditEntry(Journal journal) {
        return journalRepository.save(journal);
    }

    public void deleteEntry(Long id) {
        journalRepository.deleteById(id);
    }
}
