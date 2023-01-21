package SpringBoot.Controllers;

import SpringBoot.Security.JWT.JwtUtils;
import SpringBoot.Security.Services.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("")
public class HomeController {

    private final SecurityService securityService;
    @Autowired
    JwtUtils jwtUtils;


    @GetMapping("")
    public String open() {
        if (!securityService.isAuthenticated()) {
            return "redirect:/login";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String jwt = jwtUtils.generateJwtToken(authentication);

        var authority = authentication.getAuthorities().stream().findFirst();
        return authority.map(grantedAuthority -> "redirect:/" + grantedAuthority + "?Authorization=Bearer " + jwt)
                .orElse("redirect:/login");
    }

    @GetMapping("/error")
    public String error(){
        return "loginPage";
    }
}
