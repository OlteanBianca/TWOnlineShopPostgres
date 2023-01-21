package SpringBoot.Controllers.AdminBC;

import SpringBoot.Models.Product;
import SpringBoot.Models.Shop;
import SpringBoot.Models.User;
import SpringBoot.Security.DBServices.ShopInventoryService;
import SpringBoot.Security.DBServices.ShopService;
import SpringBoot.Security.DBServices.UserService;
import SpringBoot.Security.JWT.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ROLE_ADMIN_BC")
@RequiredArgsConstructor
public class AdminBCHomeController {

    @Autowired
    private ShopService shopService;
    @Autowired
    private UserService userService;
    @Autowired
    private ShopInventoryService shopInventoryService;
    @Autowired
    JwtUtils jwtUtils;


    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN_BC')")
    public String openPage(Model model, @RequestParam("Authorization") String token) {

        String username = jwtUtils.getUserNameFromJwtToken(token);
        User user = userService.getUserByUsername(username);
        Shop shop =shopService.getShopByUserId(user.getId());
        List<Map.Entry<Product, Integer>> shopProducts = shopInventoryService.findAllProductsQuantityInShopInventory(shop.getId());

        model.addAttribute("shopProducts", shopProducts);
        model.addAttribute("authorizationToken", token);

        return "adminBC/adminBCHomePage";
    }

    @GetMapping("/error")
    public String error() {
        return "loginPage";
    }
}
