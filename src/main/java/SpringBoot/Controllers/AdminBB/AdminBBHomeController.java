package SpringBoot.Controllers.AdminBB;

import SpringBoot.Models.Shop;
import SpringBoot.Models.ShopInventory;
import SpringBoot.Models.User;
import SpringBoot.Security.DBServices.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN_BB')")
    public String openPage(Model model, @RequestParam("Authorization") String token) {

        User user = userService.getUserFromToken(token);
        Shop shop = shopService.getShopByUserId(user.getId());
        List<ShopInventory> shopProducts = shopInventoryService.findAllProductsInShopInventory(shop.getId());

        model.addAttribute("shopProducts", shopProducts);
        model.addAttribute("authorizationToken", token);
        model.addAttribute("updatedInventory", new ShopInventory());

        return "adminBB/adminBBHomePage";
    }

    @PostMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN_BB')")
    public String save(@PathVariable String id, @ModelAttribute("updatedInventory") ShopInventory shopInventory,
                       @RequestParam("Authorization") String token) {

        if (id != null) {
            shopInventoryService.updateQuantity(Long.valueOf(id), shopInventory.getQuantity());
        }
        return "redirect:/ROLE_ADMIN_BB?Authorization=" + token;
    }

    @GetMapping("/error")
    public String error() {
        return "loginPage";
    }
}
