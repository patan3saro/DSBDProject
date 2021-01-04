package dsbd.project.productmanager.data;

import org.springframework.data.repository.CrudRepository;
import product.Product;

import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Integer> {
    public Optional<Product> findById(Integer id);
    public Product findByIdAndQuantityGreaterThanEqual(Integer id, Integer quantity);
}
