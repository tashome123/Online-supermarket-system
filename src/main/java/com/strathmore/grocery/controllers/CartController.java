package com.strathmore.grocery.controllers;

import org.springframework.web.bind.annotation.PostMapping;

public class CartController {
  @RestController
@RequestMapping("/api/cartItem")
public class CartitemController {

    @Autowired
    private CartitemService cartitemService;

    @PostMapping
    public ResponseEntity<Cartitem> createCartitem(@RequestBody Cartitem cartitem) {
        return new ResponseEntity<>(cartitemService.createCartitem(cartitem), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cartitem> getCartitem(@PathVariable Long id) {
        return ResponseEntity.ok(cartitemService.getCartitemById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cartitem> updateCartitem(@PathVariable Long id, @RequestBody Cartitem cartitem) {
        return ResponseEntity.ok(cartitemService.updateCartitem(id, cartitem));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCartitiem(@PathVariable Long id) {
        cartitemService.deleteCartitem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<Cartitem>> getAllCartitems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(cartItemService.getAllCartitems(page, size));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Cartitems>> searchProducts(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(productService.searchCartitems(name, page, size));
    }
}
}
