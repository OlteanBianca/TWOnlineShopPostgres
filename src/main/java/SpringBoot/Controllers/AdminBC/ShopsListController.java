package SpringBoot.Controllers.AdminBC;

import SpringBoot.DTO.FilterPageDTO;
import SpringBoot.Models.Product;
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
@RequestMapping("/ROLE_ADMIN_BC/shops")
@RequiredArgsConstructor
public class ShopsListController {

    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopInventoryService shopInventoryService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private UserService userService;


    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN_BC')")
    public String openPage(Model model, @RequestParam("Authorization") String token) {

        List<Shop> allShops = shopService.findAllShopsByShopFunctionId(1L);
        List<ShopInventory> allProducts = shopInventoryService.findAllProductsInShopsInventories(allShops);

        FilterPageDTO filterPageDTO = new FilterPageDTO("", token);
        filterPageDTO.setSelectedShops(allShops);

        model.addAttribute("DTO", filterPageDTO);
        model.addAttribute("allShops", allShops);
        model.addAttribute("allProducts", allProducts);
        model.addAttribute("addedProduct", new Product());

        return "adminBC/shopsPage";
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN_BC')")
    public String filterProducts(Model model, @RequestParam("Authorization") String token,
                                 @ModelAttribute("DTO") FilterPageDTO filterPageDTO) {

        if (filterPageDTO.selectedShops != null) {

            if (filterPageDTO.searchedText == null || filterPageDTO.searchedText.equals("")) {
                model.addAttribute("allProducts", shopInventoryService.findAllProductsInShopsInventories(
                        filterPageDTO.selectedShops));
            } else {
                model.addAttribute("allProducts", shopInventoryService.findAllProductsContainingString(
                        filterPageDTO.selectedShops, filterPageDTO.searchedText));
            }

            filterPageDTO.setAuthorizationToken(token);
            model.addAttribute("DTO", filterPageDTO);
            model.addAttribute("addedProduct", new Product());
            model.addAttribute("allShops", shopService.findAllShopsByShopFunctionId(1L));

            return "adminBC/shopsPage";
        }
        return "redirect:/ROLE_ADMIN_BC/shops?Authorization=" + token;
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN_BC')")
    public String addToCart(@RequestParam("Authorization") String token, @ModelAttribute("addedProduct") Product product,
                            @PathVariable String id) {

        if (id != null) {
            Long shopInventoryId = Long.parseLong(id);
            ShopInventory shopInventory = shopInventoryService.getShopInventoryById(shopInventoryId);

            User user = userService.getUserFromToken(token);
            shoppingCartService.addToShoppingCart(user, shopInventory, product.getQuantity());
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
