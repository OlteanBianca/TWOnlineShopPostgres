package SpringBoot.Controllers.AdminBC;

import SpringBoot.DTO.FilterPageDTO;
import SpringBoot.Models.Product;
import SpringBoot.Models.Shop;
import SpringBoot.Security.DBServices.ShopInventoryService;
import SpringBoot.Security.DBServices.ShopService;
import SpringBoot.Security.JWT.JwtUtils;
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
    JwtUtils jwtUtils;


    @GetMapping( "")
    @PreAuthorize("hasRole('ROLE_ADMIN_BC')")
    public String openPage(Model model, @RequestParam("Authorization") String token) {

        List<Shop> allShops = shopService.findAllShopsByShopFunctionId(1L);
        List<Product> allProducts=shopInventoryService.findAllProductsInALlShopsInventories(allShops);

        FilterPageDTO filterPageDTO = new FilterPageDTO("", token);
        model.addAttribute("DTO", filterPageDTO);
        model.addAttribute("shops", allShops);
        model.addAttribute("products", allProducts);

        return "adminBC/shopsPage";
    }

    @PostMapping(  "")
    @PreAuthorize("hasRole('ROLE_ADMIN_BC')")
    public String openPageWithNameFilter(Model model, @RequestParam("Authorization") String token,
             @ModelAttribute("DTO") FilterPageDTO dto) {

        if(dto.searchedText != null && !dto.searchedText.equals("")){
            List<Shop> allShops = shopService.findAllShopsByShopFunctionId(1L);
            List<Product> allProducts=shopInventoryService.findAllProductsContainingString(allShops, dto.searchedText);

            model.addAttribute("name", dto.searchedText);
            model.addAttribute("shops", allShops);
            model.addAttribute("products", allProducts);
            model.addAttribute("authorizationToken", token);
            return "adminBC/shopsPage";
        }

        return "redirect:/ROLE_ADMIN_BC/shops?Authorization=" + token;
    }


    @GetMapping("/error")
    public String error() {
        return "loginPage";
    }
}
