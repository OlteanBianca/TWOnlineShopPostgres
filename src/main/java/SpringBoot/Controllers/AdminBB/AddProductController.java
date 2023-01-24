package SpringBoot.Controllers.AdminBB;

import SpringBoot.Models.Product;
import SpringBoot.Models.Shop;
import SpringBoot.Models.ShopInventory;
import SpringBoot.Models.User;
import SpringBoot.Security.DBServices.ProductService;
import SpringBoot.Security.DBServices.ShopInventoryService;
import SpringBoot.Security.DBServices.ShopService;
import SpringBoot.Security.DBServices.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/ROLE_ADMIN_BB/addProduct")
@RequiredArgsConstructor
public class AddProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopInventoryService shopInventoryService;


    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN_BB')")
    public String open(Model model, @RequestParam("Authorization") String token) {

        User user = userService.getUserFromToken(token);
        Shop shop = shopService.getShopByUserId(user.getId());
        List<ShopInventory> inventory = shopInventoryService.findAllProductsInShopInventory(shop.getId());

        model.addAttribute("inventory", new ShopInventory());
        model.addAttribute("allProducts", productService.getAllProductsNotInInventory(inventory));
        model.addAttribute("authorizationToken", token);

        return "adminBB/addProductPage";
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN_BB')")
    public String addProduct(@ModelAttribute("inventory") ShopInventory shopInventory,
                             @RequestParam("Authorization") String token) {

        if (shopInventory != null && shopInventory.getProduct() != null) {

            Product product = productService.getProductById(shopInventory.getProduct().getId());
            User user = userService.getUserFromToken(token);
            Shop shop = shopService.getShopByUserId(user.getId());

            if (shop != null && product != null) {
                shopInventoryService.addProductToInventory(shop, product, shopInventory.getQuantity());
                return "redirect:/ROLE_ADMIN_BB?Authorization=" + token;
            }
        }
        return "redirect:/ROLE_ADMIN_BB/addProduct?Authorization=" + token;
    }

    @GetMapping("/error")
    public String error() {
        return "loginPage";
    }

    @PostMapping("/error")
    public String errorPage() {
        return "loginPage";
    }
}
