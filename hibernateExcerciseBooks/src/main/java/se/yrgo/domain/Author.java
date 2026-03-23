package se.yrgo.domain;

import java.util.*;

import jakarta.persistence.*;

@Entity
@Table(name = "Author")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String nationality;
    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "AUTHOR_FK")
    private Set<Book> books;


    public Author() {
    }

    public Author(String name, String nationality) {
        this.name = name;
        this.nationality = nationality;
        this.books = new HashSet<>();
    }

    public void addBookToAuthor(Book book) {
        this.books.add(book);
    }

    public void createBookAndAddToAuthor (String title, String genre, int publicationYear) {
        Book book = new Book(title, genre, publicationYear);
        this.addBookToAuthor(book);
    }

    public Set<Book> getBooks() {
        return Collections.unmodifiableSet(this.books);
    }

    public String getName() {
        return name;
    }

        public String getNationality() {
        return nationality;
    }

    @Override
    public String toString() {
        return String.format("""
                Author: %s
                Nationality: %s
                Books: %s
                """, name, nationality, books);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((nationality == null) ? 0 : nationality.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Author other = (Author) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (nationality == null) {
            if (other.nationality != null)
                return false;
        } else if (!nationality.equals(other.nationality))
            return false;
        return true;
    }

    public int getId() {
        return id;
    }

}
