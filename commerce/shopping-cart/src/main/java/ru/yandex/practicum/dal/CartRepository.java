package ru.yandex.practicum.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.dal.model.Cart;
import ru.yandex.practicum.dal.model.CartState;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, String> {

    Optional<Cart> findCartByUserIdAndState(String userId, CartState state);
}
