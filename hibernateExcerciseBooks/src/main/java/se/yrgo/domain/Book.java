package se.yrgo.domain;

import java.util.*;

import jakarta.persistence.*;

@Entity
@Table(name = "Book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String title;
    private String genre;
    private int publicationYear;
    @ManyToMany(mappedBy = "books", cascade = CascadeType.PERSIST)
    private Set<Reader> bookReaders = new HashSet<>();

    public Book() {
    }

    public Book(String title, String genre, int publicationYear) {
        this.title = title;
        this.genre = genre;
        this.publicationYear = publicationYear;
    }

    @Override
    public String toString() {
        return String.format("""
                Title: %s
                Genre: %s
                Published: %d
                """, title, genre, publicationYear);
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Set<Reader> getBookReaders() {
        return bookReaders;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + publicationYear;
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
        Book other = (Book) obj;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        if (publicationYear != other.publicationYear)
            return false;
        return true;
    }

    public String getGenre() {
        return genre;
    }

    public int getPublicationYear() {
        return publicationYear;
    }
}
