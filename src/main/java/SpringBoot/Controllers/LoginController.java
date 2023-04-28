package SpringBoot.Controllers;

import SpringBoot.Models.User;
import SpringBoot.Security.JWT.JwtUtils;
import SpringBoot.Security.Services.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final SecurityService securityService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;


    private String redirect(Authentication authentication) {
        String jwt = jwtUtils.generateJwtToken(authentication);

        var authority = authentication.getAuthorities().stream().findFirst();
        return authority.map(grantedAuthority -> "redirect:/" + grantedAuthority + "?Authorization=Bearer " + jwt)
                .orElse("login");
    }

    @GetMapping()
    public String open(Model model, String error, String logout) {

        model.addAttribute("userForm", new User());
        if (securityService.isAuthenticated()) {
            return redirect(SecurityContextHolder.getContext().getAuthentication());
        }
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "loginPage";
    }

    @PostMapping("")
    public String authenticateUser(@ModelAttribute("userForm") User userForm, Model model) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userForm.getUsername(), userForm.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            return redirect(authentication);

        } catch (AuthenticationException e) {
            model.addAttribute("error", "Your username and password is invalid.");
        }
        return "loginPage";
    }

    @GetMapping("/error")
    public String error() {
        return "loginPage";
    }
}
