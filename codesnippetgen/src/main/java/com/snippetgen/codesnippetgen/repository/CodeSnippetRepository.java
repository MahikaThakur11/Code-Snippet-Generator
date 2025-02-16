package com.snippetgen.codesnippetgen.repository;

import com.snippetgen.codesnippetgen.model.CodeSnippet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeSnippetRepository extends JpaRepository<CodeSnippet, Long> {

    // Custom query to find snippets by language
    List<CodeSnippet> findByLanguage(String language);

    // Search snippets by title containing a keyword (case-insensitive)
    List<CodeSnippet> findByTitleContainingIgnoreCase(String keyword);
}
