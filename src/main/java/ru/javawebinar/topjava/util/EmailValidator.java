package ru.javawebinar.topjava.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.exception.NotFoundException;

@Component
public class EmailValidator implements Validator {

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserTo.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserTo userTo = (UserTo) target;
        String email = null;
        try {
            User existing = userService.getByEmail(userTo.getEmail());
            email = existing.getEmail();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        if (email != null) {
            throw new RuntimeException("User with this email already exists");
//            errors.rejectValue("user.email", "user.email.exists");
        }
    }
}