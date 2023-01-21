package SpringBoot.Controllers.AdminBB;

import SpringBoot.Models.Product;
import SpringBoot.Models.Shop;
import SpringBoot.Models.User;
import SpringBoot.Security.DBServices.*;
import SpringBoot.Security.JWT.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ROLE_ADMIN_BB")
@RequiredArgsConstructor
public class AdminBBHomeController {

    @Autowired
    private ShopInventoryService shopInventoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private ShopService shopService;
    @Autowired
    JwtUtils jwtUtils;



    @GetMapping( "")
    @PreAuthorize("hasRole('ROLE_ADMIN_BB')")
    public String openPage(Model model, @RequestParam("Authorization") String token) {

        String username = jwtUtils.getUserNameFromJwtToken(token);
        User user = userService.getUserByUsername(username);
        Shop shop =shopService.getShopByUserId(user.getId());
        List<Map.Entry<Product, Integer>> shopProducts = shopInventoryService.findAllProductsQuantityInShopInventory(shop.getId());

        model.addAttribute("shopProducts", shopProducts);
        model.addAttribute("authorizationToken", token);

        return "adminBB/adminBBHomePage";
    }

    @PostMapping("")
    public String save(@ModelAttribute("shopProducts") ArrayList<Map.Entry<Product, Integer>> products) {
        return "adminBB/adminBBHomePage";
    }

    @GetMapping("/error")
    public String error() {
        return "loginPage";
    }
}
