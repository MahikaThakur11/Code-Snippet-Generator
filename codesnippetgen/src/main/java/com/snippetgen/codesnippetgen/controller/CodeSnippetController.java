package com.snippetgen.codesnippetgen.controller;

import com.snippetgen.codesnippetgen.model.CodeSnippet;
import com.snippetgen.codesnippetgen.service.CodeSnippetService;
import com.snippetgen.codesnippetgen.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/snippets")
public class CodeSnippetController {

    private final CodeSnippetService snippetService;
    private final GeminiService geminiService;

    @Autowired
    public CodeSnippetController(CodeSnippetService snippetService, GeminiService geminiService) {
        this.snippetService = snippetService;
        this.geminiService = geminiService;
    }

    // Show all snippets
    @GetMapping
    public String listSnippets(Model model) {
        List<CodeSnippet> snippets = snippetService.getAllSnippets();
        model.addAttribute("snippets", snippets);
        return "list";  // Loads list.html from templates/
    }

    // Show form to add a new snippet
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("snippet", new CodeSnippet());
        return "form";  // Loads form.html from templates/
    }

    // ✅ Save a new or updated snippet
    @PostMapping("/save")
    public String saveSnippet(@ModelAttribute CodeSnippet snippet) {
        if (snippet.getTitle() == null || snippet.getTitle().trim().isEmpty()) {
            snippet.setTitle("Untitled Snippet");
        }
        if (snippet.getLanguage() == null || snippet.getLanguage().trim().isEmpty()) {
            snippet.setLanguage("Unknown");
        }
        if (snippet.getDescription() == null) {
            snippet.setDescription("No description available.");
        }
        if (snippet.getCode() == null || snippet.getCode().trim().isEmpty()) {
            return "redirect:/snippets/new?error=CodeRequired"; // Prevent empty snippets
        }

        snippetService.saveSnippet(snippet);
        return "redirect:/snippets";
    }

    // View snippet details
    @GetMapping("/{id}")
    public String viewSnippet(@PathVariable Long id, Model model) {
        Optional<CodeSnippet> snippet = snippetService.getSnippetById(id);
        if (snippet.isPresent()) {
            model.addAttribute("snippet", snippet.get());
            return "details"; // Loads details.html from templates/
        } else {
            return "redirect:/snippets?error=SnippetNotFound"; // Redirect with an error message
        }
    }

    // Delete a snippet
    @GetMapping("/delete/{id}")
    public String deleteSnippet(@PathVariable Long id) {
        snippetService.deleteSnippet(id);
        return "redirect:/snippets";
    }

    // Search snippets
    @GetMapping("/search")
    public String searchSnippets(@RequestParam String keyword, Model model) {
        List<CodeSnippet> snippets = snippetService.searchSnippets(keyword);
        model.addAttribute("snippets", snippets);
        return "list";
    }

    // ✅ Generate snippet using Gemini AI and save it
    @PostMapping("/generate")
    public String generateSnippet(@RequestParam String prompt, Model model) {
        String generatedCode = geminiService.generateSnippet(prompt);

        if (generatedCode == null || generatedCode.trim().isEmpty()) {
            return "redirect:/snippets/new?error=GenerationFailed";
        }

        CodeSnippet snippet = new CodeSnippet();
        snippet.setTitle("Generated Snippet");
        snippet.setLanguage("Unknown"); // Default language
        snippet.setDescription("Generated using Gemini AI.");
        snippet.setCode(generatedCode);

        snippetService.saveSnippet(snippet); // ✅ Save the generated snippet

        model.addAttribute("snippet", snippet);
        return "form";
    }
}
