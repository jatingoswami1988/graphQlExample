package com.example.DemoGraphQL;

import com.example.DemoGraphQL.exception.GraphQLErrorAdapter;
import com.example.DemoGraphQL.model.Author;
import com.example.DemoGraphQL.model.Book;
import com.example.DemoGraphQL.repository.AuthorRepository;
import com.example.DemoGraphQL.repository.BookRepository;
import com.example.DemoGraphQL.resolver.BookResolver;
import com.example.DemoGraphQL.resolver.Mutation;
import com.example.DemoGraphQL.resolver.Query;
import graphql.ExceptionWhileDataFetching;
import graphql.GraphQLError;
import graphql.servlet.GraphQLErrorHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class DemoGraphQlApplication implements CommandLineRunner{
	
	@Autowired
	private AuthorRepository authorRepository;

	public static void main(String[] args) {
		SpringApplication.run(DemoGraphQlApplication.class, args);
	}

	@Bean
	public GraphQLErrorHandler errorHandler() {
		return new GraphQLErrorHandler() {
			@Override
			public List<GraphQLError> processErrors(List<GraphQLError> errors) {
				List<GraphQLError> clientErrors = errors.stream()
						.filter(this::isClientError)
						.collect(Collectors.toList());

				List<GraphQLError> serverErrors = errors.stream()
						.filter(e -> !isClientError(e))
						.map(GraphQLErrorAdapter::new)
						.collect(Collectors.toList());

				List<GraphQLError> e = new ArrayList<>();
				e.addAll(clientErrors);
				e.addAll(serverErrors);
				return e;
			}

			protected boolean isClientError(GraphQLError error) {
				return !(error instanceof ExceptionWhileDataFetching || error instanceof Throwable);
			}
		};
	}

	@Bean
	public BookResolver authorResolver(AuthorRepository authorRepository) {
		return new BookResolver(authorRepository);
	}

	@Bean
	public Query query(AuthorRepository authorRepository, BookRepository bookRepository) {
		return new Query(authorRepository, bookRepository);
	}

	@Bean
	public Mutation mutation(AuthorRepository authorRepository, BookRepository bookRepository) {
		return new Mutation(authorRepository, bookRepository);
	}

	/*@Bean
	public CommandLineRunner demo(AuthorRepository authorRepository, BookRepository bookRepository) {
		return (args) -> {
			Author author = new Author("Herbert", "Schildt");
			authorRepository.save(author);

			bookRepository.save(new Book("Java: A Beginner's Guide, Sixth Edition", "0071809252", 728, author));
		};
	}*/

	@Override
	public void run(String... args) throws Exception {
		
		Author author = new Author("Herbert", "Schildt");
		List<Book> books = new ArrayList<>();
		books.add(new Book("Java: A Beginner's Guide, Sixth Edition", "0071809252", 728, author));
		books.add(new Book("Computer Network", "0071809253", 729, author));
		author.setBooks(books);
		authorRepository.save(author);
		
		Author author1 = new Author("Author ", "One");
		List<Book> books1 = new ArrayList<>();
		books1.add(new Book("Science", "0071809254", 730, author1));
		books1.add(new Book("Maths", "0071809255", 731, author1));
		author1.setBooks(books1);
		authorRepository.save(author1);
		
		Author author2 = new Author("Author", "Two");
		List<Book> books2 = new ArrayList<>();
		books2.add(new Book("English", "0071809256", 732, author2));
		books2.add(new Book("Hindi", "0071809257", 733, author2));
		author2.setBooks(books2);
		authorRepository.save(author2);
		
		Author author3 = new Author("Author", "Three");
		List<Book> books3 = new ArrayList<>();
		books3.add(new Book("Physical Education", "0071809258", 734, author3));
		books3.add(new Book("History", "0071809259", 735, author3));
		author3.setBooks(books3);
		authorRepository.save(author3);
		
	}
}
