package logic;

import dao.InMemoryRentalDAO;
import dao.RentalDAO;
import dataobjects.Customer;
import dataobjects.Film;
import dataobjects.FilmType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {
    private RentalDAO dao;
    private Inventory inventory;

    @BeforeEach
    void setUp() {
        dao = new InMemoryRentalDAO();
        inventory = new Inventory(dao);
    }

    @Test
    void addFilm() {
        Film film = new Film("Matrix 11", FilmType.NEWRELEASE);
        inventory.addFilm(film);
        assertEquals(film,dao.findFilm(film.getFilmID()));
    }

    @Test
    void removeFilmValid() {
        List<Film> films = inventory.listAllFilms();
        Film film = films.get(0);

        inventory.removeFilm(film);
        assertEquals(null,dao.findFilm((film.getFilmID())));
    }

    @Test
    void removeFilmUnknownFilm() {
        Film film = new Film("Maasikas",FilmType.NEWRELEASE);
        String expectedResult = "There is no such item to remove.";

        try{
            inventory.removeFilm(film);
        }
        catch (RuntimeException e){
            assertEquals(expectedResult,e.getMessage());
        }
    }

    @Test
    void changeFilmTypeValid() {
        List<Film> films = inventory.listAllFilms();
        Film film = films.get(0);
        FilmType expectedResult = FilmType.REGULARRENTAL;

        inventory.changeFilmType(film,expectedResult);

        assertEquals(expectedResult,film.getType());
    }

    @Test
    void changeFilmTypeUnknownFilm() {
        Film film = new Film("Maasikas",FilmType.NEWRELEASE);
        FilmType newType = FilmType.REGULARRENTAL;
        String expectedResult = "The film you want to change the type of doesn't exist.";

        try{
            inventory.changeFilmType(film,newType);
        }
        catch (RuntimeException e){
            assertEquals(expectedResult,e.getMessage());
        }
    }

    @Test
    void changeFilmTypeSameType() {
        List<Film> films = inventory.listAllFilms();
        Film film = films.get(0);
        FilmType newType = film.getType();
        String expectedResult = "The film is already of that type.";

        try{
            inventory.changeFilmType(film,newType);
        }
        catch (RuntimeException e){
            assertEquals(expectedResult,e.getMessage());
        }
    }

    @Test
    void toStringListAllFilms() {
        List<Film> films = inventory.listAllFilms();
        StringBuilder sb = new StringBuilder();
        sb.append("All films: " + System.lineSeparator());
        for(Film f: films){
            sb.append(f + System.lineSeparator());
        }
        String expectedResult = sb.toString();

        assertEquals(expectedResult,inventory.toStringListAllFilms());
    }

    @Test
    void toStringListFilmsInStore() {
        List<Film> films = inventory.listFilmsInStore();
        StringBuilder sb = new StringBuilder();
        sb.append("All currently available films: " + System.lineSeparator());
        for(Film f: films){
            sb.append(f + System.lineSeparator());
        }
        String expectedResult = sb.toString();

        assertEquals(expectedResult,inventory.toStringListFilmsInStore());
    }
}