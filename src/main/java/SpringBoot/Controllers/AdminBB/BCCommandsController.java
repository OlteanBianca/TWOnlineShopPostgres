package SpringBoot.Controllers.AdminBB;

import SpringBoot.Models.Shop;
import SpringBoot.Models.User;
import SpringBoot.Security.DBServices.CommandService;
import SpringBoot.Security.DBServices.ShopService;
import SpringBoot.Security.DBServices.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ROLE_ADMIN_BB/commands")
@RequiredArgsConstructor
public class BCCommandsController {

    @Autowired
    private CommandService commandService;
    @Autowired
    private UserService userService;
    @Autowired
    private ShopService shopService;


    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN_BB')")
    public String openPage(Model model, @RequestParam("Authorization") String token, String error) {

        User user = userService.getUserFromToken(token);
        Shop shop = shopService.getShopByUserId(user.getId());

        model.addAttribute("commands", commandService.getAllCommandsForOneShop(shop.getId()));
        model.addAttribute("authorizationToken", token);

        if (error != null) {
            model.addAttribute("error", "Products not available.");
        }
        return "adminBB/BCCommandsPage";
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN_BB')")
    public String acceptCommand(@PathVariable("id") String id, @RequestParam("Authorization") String token, Model model) {

        if (id != null) {
            if (!commandService.acceptCommandProduct(Long.valueOf(id))) {
                model.addAttribute("error", "Products not available.");
            }
        }
        return "redirect:/ROLE_ADMIN_BB/commands?Authorization=" + token;
    }

    @GetMapping("/error")
    public String error() {
        return "loginPage";
    }
}
