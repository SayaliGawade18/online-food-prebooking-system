package com.example.onlinepreorderingsystem.dao;

import com.example.onlinepreorderingsystem.entity.Category;
import com.example.onlinepreorderingsystem.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryDao extends JpaRepository<Category,Long>
{
    @Query("select name from Category where id = :id")
    String getNameById(Long id);

    List<Category> findByNameContains(String name);
}
