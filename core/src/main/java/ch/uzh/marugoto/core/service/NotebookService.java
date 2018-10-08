package ch.uzh.marugoto.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.uzh.marugoto.core.data.entity.PersonalNote;
import ch.uzh.marugoto.core.data.entity.User;
import ch.uzh.marugoto.core.data.repository.PersonalNoteRepository;
import ch.uzh.marugoto.core.exception.PageStateNotFoundException;

@Service
public class NotebookService {

    @Autowired
    private PersonalNoteRepository personalNoteRepository;

    public PersonalNote createPersonalNote(String text, User user) throws PageStateNotFoundException {
        if (user.getCurrentlyAt() == null) {
            throw new PageStateNotFoundException();
        }

        PersonalNote personalNote = new PersonalNote(text);
        personalNote.setNoteFrom(user.getCurrentlyAt());
        personalNoteRepository.save(personalNote);

        return personalNote;
    }

    public PersonalNote getPersonalNote(String personalNoteId) {
        return personalNoteRepository.findById(personalNoteId).orElseThrow();
    }

    public PersonalNote updatePersonalNote(String id, String text) {
        PersonalNote personalNote = personalNoteRepository.findById(id).orElseThrow();
        personalNote.setMarkdownContent(text);
        personalNoteRepository.save(personalNote);
        return personalNote;
    }

    public void deletePersonalNote(String id) {
        PersonalNote personalNote = personalNoteRepository.findById(id).orElseThrow();
        personalNoteRepository.delete(personalNote);
    }
}