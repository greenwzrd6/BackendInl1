package se.yrgo.test;

import java.util.*;

import jakarta.persistence.*;
import se.yrgo.domain.*;

public class TestHibernate {
    public static void main(String[] args) {

        try (EntityManagerFactory eManagerFactory = Persistence.createEntityManagerFactory("databaseConfig");
                EntityManager em = eManagerFactory.createEntityManager();) {
            EntityTransaction tx = em.getTransaction();

            // Uppgift 1: Skapa och lagra data
            createExampleData(em, tx);

            // Uppgift 2: Hämta alla böcker av en specifik författare (JPQL)
            findAuthorBooks(em, "Birger");

            // Uppgift 3: Hämta alla läsare( readers) som har läst en viss bok (member of)
            findBookReaders(em, "Berit dödade min pappa");

            // Uppgift 4: Hämta författare vars böcker har lästs av minst en läsare (join)
            authorsWithMoreThanOneReader(em);

            // Uppgift 5: Räkna antalet böcker per författare (Aggregation Query)
            authorsAmountOfBooks(em);

            // Uppgift 6: Named Query - Hämta böcker efter genre
            getBooksByGenre(em, "Fakta");

            System.out.println("------------------------------");
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }

    private static void getBooksByGenre(EntityManager em, String genre) {
        // Skapa en Named Query i orm.xml som hämtar alla böcker baserat på genre.
        // Använd frågan i din kod för att hämta och skriva ut böcker av en viss genre.

        List<Book> books = em.createNamedQuery("searchByGenre",
                Book.class).setParameter("genre", genre).getResultList();

        System.out.println("--------- Uppgift 6 ----------");
        System.out.println();
        System.out.println("\tBöcker som har " + genre + " som genre:");
        System.out.println();
        if (books.isEmpty()) {
            System.out.println("Hittade inga böcker med " + genre + " som genre");
        }
        System.out.println("\t***************************");
        for (Book book : books) {
            System.out.printf("""
                    \tTitle: %s
                    \tGenre: %s
                    \t***************************
                    """, book.getTitle(), book.getGenre());
        }
    }

    private static void authorsAmountOfBooks(EntityManager em) {
        // Skapa en JPQL-fråga som räknar hur många böcker varje författare har skrivit.
        // Returnera författarens namn och antalet böcker.
        List<Object[]> result = em
                .createQuery("SELECT a.name, COUNT(b) FROM Author a LEFT JOIN a.books b GROUP BY a.name",
                        Object[].class)
                .getResultList();

        // Skriv ut resultaten.
        System.out.println("--------- Uppgift 5 ----------");
        System.out.println();
        System.out.println("\tVarje författares bokmängd:");
        System.out.println();
        if (result.isEmpty()) {
            System.out.println("Hittade inga författare");
        } else {
            System.out.println("\t***************************");
            for (Object[] obj : result) {
                System.out.printf("""
                        \tAuthor: %s
                        \tNumber of books: %d
                        \t***************************
                        """, obj[0], obj[1]);
            }
        }
        System.out.println();
    }

    private static void authorsWithMoreThanOneReader(EntityManager em) {
        // Skapa en JPQL-fråga som hämtar alla författare där minst en av deras böcker
        // har lästs av en läsare.
        // Använd JOIN för att koppla författare till läsare genom böcker.
        List<Author> authors = em.createQuery("FROM Author a JOIN a.books b JOIN b.bookReaders r", Author.class)
                .getResultList();

        // Hämta och skriv ut resultaten.
        System.out.println("--------- Uppgift 4 ----------");
        System.out.println();
        System.out.println("\tAlla författare som har läsare:");
        System.out.println();
        if (authors.isEmpty()) {
            System.out.println("Hittade inga författare som har läsare");
        } else {
            System.out.println("\t***************************");
            for (Author author : authors) {
                System.out.printf("""
                        \tAuthor: %s
                        \t***************************
                        """, author.getName());
            }

        }
        System.out.println();
    }

    private static void findBookReaders(EntityManager em, String bookName) {
        // Skapa en JPQL-fråga som returnerar alla läsare som har en specifik bok i sin
        // läslista.
        // Använd member of för att kontrollera om boken finns i läsarens lista.
        Book book = em.createQuery("FROM Book b WHERE b.title = :title",
                Book.class).setParameter("title", bookName).getSingleResult();

        List<Reader> readers = em.createQuery("FROM Reader r WHERE :book MEMBER OF r.books",
                Reader.class).setParameter("book", book).getResultList();
        // Hämta och skriv ut resultaten.
        System.out.println("--------- Uppgift 3 ----------");
        System.out.println();
        System.out.println("\tAlla som läser " + bookName + ":");
        System.out.println();
        if (readers.isEmpty()) {
            System.out.println("Det finns inga läsare för denna bok");
        } else {
            System.out.println("\t***************************");
            for (Reader reader : readers) {
                System.out.printf("""
                        \tName: %s
                        \tEmail: %s
                        \t***************************
                        """, reader.getName(), reader.getEmail());
            }
        }
        System.out.println();
    }

    private static void findAuthorBooks(EntityManager em, String authorName) {
        // Skapa en JPQL-fråga som hämtar en författare med ett visst namn
        Author author = em.createQuery("FROM Author a WHERE a.name = :name",
                Author.class).setParameter("name", authorName).getSingleResult();

        // Hämta böckerna från författarens lista över böcker.
        System.out.println("--------- Uppgift 2 ----------");
        System.out.println();
        System.out.println("\tBöcker av " + authorName + ":");
        System.out.println();
        System.out.println("\t***************************");
        for (Book book : author.getBooks()) {
            System.out.printf("""
                    \tTitel: %s
                    \tGenre: %s
                    \t***************************
                    """, book.getTitle(), book.getGenre());
        }
        System.out.println();
    }

    private static void createExampleData(EntityManager em, EntityTransaction tx) {
        tx.begin();

        // Skapa tre författare med olika nationaliteter.
        Author author1 = new Author("Birger", "Sverige");
        Author author2 = new Author("Ollie", "Australien");
        Author author3 = new Author("Beatrice", "England");
        Author author4 = new Author("Jean-Pierre", "Frankrike");
        Author author5 = new Author("Solveig", "Norge");
        Author author6 = new Author("Hiroshi", "Japan");
        Author author7 = new Author("Korven", "Korvstaden");
        // Skapa minst fem böcker och koppla dem till författarna.
        Book book1 = new Book("Bongo Bingo", "Komedi", 2000);
        Book book2 = new Book("Birgers Bok", "Fakta", 1967);
        Book book3 = new Book("Musikboken", "Fakta", 2007);
        Book book4 = new Book("Läskiga Boken", "Skräck", 1999);
        Book book5 = new Book("Berit dödade min pappa", "Däckare", 2022);
        Book book6 = new Book("Basker eller döden", "Poesi", 2024);
        Book book7 = new Book("Hur man överlever en arg isbjörn", "Äventyr", 1985);
        Book book8 = new Book("Cyber-Sushins återkomst", "Sci-Fi", 2077);
        Book book9 = new Book("Varför mormor bits", "Mysterium", 2010);
        Book book10 = new Book("Koda Java i sömnen", "Skräck", 2023);

        author1.addBookToAuthor(book2);
        author1.addBookToAuthor(book1);

        author2.addBookToAuthor(book1);
        author2.addBookToAuthor(book4);

        author3.addBookToAuthor(book3);
        author3.addBookToAuthor(book5);

        author4.addBookToAuthor(book6);
        author4.addBookToAuthor(book9);

        author5.addBookToAuthor(book7);

        author6.addBookToAuthor(book8);
        author6.addBookToAuthor(book10);
        // Skapa tre läsare och låt dem läsa olika böcker.
        Reader reader1 = new Reader("Börje", "burgaren@burger.com");
        Reader reader2 = new Reader("Felix", "flixmix@gmail.se");
        Reader reader3 = new Reader("Sanna", "santa@hotmail.it");
        Reader reader4 = new Reader("Gunnar", "ingen-reklam-tack@telia.se");
        Reader reader5 = new Reader("Luna", "pixel_princess@cybercore.net");
        Reader reader6 = new Reader("Kalle", "glassbilen_88@yahoo.com");

        reader1.addBook(book5);
        reader1.addBook(book3);

        reader2.addBook(book1);

        reader3.addBook(book5);
        reader3.addBook(book2);
        reader3.addBook(book4);

        reader4.addBook(book7);
        reader4.addBook(book10);
        // Spara all data i databasen.
        em.persist(author1);
        em.persist(author2);
        em.persist(author3);
        em.persist(author4);
        em.persist(author5);
        em.persist(author6);
        em.persist(author7);
        em.persist(reader1);
        em.persist(reader2);
        em.persist(reader3);
        em.persist(reader4);
        em.persist(reader5);
        em.persist(reader6);

        tx.commit();
    }
}
