package SpringBoot.Security.Services;

import SpringBoot.Models.Request;
import SpringBoot.Models.User;
import SpringBoot.Security.DBServices.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Service
@RequiredArgsConstructor
public class UserValidatorService implements Validator {

    @Autowired
    private UserService userService;


    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object userEntity, Errors errors) {
        Request request = (Request) userEntity;

        if (request.getRoleName().equals("admin_bb") || request.getRoleName().equals("admin_bc")) {
            validateShop(errors);
        }

        validateEmail(request.getEmail(), errors);
        validateUsername(request.getUsername(), errors);
        validatePassword(request.getPassword(), request.getPasswordConfirm(), errors);
    }

    public void validatePassword(String password, String confirmPassword, Errors errors) {
        /*
           at least 8 digits {8,}
           at least one number (?=.*\d)
           at least one lowercase (?=.*[a-z])
           at least one uppercase (?=.*[A-Z])
           at least one special character (?=.*[@#$%^&+=])
           No space [^\s]
        */
        String passwordRegexPattern = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*[^\s]{8,}$";

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "user.isPasswordEmpty");

        if (!password.matches(passwordRegexPattern))
            errors.rejectValue("password", "user.isValidPassword");

        if (!password.equals(confirmPassword))
            errors.rejectValue("passwordConfirm", "user.isPasswordTheSame");
    }

    public void validateEmail(String email, Errors errors) {

        /* Typical email format: email@domain.com */
        String emailRegexPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        if (!email.matches(emailRegexPattern))
            errors.rejectValue("email", "user.isValidEmail");

        if (userService.emailAlreadyExists(email)) {
            errors.rejectValue("email", "user.isEmailAlreadyUsed");
        }
    }

    public void validateUsername(String username, Errors errors) {

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "user.isUsernameEmpty");

        if (username.length() < 2 || username.length() > 32)
            errors.rejectValue("username", "user.isValidUserLength");

        if (userService.usernameAlreadyExists(username)) {
            errors.rejectValue("username", "user.isUsernameAlreadyUsed");
        }
    }

    public void validateShop(Errors errors) {

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "address.isAddressEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "code", "code.isCodeEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shopName", "shopName.isShopNameEmpty");
    }
}
