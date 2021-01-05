package dsbd.project.productmanager.service;

import dsbd.project.productmanager.data.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import product.Product;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public Optional<Product> findById(int id){
        return productRepository.findById(id);
    }
}
