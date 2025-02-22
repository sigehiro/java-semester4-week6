package com.example.Week6JPAApp.services;

import com.example.Week6JPAApp.models.Dish;
import com.example.Week6JPAApp.repositories.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

@Service
public class DishService {

    //constructor injection
    //final にしてDishServiceを定義するメリットは２つ。コンストラクタインジェクションishServiceクラスが
    // DishRepositoryに依存していることが明示されます。
    // これにより、クラスの依存関係が明確になり、コードの可読性が向上します
    //不変性の確保: DishRepositoryフィールドがfinalで宣言されているため
    // 一度初期化された後に変更されることがありません。これにより、オブジェクトの不変性が確保され、
    // 予期しない変更によるバグを防ぐことができます
    private final DishRepository dishRepository;

    public DishService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

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
    public Page<Dish> getPaginationToDishes(int pageNo,
                                            int pageSize,
                                            String sortField,
                                            String sortDirection) {

        // Determine the sort direction and create a Sort object
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        // Create a Pageable object with the specified page number, page size, and sort order
        Pageable pageable = PageRequest.of(pageNo -1, pageSize, sort);

        // Retrieve and return the paginated list of dishes from the repository
        return dishRepository.findAll(pageable);
    }

}
