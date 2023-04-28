package SpringBoot.Security.DBServices;

import SpringBoot.Models.ShopInventory;
import SpringBoot.Models.ShoppingCart;
import SpringBoot.Models.ShoppingCartProducts;
import SpringBoot.Models.User;
import SpringBoot.Repository.ShoppingCartProductsRepository;
import SpringBoot.Repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ShoppingCartService {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    @Autowired
    private ShoppingCartProductsRepository shoppingCartProductsRepository;



    public ShoppingCart addShoppingCart(User user) {
        return shoppingCartRepository.save(new ShoppingCart(user));
    }

    public ShoppingCart getShoppingCart(User user) {
        return shoppingCartRepository.findByUserId(user.getId());
    }

    public List<ShoppingCartProducts> getAllProductsInShoppingCart(Long shoppingCartId) {

        return shoppingCartProductsRepository.findAllByShoppingCartId(shoppingCartId);
    }

    public void addToShoppingCart(User user, ShopInventory shopInventory, int quantity) {
        ShoppingCart shoppingCart = getShoppingCart(user);
        if (shoppingCart == null) {
            shoppingCart = addShoppingCart(user);
        }

        ShoppingCartProducts shoppingCartProduct = new ShoppingCartProducts(quantity, shopInventory.getShop(),
                shopInventory.getProduct(), shoppingCart);

        shoppingCartProductsRepository.save(shoppingCartProduct);
    }

    @Transactional
    public void deleteShoppingCart(ShoppingCart shoppingCart) {

        shoppingCartProductsRepository.removeAllByShoppingCartId(shoppingCart.getId());
        shoppingCartRepository.delete(shoppingCart);
    }

    @Transactional
    public void deleteProductInShoppingCart(Long shoppingCartProductId) {
        shoppingCartProductsRepository.removeById(shoppingCartProductId);
    }
}
