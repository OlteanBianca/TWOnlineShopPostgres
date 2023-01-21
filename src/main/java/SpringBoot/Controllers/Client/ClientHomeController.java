package SpringBoot.Controllers.Client;

import SpringBoot.Models.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/ROLE_CLIENT")
@RequiredArgsConstructor

public class ClientHomeController {


    @GetMapping( "")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public String openPageForClient(Model model, @RequestParam("Authorization") String token) {

        model.addAttribute("authorizationToken", token);

        return "client/clientHomePage";
    }

    @PostMapping( "")
    public String openPageForClient(@ModelAttribute("authorizationToken") String token){

        return "redirect:/ROLE_CLIENT?Authorization=" + token;
    }

    @GetMapping({"/error", "/search/error"})
    public String error() {
        return "loginPage";
    }


    @PostMapping ("/search")
    public String search(@ModelAttribute("shops") ArrayList<Shop> allShops, @ModelAttribute("authorizationToken") String token) {


        return "redirect:/ROLE_CLIENT?Authorization = " + token;
    }
}
