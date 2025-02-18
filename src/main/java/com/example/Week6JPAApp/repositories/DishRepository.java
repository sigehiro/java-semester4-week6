package com.example.Week6JPAApp.repositories;


import com.example.Week6JPAApp.models.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DishRepository extends JpaRepository<Dish, Integer> {

    //custom method to find records by category and price
    List<Dish> findByIgnoreCaseCategoryAndPrice(String category, double price);

    //native query (custom method)
    //欠点: データベースに依存するため、異なるデータベースに
    // 移行する際に互換性が問題になる可能性があります。
    //Native QueryとJPQL Queryの両方を使用できますが、
    // Native Queryの場合はそれぞれのデータベースに適したSQL文を書く必要があります。
//    @Query(value="select * from Dish where lower(category) = lower(?1) and price = ?2", nativeQuery = true)
//    public List<Dish> findByCategoryAndPrice(String searchedCategory, Double searchedPrice);

    //JPQL Query (custom method)
    //JPQL Queryはデータベースに依存しないため、
    // 異なるデータベースに移行しても問題ありません。
//    @Query("SELECT d FROM Dish d WHERE LOWER(d.category) = LOWER(:searchedCategory) AND d.price = :searchedPrice")
//    List<Dish> findByCategoryAndPrice(@Param("searchedCategory") String searchedCategory, @Param("searchedPrice") Double searchedPrice);



}