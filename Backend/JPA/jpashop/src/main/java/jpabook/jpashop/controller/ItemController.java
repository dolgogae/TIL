package jpabook.jpashop.controller;

import jpabook.jpashop.controller.form.BookForm;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm form) {
        Book book = new Book();
        // db에 한번 갔다온 객체 -> 준영속성 객체 -> JPA가 변경관리를 하지 않는다.
        book.createBook(
                form.getId(),
                form.getName(),
                form.getPrice(),
                form.getStockQuantity(),
                form.getAuthor(),
                form.getIsbn()
        );

        itemService.saveItem(book);

        return "redirect:/items";
    }

    @GetMapping("/items")
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
        Book item = (Book) itemService.findOne(itemId);

        BookForm form = new BookForm(
                item.getId(),
                item.getName(),
                item.getPrice(),
                item.getStockQuantity(),
                item.getAuthor(),
                item.getIsbn()
        );

        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    @PostMapping("/items/{itemId}/edit")
    public String updateItemForm(@PathVariable Long itemId, @ModelAttribute("form") BookForm form) {

//        Book book = new Book();
//        book.createBook(
//                form.getId(),
//                form.getName(),
//                form.getPrice(),
//                form.getStockQuantity(),
//                form.getAuthor(),
//                form.getIsbn()
//        );
//
//        itemService.saveItem(book);
        itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity());
        return "redirect:/items";
    }
}
