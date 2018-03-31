package logic;

import dao.RentalDAO;
import dataobjects.Film;
import dataobjects.FilmType;

import java.util.List;

public class Inventory {
    private final RentalDAO dao;

    public Inventory(RentalDAO dao) {
        this.dao = dao;
    }

    public void addFilm(Film film){
        //check if the item isn't already in the inventory
        if(dao.findFilm(film.getFilmID())==null){
            dao.saveFilm(film);
        }
        else{
            throw new RuntimeException("This item already is in the inventory.");
        }
    }

    public void removeFilm(Film film){
        //check if the item indeed exists in the inventory
        if(!(dao.findFilm(film.getFilmID())==null)){
            dao.removeFilm(film);
        }
        else{
            throw new RuntimeException("There is no such item to remove.");
        }
    }

    public void changeFilmType(Film film, FilmType newType){
        //check if the item indeed exists in the inventory
        if(!(dao.findFilm(film.getFilmID())==null)){
            Film changeable = dao.findFilm(film.getFilmID());
            changeable.setType(newType);
        }
        else{
            throw new RuntimeException("The film you want to change type of doesn't exist.");
        }
    }

    public List<Film> listAllFilms(){
        return dao.findAllFilms();
    }

    public List<Film> listFilmsInStore(){
        return dao.findFilmsInStore();
    }

    public String toStringListAllFilms(){
        List<Film> allFilms = listAllFilms();
        StringBuilder sb = new StringBuilder();
        for(Film f: allFilms){
            sb.append(f + System.lineSeparator());
        }
        return sb.toString();
    }
    public String toStringListFilmsInStore(){
        List<Film> filmsInStore = listFilmsInStore();
        StringBuilder sb = new StringBuilder();
        for(Film f: filmsInStore){
            sb.append(f + System.lineSeparator());
        }
        return sb.toString();
    }
}
