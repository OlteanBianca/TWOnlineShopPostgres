package SpringBoot.Security.DBServices;

import SpringBoot.Models.CommandProducts;
import SpringBoot.Models.Product;
import SpringBoot.Models.Shop;
import SpringBoot.Models.ShopInventory;
import SpringBoot.Repository.ShopInventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ShopInventoryService {
    @Autowired
    ShopInventoryRepository shopInventoryRepository;


    public List<Product> findAllProductsInALlShopsInventories(List<Shop> shops){
        List<Product> allProducts = new ArrayList<>();

        for (Shop shop:shops) {
            var products = shopInventoryRepository.findAllByShopId(shop.getId());

            for (ShopInventory inventory: products) {
                allProducts.add(inventory.getProduct());
            }
        }
        return allProducts;
    }

    public List<Product> findAllProductsContainingString(List<Shop> shops, String text){
        List<Product> allProducts = new ArrayList<>();

        for (Shop shop:shops) {
            List<ShopInventory> products = shopInventoryRepository.findAllByShopIdAndProductNameContaining(shop.getId(), text);

            for (ShopInventory inventory: products) {
                allProducts.add(inventory.getProduct());
            }
        }
        return allProducts;
    }

    public List<Map.Entry<Product, Integer>> findAllProductsQuantityInShopInventory(Long id){
        List<Map.Entry<Product, Integer>> allProducts = new ArrayList<>();
        var products = shopInventoryRepository.findAllByShopId(id);

        for (ShopInventory inventory: products) {

            allProducts.add(new AbstractMap.SimpleEntry<>(inventory.getProduct(), inventory.getQuantity()));
        }
        return allProducts;
    }

    public void addProductToInventory(Shop shop, Product product, int quantity){
        ShopInventory shopInventory = new ShopInventory();
        shopInventory.setProduct(product);
        shopInventory.setShop(shop);
        shopInventory.setQuantity(quantity);
        shopInventoryRepository.save(shopInventory);
    }

    public boolean isProductQuantityInInventory(Long productId, Long shopId, int quantity){
         ShopInventory inventory = shopInventoryRepository.findByProductIdAndShopId(productId, shopId);

         return inventory.getQuantity() >= quantity;
    }

    public void updateShopInventory(Long shopId, List<CommandProducts> products){

        for (CommandProducts command: products) {
            ShopInventory inventory = shopInventoryRepository.findByProductIdAndShopId(command.getProduct().getId(), shopId);
            inventory.setQuantity(inventory.getQuantity() - command.getQuantity());
            shopInventoryRepository.save(inventory);
        }
    }
}
