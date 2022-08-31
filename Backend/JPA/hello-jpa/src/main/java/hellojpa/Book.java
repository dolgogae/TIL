package hellojpa;

import java.util.jar.Attributes.Name;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("book")
public class Book extends Item{
    
    private String name;
    private String isbn;
}
