package SpringBoot.Controllers.AdminBC;

import SpringBoot.Models.ShoppingCart;
import SpringBoot.Models.ShoppingCartProducts;
import SpringBoot.Models.User;
import SpringBoot.Security.DBServices.CommandService;
import SpringBoot.Security.DBServices.ShoppingCartService;
import SpringBoot.Security.DBServices.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/ROLE_ADMIN_BC/shoppingCart")
@RequiredArgsConstructor
public class AdminBCShoppingCartController {

    @Autowired
    private UserService userService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private CommandService commandService;


    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN_BC')")
    public String openPage(Model model, @RequestParam("Authorization") String token) {

        User user = userService.getUserFromToken(token);
        ShoppingCart shoppingCart = shoppingCartService.getShoppingCart(user);

        if (shoppingCart != null) {
            model.addAttribute("products", shoppingCartService.getAllProductsInShoppingCart(shoppingCart.getId()));
        }
        model.addAttribute("authorizationToken", token);
        return "adminBC/adminBCShoppingCartPage";
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ROLE_ADMIN_BC')")
    public String saveCommand(@RequestParam("Authorization") String token, Model model) {

        User user = userService.getUserFromToken(token);
        if (user != null) {

            ShoppingCart shoppingCart = shoppingCartService.getShoppingCart(user);
            if (shoppingCart != null) {

                List<ShoppingCartProducts> products = shoppingCartService.getAllProductsInShoppingCart(shoppingCart.getId());
                commandService.createCommandFromShoppingCart(user, products);

                products = shoppingCartService.getAllProductsInShoppingCart(shoppingCart.getId());
                if (products == null || products.isEmpty()) {
                    shoppingCartService.deleteShoppingCart(shoppingCart);
                } else {
                    model.addAttribute("error", "Products not available!");
                    model.addAttribute("products", products);
                    model.addAttribute("authorizationToken", token);
                    return "adminBC/adminBCShoppingCartPage";
                }
            }
        }
        return "redirect:/ROLE_ADMIN_BC/shops?Authorization=" + token;
    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN_BC')")
    public String deleteCommand(@RequestParam("Authorization") String token) {

        User user = userService.getUserFromToken(token);
        if (user != null) {

            ShoppingCart shoppingCart = shoppingCartService.getShoppingCart(user);
            if (shoppingCart != null) {
                shoppingCartService.deleteShoppingCart(shoppingCart);
            }
        }
        return "redirect:/ROLE_ADMIN_BC/shops?Authorization=" + token;
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
