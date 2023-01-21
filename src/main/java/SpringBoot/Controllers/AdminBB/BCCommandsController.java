package SpringBoot.Controllers.AdminBB;

import SpringBoot.Models.Shop;
import SpringBoot.Models.User;
import SpringBoot.Security.DBServices.CommandService;
import SpringBoot.Security.DBServices.ShopService;
import SpringBoot.Security.DBServices.UserService;
import SpringBoot.Security.JWT.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    @Autowired
    JwtUtils jwtUtils;


    @GetMapping( "")
    @PreAuthorize("hasRole('ROLE_ADMIN_BB')")
    public String openPage(Model model, @RequestParam("Authorization") String token, Error error) {

        String username = jwtUtils.getUserNameFromJwtToken(token);
        User user = userService.getUserByUsername(username);
        Shop shop = shopService.getShopByUserId(user.getId());

        model.addAttribute("commands", commandService.getAllActiveCommandsForOneShop(shop.getId()));
        model.addAttribute("authorizationToken", token);

        if (error != null)
            model.addAttribute("error", "Products not available.");

        return "adminBB/BCCommandsPage";
    }

    @GetMapping( "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN_BB')")
    public String acceptCommand(@PathVariable("id") String id, @RequestParam("Authorization") String token, Model model){

        if(id != null){
           if(!commandService.acceptCommand(Long.valueOf(id))){
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
