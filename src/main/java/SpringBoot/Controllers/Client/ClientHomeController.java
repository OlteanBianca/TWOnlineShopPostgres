package SpringBoot.Controllers.Client;

import SpringBoot.DTO.FilterPageDTO;
import SpringBoot.Models.*;
import SpringBoot.Security.DBServices.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/ROLE_CLIENT")
@RequiredArgsConstructor
public class ClientHomeController {

    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopInventoryService shopInventoryService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private UserService userService;


    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public String openPage(Model model, @RequestParam("Authorization") String token) {

        List<Shop> allShops = shopService.findAllShopsByShopFunctionId(2L);
        List<ShopInventory> allProducts = shopInventoryService.findAllProductsInShopsInventories(allShops);

        FilterPageDTO filterPageDTO = new FilterPageDTO("", token);
        filterPageDTO.setSelectedShops(allShops);

        model.addAttribute("DTO", filterPageDTO);
        model.addAttribute("allShops", allShops);
        model.addAttribute("allProducts", allProducts);
        model.addAttribute("addedProduct", new Product());

        return "client/clientHomePage";
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public String filterProducts(Model model, @RequestParam("Authorization") String token,
                                 @ModelAttribute("DTO") FilterPageDTO filterPageDTO) {

        if (filterPageDTO.selectedShops != null) {

            if (filterPageDTO.searchedText != null && !filterPageDTO.searchedText.equals("")) {
                model.addAttribute("allProducts", shopInventoryService.findAllProductsContainingString(
                        filterPageDTO.selectedShops, filterPageDTO.searchedText));
            } else {
                model.addAttribute("allProducts", shopInventoryService.findAllProductsInShopsInventories(
                        filterPageDTO.selectedShops));
            }

            filterPageDTO.setAuthorizationToken(token);
            model.addAttribute("DTO", filterPageDTO);
            model.addAttribute("addedProduct", new Product());
            model.addAttribute("allShops", shopService.findAllShopsByShopFunctionId(2L));

            return "client/clientHomePage";
        }
        return "redirect:/ROLE_CLIENT?Authorization=" + token;
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public String addToCart(@RequestParam("Authorization") String token, @ModelAttribute("addedProduct") Product product,
                            @PathVariable String id) {

        if (id != null) {
            ShopInventory shopInventory = shopInventoryService.getShopInventoryById(Long.parseLong(id));
            User user = userService.getUserFromToken(token);
            shoppingCartService.addToShoppingCart(user, shopInventory, product.getQuantity());
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
