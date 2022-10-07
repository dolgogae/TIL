package hellojpa;

import javax.persistence.Entity;

@Entity
public class Movie extends Item{
    private String actor;
    private String director;

    public String getActor() {
        return this.actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getDirector() {
        return this.director;
    }

    public void setDirector(String director) {
        this.director = director;
    }


}
