package SpringBoot.Controllers.Client;

import SpringBoot.Models.*;
import SpringBoot.Security.DBServices.CommandService;
import SpringBoot.Security.DBServices.ShoppingCartService;
import SpringBoot.Security.DBServices.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/ROLE_CLIENT/shoppingCart")
@RequiredArgsConstructor
public class ClientShoppingCartController {

    @Autowired
    private UserService userService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private CommandService commandService;



    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public String openPage(Model model, @RequestParam("Authorization") String token) {

        User user = userService.getUserFromToken(token);
        ShoppingCart shoppingCart = shoppingCartService.getShoppingCart(user);

        if (shoppingCart != null) {
            model.addAttribute("products", shoppingCartService.getAllProductsInShoppingCart(shoppingCart.getId()));
        }
        model.addAttribute("authorizationToken", token);
        return "client/clientShoppingCartPage";
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public String saveCommand(@RequestParam("Authorization") String token, Model model) {

        User user = userService.getUserFromToken(token);
        if (user != null) {

            ShoppingCart shoppingCart = shoppingCartService.getShoppingCart(user);
            if (shoppingCart != null) {

                List<ShoppingCartProducts> products = shoppingCartService.getAllProductsInShoppingCart(shoppingCart.getId());
                Command command = commandService.createCommandFromShoppingCart(user, products);
                commandService.finishCommand(command);

                products = shoppingCartService.getAllProductsInShoppingCart(shoppingCart.getId());
                if (products == null || products.isEmpty()) {
                    shoppingCartService.deleteShoppingCart(shoppingCart);
                } else {
                    model.addAttribute("error", "Products not available!");
                    model.addAttribute("products", products);
                    model.addAttribute("authorizationToken", token);
                    return "client/clientShoppingCartPage";
                }
            }
        }
        return "redirect:/ROLE_CLIENT?Authorization=" + token;
    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public String deleteCommand(@RequestParam("Authorization") String token) {

        User user = userService.getUserFromToken(token);
        if (user != null) {

            ShoppingCart shoppingCart = shoppingCartService.getShoppingCart(user);
            if (shoppingCart != null) {
                shoppingCartService.deleteShoppingCart(shoppingCart);
            }
        }
        return "redirect:/ROLE_CLIENT?Authorization=" + token;
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
