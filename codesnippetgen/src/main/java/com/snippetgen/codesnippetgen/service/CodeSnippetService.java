package com.snippetgen.codesnippetgen.service;

import com.snippetgen.codesnippetgen.model.CodeSnippet;
import com.snippetgen.codesnippetgen.repository.CodeSnippetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CodeSnippetService {

    private final CodeSnippetRepository snippetRepository;

    @Autowired
    public CodeSnippetService(CodeSnippetRepository snippetRepository) {
        this.snippetRepository = snippetRepository;
    }

    // Save a new code snippet
    public void saveSnippet(CodeSnippet snippet) {
        snippet.setCreatedAt(new Date()); // ✅ Set Date directly
        snippetRepository.save(snippet);
    }

    // Retrieve all code snippets
    public List<CodeSnippet> getAllSnippets() {
        return snippetRepository.findAll();
    }

    // Retrieve a snippet by ID
    public Optional<CodeSnippet> getSnippetById(Long id) {
        return snippetRepository.findById(id);
    }

    // Retrieve snippets by language
    public List<CodeSnippet> getSnippetsByLanguage(String language) {
        return snippetRepository.findByLanguage(language);
    }

    // Search snippets by title keyword
    public List<CodeSnippet> searchSnippets(String keyword) {
        return snippetRepository.findByTitleContainingIgnoreCase(keyword);
    }

    // Delete a snippet by ID
    public void deleteSnippet(Long id) {
        snippetRepository.deleteById(id);
    }
}
