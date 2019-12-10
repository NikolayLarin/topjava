package ru.javawebinar.topjava.web;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static ru.javawebinar.topjava.TestUtil.userAuth;
import static ru.javawebinar.topjava.UserTestData.ADMIN;
import static ru.javawebinar.topjava.UserTestData.USER;

class RootControllerTest extends AbstractControllerTest {

    @Test
    void getUsers() throws Exception {
        mockMvc.perform(get("/users")
                .with(userAuth(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/users.jsp"));
    }

    @Test
    void getUsersByUnAuth() throws Exception {
        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    void getMealsByUnAuth() throws Exception {
        mockMvc.perform(get("/meals"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    void getMeals() throws Exception {
        mockMvc.perform(get("/meals")
                .with(userAuth(USER)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("meals"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/meals.jsp"));
    }

    @Test
    void getUsersByUser() throws Exception {
        mockMvc.perform(get("/ajax/admin/users")
                .with(userAuth(USER)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }
}