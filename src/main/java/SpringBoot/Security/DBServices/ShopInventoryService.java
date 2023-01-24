package SpringBoot.Security.DBServices;

import SpringBoot.Models.CommandProducts;
import SpringBoot.Models.Product;
import SpringBoot.Models.Shop;
import SpringBoot.Models.ShopInventory;
import SpringBoot.Repository.ShopInventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShopInventoryService {
    @Autowired
    ShopInventoryRepository shopInventoryRepository;


    public ShopInventory getShopInventoryById(Long id) {
        var shop = shopInventoryRepository.findById(id);
        return shop.orElse(null);
    }

    public List<ShopInventory> findAllProductsInShopsInventories(List<Shop> shops) {
        List<ShopInventory> allProducts = new ArrayList<>();

        for (Shop shop : shops) {
            allProducts.addAll(shopInventoryRepository.findAllByShopId(shop.getId()));
        }
        return allProducts;
    }

    public List<ShopInventory> findAllProductsInShopInventory(Long shopId) {
        return shopInventoryRepository.findAllByShopId(shopId);
    }

    public List<ShopInventory> findAllProductsContainingString(List<Shop> shops, String text) {
        List<ShopInventory> allProducts = new ArrayList<>();

        for (Shop shop : shops) {
            List<ShopInventory> products = shopInventoryRepository.findAllByShopIdAndProductNameContaining(
                    shop.getId(), text);

            allProducts.addAll(products);
        }
        return allProducts;
    }

    public void addProductToInventory(Shop shop, Product product, int quantity) {
        ShopInventory shopInventory = new ShopInventory();
        shopInventory.setProduct(product);
        shopInventory.setShop(shop);
        shopInventory.setQuantity(quantity);
        shopInventoryRepository.save(shopInventory);
    }

    public boolean isProductQuantityInInventory(Long productId, Long shopId, int quantity) {
        ShopInventory inventory = shopInventoryRepository.findByProductIdAndShopId(productId, shopId);

        return inventory.getQuantity() >= quantity;
    }

    public void updateShopInventory(CommandProducts product) {

        ShopInventory inventory = shopInventoryRepository.findByProductIdAndShopId(product.getProduct().getId(),
                product.getShop().getId());
        inventory.setQuantity(inventory.getQuantity() - product.getQuantity());
        shopInventoryRepository.save(inventory);
    }

    public void updateQuantity(Long shopInventoryId, int quantity) {

        Optional<ShopInventory> shopInventory = shopInventoryRepository.findById(shopInventoryId);
        if (shopInventory.isPresent()) {

            ShopInventory inventory = shopInventory.get();
            inventory.setQuantity(quantity);
            shopInventoryRepository.save(inventory);
        }
    }
}
