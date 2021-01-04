package dsbd.project.productmanager.service;

import dsbd.project.productmanager.data.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import product.Product;

import javax.transaction.Transactional;

@Service
@Transactional
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public Product findByIdAndQuantityGreaterThanEqual(Integer id, Integer quantity){
        return productRepository.findByIdAndQuantityGreaterThanEqual(id, quantity);
    }
}
