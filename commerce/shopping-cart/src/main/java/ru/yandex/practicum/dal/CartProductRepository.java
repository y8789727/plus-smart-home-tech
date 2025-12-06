package ru.yandex.practicum.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.dal.model.CartProduct;

import java.util.List;

public interface CartProductRepository extends JpaRepository<CartProduct, Integer> {
    void deleteByCartId(String cartId);

    List<CartProduct> getProductByCartId(String cartId);

}
