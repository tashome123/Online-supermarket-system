package com.strathmore.grocery.services;

import com.strathmore.grocery.repositories.CartItemRepository;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.autoconfigure.data.web.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;

@Service
public class CartitemService<Cartitem> {
    @Autowired
    private CartItemRepository CartitemRepository;

    public Cartitem createCartitem(Cartitem cartitem) {
        return cartitemRepository.save(cartitem);
    }

    public Cartitem getCartitemById(Long id) {
        return cartitemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cartitem not found"));
    }

    public Cartitem updateCartitem(Long id, Cartitem updatedCartitem) {
        Cartitem cartitem = getCartitemById(id);
        cartitem.setName(updatedCartitem.getName());
        cartitem.setDescription(updatedCartitem.getDescription());
        cartitem.setPrice(updatedCartitem.getPrice());
        cartitem.setCategory(updatedCartitem.getCategory());
        return cartitemRepository.save(cartitem);
    }

    public void deleteCartitem(Long id) {
        cartitemRepository.deleteById(id);
    }

    public Page<Cartitem> searchCartitems(String name, int page, int size) {
        SpringDataWebProperties.Pageable pageable = PageRequest.of(page, size);
        return cartitemRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    public Page<Cartitem> getAllCartitems(int page, int size) {
        return cartitemRepository.findAll(PageRequest.of(page, size));
    }
}
