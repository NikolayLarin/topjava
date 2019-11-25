package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.SecurityUtil;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.MEAL1;
import static ru.javawebinar.topjava.MealTestData.MEAL1_ID;
import static ru.javawebinar.topjava.MealTestData.MEAL5;
import static ru.javawebinar.topjava.MealTestData.MEAL6;
import static ru.javawebinar.topjava.MealTestData.MEAL7;
import static ru.javawebinar.topjava.MealTestData.MEALS;
import static ru.javawebinar.topjava.MealTestData.assertMatch;
import static ru.javawebinar.topjava.MealTestData.contentJson;
import static ru.javawebinar.topjava.TestUtil.readFromJson;

class MealRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = MealRestController.REST_URL + '/';

    @Autowired
    private MealService mealService;

    @Test
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + MEAL1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MEAL1));
    }

    @Test
    void getAll() throws Exception {
        List<MealTo> tos = MealsUtil.getTos(MEALS, SecurityUtil.authUserCaloriesPerDay());
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(tos));
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + MEAL1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> mealService.get(MEAL1_ID, SecurityUtil.authUserId()));
    }

    @Test
    void update() throws Exception {
        Meal updated = MealTestData.getUpdated();
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        assertMatch(mealService.get(MEAL1_ID, SecurityUtil.authUserId()), updated);
    }

    @Test
    void createWithLocation() throws Exception {
        Meal newMeal = MealTestData.getNew();
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMeal)))
                .andExpect(status().isCreated());

        Meal created = readFromJson(action, Meal.class);
        Integer newId = created.getId();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(mealService.get(newId, SecurityUtil.authUserId()), newMeal);
    }

    @Test
    void getBetween() throws Exception {
        List<MealTo> tos = MealsUtil.getTos(List.of(MEAL7, MEAL6, MEAL5), SecurityUtil.authUserCaloriesPerDay());
        String startDate = "2015-05-31";
        String startTime = "07:00";
        String endDate = "2015-05-31";
        String endTime = "23:00";
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "/filter?" +
                "startDate=" + startDate + "&startTime=" + startTime +
                "&endDate=" + endDate + "&endTime=" + endTime))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(tos));
/*
        String startDateTime = "2015-05-31T07:00";
        String endDateTime = "2015-05-31T23:00";
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "/filter?" +
                "startDateTime=" + startDateTime + "&endDateTime=" + endDateTime))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(tos));
*/
    }

    @Test
    void getBetweenWithEmptyFields() throws Exception {
        List<MealTo> tos = MealsUtil.getTos(List.copyOf(MEALS), SecurityUtil.authUserCaloriesPerDay());
        String startDate = "";
        String startTime = "";
        String endDate = "";
        String endTime = "";
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "/filter?" +
                "startDate=" + startDate + "&startTime=" + startTime +
                "&endDate=" + endDate + "&endTime=" + endTime))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(tos));
    }
}