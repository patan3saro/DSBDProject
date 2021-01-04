package dsbd.project.productmanager.controller;

import dsbd.project.productmanager.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import product.Product;

import java.util.Optional;

@Controller
@RequestMapping(path = "/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping(path="/id/{id}")
    public Optional<Product> findById(@PathVariable Integer id){
        return productService.findById(id);
    }
}
