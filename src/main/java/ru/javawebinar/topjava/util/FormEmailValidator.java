package ru.javawebinar.topjava.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.exception.ExistException;

@Component
public class FormEmailValidator implements Validator {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserTo.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserTo userTo = (UserTo) target;
        User existing = userRepository.getByEmail(userTo.getEmail());
        if (existing != null && userTo.getId() == null) {
            throw new ExistException("User with this email already exists");
        }
    }
}