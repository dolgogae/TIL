package jpabook.jpashop.domain.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@DiscriminatorValue("B")
public class Book extends Item{

    private String author;
    private String isbn;


    public void createBook(Long id, String name, int price, int stockQuantity, String author, String isbn){
        this.id = id;
        this.name = name;
        this.price = price;
        this.author = author;
        this.stockQuantity = stockQuantity;
        this.isbn = isbn;
    }

//            book.setName(form.getName());
//        book.setPrice(form.getPrice());
//        book.setStockQuantity(form.getStockQuantity());
//        book.setAuthor(form.getAuthor());
//        book.setIsbn(form.getIsbn());
}
