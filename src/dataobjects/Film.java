package dataobjects;

import java.util.concurrent.atomic.AtomicInteger;

public class Film {
    private int filmID;
    private String name;
    private FilmType type;
    private boolean inStore;

    /*ID is required as there can be many copies of the same film
    and the ID is what differentiates the copies from each other
     */
    private static AtomicInteger ID_GENERATOR = new AtomicInteger(1000);


    public Film(String name, FilmType type) {
        this.filmID = ID_GENERATOR.getAndIncrement();
        this.name = name;
        this.type = type;
        this.inStore = true;
    }

    public int getFilmID() {
        return filmID;
    }

    public void setFilmID(int filmID) {
        this.filmID = filmID;
    }

    public boolean isInStore() {

        return inStore;
    }

    public void setInStore(boolean inStore) {
        this.inStore = inStore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FilmType getType() {
        return type;
    }

    public void setType(FilmType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return name + "(" + type + ")";
    }
}
