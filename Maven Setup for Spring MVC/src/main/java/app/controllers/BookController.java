package app.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import app.entities.Book;

@RestController
public class BookController {
	
	@GetMapping("/books")
	public Book books() {
		
		var book = new Book("Charles Dickens", "Great expectations");
		return book;
	}
}
