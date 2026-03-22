package app.electronics.Items;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Items> getAllItems(){
        return itemRepository.findAll();
    }

    public Items addItem(Items items){
        return itemRepository.save(items);
    }

    public Items findById(Integer id){
        return itemRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Item-ul nu există"
        ));
    }

    public Items updateItem(int id, Items item) {
        Items existingItem = itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Item-ul nu există"
                ));

        if (item.getName() != null)
            existingItem.setName(item.getName());

        if (item.getDescription() != null)
            existingItem.setDescription(item.getDescription());

        if (item.getSubtype() != null)
            existingItem.setSubtype(item.getSubtype());

        if (item.getCategory() != null)
            existingItem.setCategory(item.getCategory());

        if (item.getImage() != null)
            existingItem.setImage(item.getImage());

        if (item.getBrand() != null)
            existingItem.setBrand(item.getBrand());

        return itemRepository.save(existingItem);
    }

    public void deleteItem(int id) {
        if (!itemRepository.existsById(id)) {
            throw  new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Item-ul nu există"
            );
        }
        itemRepository.deleteById(id);
    }


}
