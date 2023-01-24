package SpringBoot.Controllers.AdminBC;

import SpringBoot.Models.User;
import SpringBoot.Security.DBServices.CommandService;
import SpringBoot.Security.DBServices.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ROLE_ADMIN_BC/commands")
@RequiredArgsConstructor
public class AdminBCCommandsController {

    @Autowired
    private CommandService commandService;
    @Autowired
    private UserService userService;


    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN_BC')")
    public String openPage(Model model, @RequestParam("Authorization") String token) {

        User user = userService.getUserFromToken(token);

        model.addAttribute("commands", commandService.getAllCommandProductsForOneClient(user.getId()));
        model.addAttribute("authorizationToken", token);
        return "adminBC/adminBCCommandsPage";
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN_BC')")
    public String deleteCommand(@PathVariable("id") String id, @RequestParam("Authorization") String token) {

        if (id != null) {
            commandService.deleteCommandProduct(Long.valueOf(id));
        }
        return "redirect:/ROLE_ADMIN_BC/commands?Authorization=" + token;
    }

    @GetMapping("/error")
    public String error() {
        return "loginPage";
    }
}
