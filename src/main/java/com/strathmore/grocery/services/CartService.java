package com.strathmore.grocery.services;

@Service
public class CartitemService {
    @Autowired
    private CartitemRepository cartitemRepository;

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
        Pageable pageable = PageRequest.of(page, size);
        return cartitemRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    public Page<Cartitem> getAllCartitems(int page, int size) {
        return cartitemRepository.findAll(PageRequest.of(page, size));
    }
}
