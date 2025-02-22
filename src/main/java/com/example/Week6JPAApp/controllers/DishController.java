package com.example.Week6JPAApp.controllers;

import com.example.Week6JPAApp.models.Dish;
import com.example.Week6JPAApp.services.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/restaurant")
//localhost:8080/restaurant/home とかmenuにアクセスすることができる
public class DishController {
//  A field to hold an instance of DishService.
//  This field is FINAL and is initialized in the constructor, so it is never modified.
    private final DishService dishService;

    // Spring will automatically inject an instance of DishService via @Autowired annotation.
    @Autowired
    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @Value("${restaurant.name}")
    private String restaurantName;

// Read the value of the page.size property from the application.properties file and set it to the pageSize field.
// This dynamically changes the page size used for pagination from an external configuration file.
// The setting value is “5”.
    @Value("${page.size}")
    private int pageSize;

    //endpoint for home page
    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("rName", restaurantName);
        return "home";
    }

    //endpoint for main page
    //ページネーションを追加する
    @GetMapping("/menu/{pageNumber}")
    public String menu(Model model,
                       @RequestParam(required = false) String message,
                       @RequestParam(required = false) String searchedCategory,
                       @RequestParam(required = false) Double searchedPrice,
                       @PathVariable int pageNumber,
                       //ここで、デフォルトのsortをIDにしているので、menu pageでid昇順でsort がかかった状態で表示される
                       //ここで、defaultValue をprice とかにするとまた表示が変わる。
                       @RequestParam(defaultValue = "id") String sortField,
                       @RequestParam(defaultValue = "asc") String sortDirection) {

        //filter dishes by category and price
        if (searchedCategory != null && searchedPrice != null) {
            List<Dish> filteredDishes = dishService.getDishByCategoryAndPrice(searchedCategory, searchedPrice);
            model.addAttribute("dishes", filteredDishes);
            model.addAttribute("message", filteredDishes.isEmpty() ? "No dishes found" : "Dish filtered successfully!");
            return "menu";
        }

        //pagination - return dishes in pages
        Page<Dish> page = dishService.getPaginationToDishes(pageNumber, pageSize, sortField, sortDirection);

        model.addAttribute("dishes", page.getContent());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        //general condition - return all dishes
        //model.addAttribute("dishes", dishService.getAllDishes());
        model.addAttribute("message", message);
        
        return "menu";
    }



    //endpoint for add dish page
    @GetMapping("/add")
    public String addDish(Model model) {
        model.addAttribute("dish", new Dish());
        return "add-dish";
    }

    //endpoint the save the dish
    @PostMapping("/save")
    public String saveDishes(@ModelAttribute Dish dish, Model model){

        int statusCode = dishService.saveDish(dish);
        if(statusCode == 0){
            model.addAttribute("message", "Price should be less than 20");
            return "add-dish";
        }
        //http://localhost:8080/restaurant/save
        //Data is input anything in add-dish -> solve <problem -> ID:0 in save page> -> auto increment
        //Subtract “-1” to get the last element of the list, since the list starts at index 0
        // Get the last added dish from the list of saved dishes
        List<Dish> dishes = dishService.getAllDishes();
        Dish lastDishes = dishes.get(dishes.size() - 1);

        //save the data
        //open the menu page with updates data
        // Add data to the model to open the menu with updated data
        model.addAttribute("dishes", lastDishes);
        model.addAttribute("message", lastDishes.getName() + " added successfully");
        return "menu";
    }


    //endpoint to delete a dish
    @GetMapping ("/delete/{id}")
    public String deleteDish(@PathVariable int id, Model model) {
        // Delete the dish by ID and get status code
        int deleteStatusCode = dishService.deleteDishById(id);

        // Check if delete was successful (dish exists)
        if (deleteStatusCode == 1) {
            return "redirect:/restaurant/menu/1?message=Dish deleted successfully!";
        }
        //does delete fail (dish does not exist)
        return "redirect:/restaurant/menu/1?message=Dish does not exist!";
    }


    //endpoint to update a dish
    @GetMapping("/update/{id}")
    public String updateDish(@PathVariable int id, Model model) {
        //get the dish to be updated by its id from the database
        Optional<Dish> optionalDishToUpdate = dishService.getDishById(id);

        if (optionalDishToUpdate.isPresent()) {
            dishService.updateDish(optionalDishToUpdate.get());
            model.addAttribute("dish", optionalDishToUpdate.get());
            return "add-dish";
        }
        return "redirect:/restaurant/menu/1?message=Dish does not exist!";

    }

    @PostMapping("/update")
    public String updateDish(@ModelAttribute Dish dish, Model model) {
        //call the update method in the service class
        dishService.updateDish(dish);
        return "redirect:/restaurant/menu/1?message=Dish updated successfully!";
    }

}
