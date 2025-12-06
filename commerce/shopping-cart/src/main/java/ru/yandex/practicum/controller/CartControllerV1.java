package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.exception.NotAuthorizedUserException;
import ru.yandex.practicum.service.CartService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/shopping-cart")
@RequiredArgsConstructor
public class CartControllerV1 {

    private final CartService cartService;

    @GetMapping
    public ShoppingCartDto getShoppingCartByUser(@RequestParam(value = "username", required = false) String userName) {
        validateUser(userName);

        return cartService.getShoppingCartByUser(userName);
    }

    @PutMapping
    public ShoppingCartDto addProductsToCart(@RequestParam(value = "username", required = false) String userName,
                                             @RequestBody Map<String, Integer> products){
        validateUser(userName);

        return cartService.addProductsToCart(userName, products);
    }

    @DeleteMapping
    public void deactivateUserCart(@RequestParam(value = "username", required = false) String userName) {
        validateUser(userName);

        cartService.deactivateUserCart(userName);
    }

    @PostMapping("/remove")
    public ShoppingCartDto removeProductsFromCart(@RequestParam(value = "username", required = false) String userName,
                                                  @RequestBody List<String> products) {
        validateUser(userName);

        return cartService.removeProductsFromCart(userName, products);
    }

    @PostMapping("/change-quantity")
    public ShoppingCartDto changeProductQuantity(@RequestParam(value = "username", required = false) String userName,
                                                 @RequestBody ChangeProductQuantityRequest productQuantity) {
        validateUser(userName);

        return cartService.changeProductQuantity(userName, productQuantity);
    }

    private void validateUser(String userName) {
        if (userName == null || userName.isBlank()) {
            throw new NotAuthorizedUserException("Пользователь не определен!");
        }
    }
}
