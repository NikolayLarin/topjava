package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping(value = "/meals")
public class JspMealController extends AbstractMealController {

    public JspMealController(MealService service) {
        super(service);
    }

    @GetMapping("")
    public String getAll(Model model) {
        model.addAttribute("meals", super.getAll());
        return "meals";
    }

    @GetMapping("/filter")
    public String getBetween(Model model,
                             @RequestParam("startDate") String startDate,
                             @RequestParam("endDate") String endDate,
                             @RequestParam("startTime") String startTime,
                             @RequestParam("endTime") String endTime) {
        model.addAttribute("meals", super.getBetween(
                parseLocalDate(startDate),
                parseLocalTime(startTime),
                parseLocalDate(endDate),
                parseLocalTime(endTime)));
        return "meals";
    }

    @GetMapping("/create")
    public String getNew(Model model) {
        final Meal meal = new Meal(
                LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        addAttribute(model, meal, "create");
        return "mealForm";
    }

    @GetMapping("/update/{id}")
    public String getPresent(Model model, @PathVariable String id) {
        final Meal meal = super.get(getParsed(id));
        addAttribute(model, meal, "update");
        return "mealForm";
    }

    @PostMapping("/")
    public String create(@RequestParam("dateTime") String dateTime,
                         @RequestParam("description") String description,
                         @RequestParam("calories") String calories,
                         @RequestParam("id") String id) {
        final Meal meal = new Meal(
                LocalDateTime.parse(dateTime), description, getParsed(calories));
        if (StringUtils.isEmpty(id)) {
            super.create(meal);
        } else {
            super.update(meal, getParsed(id));
        }
        return "redirect:/meals";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        super.delete(getParsed(id));
        return "redirect:/meals";
    }

    private static Integer getParsed(String param) {
        String s = Objects.requireNonNull(param);
        return Integer.parseInt(s);
    }

    private static void addAttribute(Model model, Meal meal, String action) {
        model
                .addAttribute("meal", meal)
                .addAttribute("action", action);
    }
}