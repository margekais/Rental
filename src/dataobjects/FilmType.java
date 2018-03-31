package dataobjects;

//an enum type class is used because
public enum FilmType {
    NEWRELEASE ("New Release"),
    REGULARRENTAL ("Regular rental"),
    OLDFILM("Old film");

    private final String name;

    FilmType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
