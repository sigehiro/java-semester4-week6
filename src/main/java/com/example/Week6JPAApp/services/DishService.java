package com.example.Week6JPAApp.services;

import com.example.Week6JPAApp.models.Dish;
import com.example.Week6JPAApp.repositories.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

@Service
public class DishService {

    //injecting the repository
    @Autowired
    private DishRepository dishRepository;

    // Method to retrieve all dishes from the database
    public List<Dish> getAllDishes() {
        //business logic should have been here
        return dishRepository.findAll();
    }

    // Method to save a dish in the database
    public int saveDish(Dish dish) {
        //business logic should have been here
        //if price is greater than 20, do not save
        if(dish.getPrice() > 20){
            return 0;
        }
        //save the dish. saveDish() returns the number of rows affected
        dishRepository.save(dish);
        return 1;
//        return dishRepository.saveDish(dish);
    }


    //delete a dish from the db
    //0=> dish not deleted
    //1=> dish deleted
    public int deleteDishById(int id) {
        //business logic should have been here
        if(dishRepository.existsById(id)){
            dishRepository.deleteById(id);
            return 1;
        }
        return 0;
    }

    // Method to update a dish in the database
    public void updateDish(Dish dish) {
        //business logic should have been here
        dishRepository.save(dish);
    }

    // Method to retrieve a dish by its ID
    public Optional<Dish> getDishById(int id) {
        //business logic should have been here
        return dishRepository.findById(id);
    }

    // Method to retrieve dishes by category and price
    public List<Dish> getDishByCategoryAndPrice(String category, double price) {
        //business logic should have been here

        return dishRepository.findByIgnoreCaseCategoryAndPrice(category, price);
    }

    //pagination method
    public Page<Dish> getPaginationToDishes(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo -1, pageSize);
        return dishRepository.findAll(pageable);

    }

}
