package SpringBoot.Security.DBServices;

import SpringBoot.Models.Product;
import SpringBoot.Models.ShopInventory;
import SpringBoot.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;


    public void addProduct(Product product) {
        productRepository.save(product);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> getAllProductsNotInInventory(List<ShopInventory> inventory) {

        List<Long> productsInInventory = new ArrayList<>();
        for (ShopInventory product : inventory) {
            productsInInventory.add(product.getProduct().getId());
        }
        return productRepository.findAllByIdNotIn(productsInInventory);
    }
}
