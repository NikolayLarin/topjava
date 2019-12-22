package ru.javawebinar.topjava.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.to.UserTo;

@Component
public class FormEmailValidator implements Validator {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageSource messageSource;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserTo.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserTo userTo = (UserTo) target;
        User existing = userRepository.getByEmail(userTo.getEmail());
        if (existing != null) {
            final Integer userToId = userTo.getId();
            if (userTo.isNew()) {
                rejectEmailChanging();
            }
            if (userTo.getEmail().equals(existing.getEmail()) && !userToId.equals(existing.getId())) {
                rejectEmailChanging();
            }
        }
    }

    private void rejectEmailChanging() {
        throw new DataIntegrityViolationException(messageSource.getMessage(
                "exception.duplicate_email", null, LocaleContextHolder.getLocale()));
    }
}