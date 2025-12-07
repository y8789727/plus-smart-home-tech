package ru.yandex.practicum.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.dal.model.CartProduct;

import java.util.List;
import java.util.UUID;

public interface CartProductRepository extends JpaRepository<CartProduct, Integer> {
    void deleteByCartId(UUID cartId);

    List<CartProduct> getProductByCartId(UUID cartId);

}
