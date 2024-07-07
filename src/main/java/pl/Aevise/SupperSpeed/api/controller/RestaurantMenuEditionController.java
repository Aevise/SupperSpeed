package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.Aevise.SupperSpeed.api.dto.DishCategoryDTO;
import pl.Aevise.SupperSpeed.api.dto.DishDTO;
import pl.Aevise.SupperSpeed.api.dto.RestaurantDTO;
import pl.Aevise.SupperSpeed.business.*;
import pl.Aevise.SupperSpeed.domain.Dish;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static pl.Aevise.SupperSpeed.business.utils.ImageHandlerInterface.MAX_IMAGE_HEIGHT;
import static pl.Aevise.SupperSpeed.business.utils.ImageHandlerInterface.MAX_IMAGE_WIDTH;

@Controller
@AllArgsConstructor
public class RestaurantMenuEditionController {

    static final String RESTAURANT_MENU_EDIT = "/restaurant/profile/menu";
    static final String RESTAURANT_MENU_UPDATE_DISH = "/restaurant/profile/menu/updateDish";
    static final String RESTAURANT_MENU_DELETE_DISH = "/restaurant/profile/menu/deleteDish";
    static final String RESTAURANT_MENU_UPDATE_CATEGORY = "/restaurant/profile/menu/updateCategory";
    static final String RESTAURANT_MENU_DELETE_CATEGORY = "/restaurant/profile/menu/deleteCategory";
    static final String RESTAURANT_MENU_ADD_CATEGORY = "/restaurant/profile/menu/addCategory";
    static final String RESTAURANT_MENU_ADD_DISH = "/restaurant/profile/menu/addDish";
//    static final String ERROR = "error";

    private final DishListService dishListService;
//    private final ProfileService profileService;
    private final DishCategoryService dishCategoryService;
    private final DishService dishService;
    private final RestaurantService restaurantService;
    private final ImageHandlingService imageHandlingService;


    @GetMapping(RESTAURANT_MENU_EDIT)
    public String getRestaurantMenuEdit(
            Model model,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        RestaurantDTO restaurantByEmail = restaurantService.findRestaurantByEmail(userDetails.getUsername());
        Integer restaurantId = restaurantByEmail.getRestaurantId();
        String restaurantName = restaurantByEmail.getRestaurantName();

        //moze trzeba najpierw pobrac wszystkie zdjecia aby sql nie pytal o to osobno dla kazdego zdjecia??

        List<DishCategoryDTO> dishCategories = dishListService.getDishCategoriesByRestaurantId(restaurantId);
        var dishList = dishService.extractDishesByCategory(dishCategories, false);

        String restaurantDirectory = imageHandlingService.getRestaurantName(restaurantId, restaurantName);

        model.addAttribute("dishesByCategory", dishList);
        model.addAttribute("restaurantId", restaurantId);
        model.addAttribute("categories", dishCategories);

        model.addAttribute("restaurantDirectory", restaurantDirectory);
        model.addAttribute("imageWidth", MAX_IMAGE_WIDTH);
        model.addAttribute("imageHeight", MAX_IMAGE_HEIGHT);
        model.addAttribute("restaurantName", restaurantName);
        return "restaurant_menu_edit";
    }

    @PostMapping(RESTAURANT_MENU_UPDATE_DISH)
    public String updateDish(
            @ModelAttribute DishDTO dishDTO
    ) {
        if (dishDTO.getAvailability() == null) {
            dishDTO.setAvailability(false);
        }
        dishService.updateDish(dishDTO);
        return "redirect:" + RESTAURANT_MENU_EDIT;
    }

    @PostMapping(RESTAURANT_MENU_DELETE_DISH)
    public String deleteDish(
            @RequestParam(value = "dishId") Integer dishId
    ) {
        dishService.deleteOrHideDishByDishId(dishId);
        return "redirect:" + RESTAURANT_MENU_EDIT;
    }

    @PostMapping(RESTAURANT_MENU_DELETE_CATEGORY)
    public String deleteCategory(
            @RequestParam(value = "dishCategoryId") Integer categoryId
    ) {
        List<Dish> allDishesByCategory = dishService.findAllByCategory(categoryId);
        if (!allDishesByCategory.isEmpty()) {
            var dishMapByPresenceInOrder = mapOrdersByPresenceInAnyOrder(allDishesByCategory);
            dishService.deleteOrHideDishesMap(dishMapByPresenceInOrder);
            dishCategoryService.deleteCategory(categoryId);
        } else {
            dishCategoryService.deleteCategory(categoryId);
        }


        return "redirect:" + RESTAURANT_MENU_EDIT;
    }

    private Map<Boolean, List<Dish>> mapOrdersByPresenceInAnyOrder(List<Dish> allDishesByCategory) {
        Map<Boolean, List<Dish>> isDishInOrder = new LinkedHashMap<>();
        isDishInOrder.put(true, new ArrayList<>());
        isDishInOrder.put(false, new ArrayList<>());

        for (Dish dish : allDishesByCategory) {
            boolean dishInOrder = dishListService.isDishInOrder(dish.getDishId());
            isDishInOrder.get(dishInOrder).add(dish);
        }
        return isDishInOrder;
    }

    @PostMapping(RESTAURANT_MENU_UPDATE_CATEGORY)
    public String updateCategory(
            @ModelAttribute DishCategoryDTO dishCategoryDTO
    ) {
        dishCategoryService.updateCategory(dishCategoryDTO);

        return "redirect:" + RESTAURANT_MENU_EDIT;
    }

    @PostMapping(RESTAURANT_MENU_ADD_CATEGORY)
    public String addCategory(
            @RequestParam(value = "categoryName") String categoryName,
            @RequestParam(value = "restaurantId") String restaurantId
    ) {

        dishCategoryService.addCategory(dishCategoryService.buildDishCategory(restaurantId, categoryName));
        return "redirect:" + RESTAURANT_MENU_EDIT;
    }

    @PostMapping(RESTAURANT_MENU_ADD_DISH)
    public String addDish(
            @ModelAttribute DishDTO dishDTO,
            @RequestParam(value = "restaurantId") Integer restaurantId,
            @RequestParam(value = "categoryId") Integer categoryId
    ) {
        Dish dish = dishService.buildDish(dishDTO, restaurantId, categoryId);
        dishService.addDish(dish);
        return "redirect:" + RESTAURANT_MENU_EDIT;
    }


}
