package ru.yandex.practicum.dal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.dto.product.ProductCategory;
import ru.yandex.practicum.dto.product.ProductState;
import ru.yandex.practicum.dal.model.Product;

public interface ProductRepository extends JpaRepository<Product, String> {
    Page<Product> findByCategoryAndProductState(ProductCategory category, ProductState productState, Pageable pageable);
}
