package com.example.onlinepreorderingsystem.controller;

import com.example.onlinepreorderingsystem.dao.*;
import com.example.onlinepreorderingsystem.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/customer/")
public class CustomerController
{
    @Autowired
    ProductDao prodDao;

    @Autowired
    CategoryDao catDao;

    @Autowired
    UserDao userDao;

    @Autowired
    OrderDetailsDao orderDetailsDao;

    @Autowired
    OrderDao orderDao;

    @GetMapping("/menu/")
    public String menu(Model model)
    {
        List<Category> listCat = catDao.findAll();
        List<Product> listProd = prodDao.findAll();
        model.addAttribute("listCat", listCat);
        model.addAttribute("idCategorySelected", -1);
        model.addAttribute("listProd", listProd);
        model.addAttribute("email", ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());

        signInOutCode(model);

        return "menu";
    }

    @PostMapping("/menu/filter/")
    public String filterMenu(Model model, long idCategory, String search)
    {
        if(idCategory == -1)
        {
            List<Product> listProd = prodDao.findByNameContains(search);
            model.addAttribute("idCategorySelected", -1);
            model.addAttribute("listProd", listProd);
        }
        else
        {
            List<Product> listProd = prodDao.findByIdCategoryAndNameContains(idCategory, search);
            model.addAttribute("idCategorySelected", idCategory);
            model.addAttribute("listProd", listProd);
        }

        List<Category> listCat = catDao.findAll();
        model.addAttribute("listCat", listCat);
        model.addAttribute("email", ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());

        signInOutCode(model);

        return "menu";

    }


    public void signInOutCode(Model model)
    {
        String signBtnText = "Sign In";
        String signUrl = "/login";

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails)
        {
            signBtnText = "Sign Out";
            signUrl = "/logout";
        }

        model.addAttribute("btnText", signBtnText);
        model.addAttribute("btnUrl", signUrl);
    }

    @GetMapping("/details/")
    public String customerDetails(Model model)
    {
        List<User> listUser = userDao.findByIsAdmin(false);
        model.addAttribute("userDao", userDao);
        model.addAttribute("listUser", listUser);

        return "customerDetails";
    }

    @GetMapping("/orders/")
    public String customerOrder(Model model)
    {
        List<Orders> listOrders = orderDao.findAll();
        model.addAttribute("listOrders", listOrders);


        model.addAttribute("userDao", userDao);
        model.addAttribute("prodDao", prodDao);

        signInOutCode(model); //newly added

        return "ordersCustomer";
    }

    @GetMapping("/user/delete/{id}/")
    public String deleteCustomer(Model model, @PathVariable long id)
    {
        userDao.deleteById(id);

        List<User> listUser = userDao.findByIsAdmin(false);
        model.addAttribute("listUser", listUser);

        return"customerDetails";

    }



}
