package ru.javawebinar.topjava.service;

import org.junit.Assume;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataAccessException;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.Set;

import static ru.javawebinar.topjava.UserTestData.ADMIN;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.UserTestData.assertMatch;
import static ru.javawebinar.topjava.UserTestData.getNew;
import static ru.javawebinar.topjava.UserTestData.getNewWithEmptyRoles;
import static ru.javawebinar.topjava.UserTestData.getUpdated;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public abstract class AbstractUserServiceTest extends AbstractServiceTest {

    @Autowired
    protected UserService service;

    @Autowired
    protected CacheManager cacheManager;

    @Test
    public void create() throws Exception {
        User newUser = getNew();
        newUser.addRole(Role.ROLE_USER, Role.ROLE_ADMIN);
        User created = service.create(newUser);
        Integer newId = created.getId();
        newUser.setId(newId);
        assertMatch(created, newUser);
        assertMatch(service.get(newId), newUser);
    }

    @Test
    public void createWithNoRoles() throws Exception {
        User newUserWithNoRoles = getNewWithEmptyRoles();
        User createdWithNoRoles = service.create(newUserWithNoRoles);
        Integer newUserWithNoRolesId = createdWithNoRoles.getId();
        newUserWithNoRoles.setId(newUserWithNoRolesId);
        assertMatch(createdWithNoRoles, newUserWithNoRoles);
        assertMatch(service.get(newUserWithNoRolesId), newUserWithNoRoles);
    }

    @Test(expected = DataAccessException.class)
    public void duplicateEmailCreate() throws Exception {
        service.create(new User(null, "Duplicate", "user@yandex.ru", "newPass", Role.ROLE_USER, Role.ROLE_ADMIN));
    }

    @Test(expected = NotFoundException.class)
    public void delete() throws Exception {
        service.delete(ADMIN_ID);
        service.get(ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deletedNotFound() throws Exception {
        service.delete(1);
    }

    @Test
    public void get() throws Exception {
        User admin = service.get(ADMIN_ID);
        assertMatch(admin, ADMIN);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() throws Exception {
        service.get(1);
    }

    @Test
    public void getByEmail() throws Exception {
        User user = service.getByEmail("user@yandex.ru");
        assertMatch(user, USER);
        User admin = service.getByEmail("admin@gmail.com");
        assertMatch(admin, ADMIN);
    }

    @Test(expected = NotFoundException.class)
    public void geNotFoundByEmail() throws Exception {
        service.getByEmail("notExistEmail@gmail.com");
    }

    @Test
    public void update() throws Exception {
        User updated = getUpdated();
        service.update(updated);
        assertMatch(service.get(USER_ID), updated);
        assertMatch(service.getAll(), ADMIN, updated);
    }

    @Test
    public void updateWithTwoRoles() throws Exception {
        User updated = getUpdated().addRole(Role.ROLE_ADMIN);
        service.update(updated);
        assertMatch(service.get(USER_ID), updated);
        assertMatch(service.getAll(), ADMIN, updated);
    }

    @Test
    public void updateWithNoRoles() throws Exception {
        User updated = getUpdated().clearRoles();
        service.update(updated);
        assertMatch(service.get(USER_ID), updated);
        assertMatch(service.getAll(), ADMIN, updated);
    }

    @Test
    public void zzzGetAll() throws Exception {
        assertMatch(service.getAll(), ADMIN, USER);
    }

    @Test
    public void createWithException() throws Exception {
        Assume.assumeTrue(!super.isJdbcTest());
        validateRootCause(() -> service.create(new User(null, "  ", "mail@yandex.ru", "password", Role.ROLE_USER)), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new User(null, "User", "  ", "password", Role.ROLE_USER)), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new User(null, "User", "mail@yandex.ru", "  ", Role.ROLE_USER)), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new User(null, "User", "mail@yandex.ru", "password", 9, true, new Date(), Set.of())), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new User(null, "User", "mail@yandex.ru", "password", 10001, true, new Date(), Set.of())), ConstraintViolationException.class);
    }
}