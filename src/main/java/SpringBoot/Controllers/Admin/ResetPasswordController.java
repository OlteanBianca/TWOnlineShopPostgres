package SpringBoot.Controllers.Admin;

import SpringBoot.Models.User;
import SpringBoot.Security.DBServices.UserService;
import SpringBoot.Security.JWT.JwtUtils;
import SpringBoot.Security.Services.UserValidatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ROLE_ADMIN/resetPassword")
@RequiredArgsConstructor
public class ResetPasswordController {

    private final UserValidatorService userValidatorService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtils jwtUtils;


    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String open(Model model, @RequestParam("Authorization") String token) {

        String username = jwtUtils.getUserNameFromJwtToken(token);
        User user = userService.getUserByUsername(username);

        model.addAttribute("authorizationToken", token);
        model.addAttribute("userForm", user);

        return "admin/resetPasswordPage";
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String resetPassword(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, Model model,
           @RequestParam("Authorization") String token) {

        userValidatorService.validatePassword(userForm.getPassword(), userForm.getPasswordConfirm(), bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("authorizationToken", token);
            model.addAttribute("userForm", userForm);
            return "admin/resetPasswordPage";
        }

        userService.updatePassword(userForm.getId(), userForm.getPassword());
        return "redirect:/ROLE_ADMIN?Authorization=" + token;
    }

    @GetMapping("/error")
    public String error() {
        return "loginPage";
    }
}
