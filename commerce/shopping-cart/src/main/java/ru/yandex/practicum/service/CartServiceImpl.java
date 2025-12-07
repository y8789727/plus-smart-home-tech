package ru.yandex.practicum.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dal.CartProductRepository;
import ru.yandex.practicum.dal.CartRepository;
import ru.yandex.practicum.dal.model.Cart;
import ru.yandex.practicum.dal.model.CartProduct;
import ru.yandex.practicum.dal.model.CartState;
import ru.yandex.practicum.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.mapper.CartMapper;
import ru.yandex.practicum.dto.warehouse.WarehouseClient;
import ru.yandex.practicum.exception.NoCartFound;
import ru.yandex.practicum.exception.NoProductInWarehouse;
import ru.yandex.practicum.exception.NoProductsInShoppingCartException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;
    private final WarehouseClient warehouseClient;

    @Override
    public ShoppingCartDto getShoppingCartByUser(String userName) {
        return CartMapper.mapToDto(getUserCart(userName).orElseThrow(() -> new NoCartFound("Для пользователя " + userName + " не найдена активная корзина")));
    }

    @Override
    public ShoppingCartDto addProductsToCart(String userName, Map<UUID, Integer> products) {
        final Cart cart = getUserCart(userName).orElse(Cart.builder()
                        .id(UUID.randomUUID())
                        .userId(userName)
                        .state(CartState.ACTIVE)
                        .products(new HashMap<>())
                        .build());
        products.forEach((key, value) -> {
            if (cart.getProducts().containsKey(key)) {
                cart.getProducts().get(key).setQuantity(value);
            } else {
                cart.getProducts().put(key, CartProduct.builder()
                                .cartId(cart.getId())
                                .productId(key)
                                .quantity(value)
                            .build());
            }
        });

        checkProductInWarehouse(CartMapper.mapToDto(cart));

        saveCartAndProducts(cart);

        return getShoppingCartByUser(userName);
    }

    @Override
    public void deactivateUserCart(String userName) {
        Cart cart = getUserCart(userName).orElseThrow(() -> new NoCartFound("Для пользователя " + userName + " не найдена активная корзина"));
        cart.setState(CartState.INACTIVE);
        cartRepository.save(cart);
    }

    @Override
    public ShoppingCartDto removeProductsFromCart(String userName, List<UUID> products) {
        final Cart cart = getUserCart(userName).orElseThrow(() -> new NoCartFound("Для пользователя " + userName + " не найдена активная корзина"));
        products.forEach(p -> {
            if (cart.getProducts().containsKey(p)) {
                cart.getProducts().remove(p);
            } else {
                throw new NoProductsInShoppingCartException("Продукт id=" + p + " не найден в корзине пользователя " + userName);
            }
        });

        saveCartAndProducts(cart);

        return getShoppingCartByUser(userName);
    }

    @Override
    public ShoppingCartDto changeProductQuantity(String userName, ChangeProductQuantityRequest productQuantity) {
        final Cart cart = getUserCart(userName).orElseThrow(() -> new NoCartFound("Для пользователя " + userName + " не найдена активная корзина"));
        if (cart.getProducts().containsKey(productQuantity.getProductId())) {
            cart.getProducts().get(productQuantity.getProductId()).setQuantity(productQuantity.getNewQuantity());
        } else {
            throw new NoProductsInShoppingCartException("Продукт id=" + productQuantity.getProductId() + " не найден в корзине пользователя " + userName);
        }

        checkProductInWarehouse(CartMapper.mapToDto(cart));

        saveCartAndProducts(cart);

        return getShoppingCartByUser(userName);
    }

    private Optional<Cart> getUserCart(String userName) {
        Optional<Cart> cart = cartRepository.findCartByUserIdAndState(userName, CartState.ACTIVE);
        if (cart.isPresent()) {
            cart.get().setProducts(cartProductRepository.getProductByCartId(cart.get().getId()).stream()
                    .collect(Collectors.toMap(CartProduct::getProductId, Function.identity())));
        } else {
            return Optional.empty();
        }
        return cart;
    }

    private void checkProductInWarehouse(ShoppingCartDto cart) {
        try {
            warehouseClient.checkProductsAvailable(cart);
        } catch (FeignException e) {
            if (e.status() == 400) {
                throw new NoProductInWarehouse("Недостаточно товаров на складе");
            } else {
                throw e;
            }
        }
    }

    private void saveCartAndProducts(Cart cart) {
        cartRepository.save(cart);
        cartProductRepository.saveAll(cart.getProducts().values());
    }
}
