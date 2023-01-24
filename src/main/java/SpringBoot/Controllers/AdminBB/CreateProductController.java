package SpringBoot.Controllers.AdminBB;

import SpringBoot.Models.Product;
import SpringBoot.Security.DBServices.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/ROLE_ADMIN_BB/createProduct")
@RequiredArgsConstructor
public class CreateProductController {
    @Autowired
    private ProductService productService;


    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN_BB')")
    public String open(Model model, String error, @RequestParam("Authorization") String token) {

        model.addAttribute("product", new Product());
        model.addAttribute("authorizationToken", token);

        if (error != null)
            model.addAttribute("error", "Product invalid.");

        return "adminBB/createProductPage";
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN_BB')")
    public String createProduct(@ModelAttribute("product") Product product, @RequestParam("Authorization") String token,
                                BindingResult bindingResult) {

        if (product.getName().isEmpty()) {

            bindingResult.rejectValue("name", "product.isNameEmpty");
            return "redirect:/ROLE_ADMIN_BB/createProduct?Authorization=" + token;
        }
        productService.addProduct(product);
        return "redirect:/ROLE_ADMIN_BB?Authorization=" + token;
    }

    @GetMapping("/error")
    public String error() {
        return "loginPage";
    }
}
