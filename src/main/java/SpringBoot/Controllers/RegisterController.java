package SpringBoot.Controllers;

import SpringBoot.Models.Request;
import SpringBoot.Security.DBServices.RequestService;
import SpringBoot.Security.DBServices.UserService;
import SpringBoot.Security.JWT.JwtUtils;
import SpringBoot.Security.Services.UserValidatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {

    private final UserValidatorService userValidatorService;
    @Autowired
    private RequestService requestService;
    @Autowired
    private UserService userService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;


    @GetMapping()
    public String open(Model model) {

        model.addAttribute("userForm", new Request());
        return "registerPage";
    }

    @PostMapping()
    public String register(@ModelAttribute("userForm") Request userForm, BindingResult bindingResult) {

        userValidatorService.validate(userForm, bindingResult);
        if (bindingResult.hasErrors())
            return "registerPage";

        if (userForm.getRoleName().equals("admin_bb") || userForm.getRoleName().equals("admin_bc")) {
            userForm.setRole(userService.getUserRole(userForm.getRoleName()));
            requestService.addNewRequest(userForm);
            return "messagePage";
        } else {
            userService.addNewUser(userForm);
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userForm.getUsername(), userForm.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        var authority = authentication.getAuthorities().stream().findFirst();
        return authority.map(grantedAuthority -> "redirect:/" + grantedAuthority + "?Authorization=Bearer " + jwt)
                .orElse("register");
    }

    @GetMapping("/error")
    public String error() {
        return "registerPage";
    }
}
