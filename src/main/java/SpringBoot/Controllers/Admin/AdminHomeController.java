package SpringBoot.Controllers.Admin;

import SpringBoot.Security.DBServices.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ROLE_ADMIN")
@RequiredArgsConstructor
public class AdminHomeController {

    @Autowired
    private RequestService requestService;


    @GetMapping( "")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String openPage(Model model, @RequestParam("Authorization") String token) {

        model.addAttribute("requests", requestService.getActiveRequests());
        model.addAttribute("authorizationToken", token);

        return "admin/adminHomePage";
    }

    @GetMapping( "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String acceptRequest(@PathVariable("id") String id, @RequestParam("Authorization") String token){

        if(id != null){
            requestService.acceptRequest(Long.valueOf(id));
        }
        return "redirect:/ROLE_ADMIN?Authorization=" + token;
    }

    @GetMapping("/error")
    public String error() {
        return "loginPage";
    }
}
