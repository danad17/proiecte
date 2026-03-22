package app.electronics.Items;


import app.electronics.User.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemsController {


    private final ItemService itemService;

    @Autowired
    ItemsController(ItemService itemService) {
        this.itemService = itemService;
    }


    @GetMapping("/getAll")
    public List<Items> getAllItems(){
        return itemService.getAllItems();
    }

    @GetMapping("/getById/{id}")
    public Items getItemById(@PathVariable int id){
        return itemService.findById(id);
    }


    @PostMapping("/add")
    public ResponseEntity<Items> addItem(@Valid @RequestBody Items items){
        Items itemSaved = itemService.addItem(items);
        return  ResponseEntity.ok(itemSaved);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Items> updateItem(@PathVariable int id ,@RequestBody Items items){
        Items itemSaved = itemService.updateItem(id,items);
        return  ResponseEntity.ok(itemSaved);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteItem(@PathVariable int id){
        itemService.deleteItem(id);
    }
}
