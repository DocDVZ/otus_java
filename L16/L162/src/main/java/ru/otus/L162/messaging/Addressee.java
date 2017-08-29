package ru.otus.L162.messaging;

/**
 * Created by DocDVZ on 27.08.2017.
 */
public class Addressee {

    private String name;

    public Addressee(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Addressee addressee = (Addressee) o;

        return name != null ? name.equals(addressee.name) : addressee.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
