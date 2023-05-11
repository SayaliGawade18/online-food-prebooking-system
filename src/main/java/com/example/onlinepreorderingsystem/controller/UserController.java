package com.example.onlinepreorderingsystem.controller;

import com.example.onlinepreorderingsystem.dao.UserDao;
import com.example.onlinepreorderingsystem.entity.Category;
import com.example.onlinepreorderingsystem.entity.Hotel;
import com.example.onlinepreorderingsystem.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@Controller
public class UserController
{
    @Autowired
    UserDao userDao;

    @GetMapping("/public/reg/")
    public String registrationPage()
    {
        return "regUser";
    }

    @PostMapping("/public/user/save/")
    public String saveUser(Model model, String name, String email, String password, String contact)
    {
        User user = new User(name, contact, email, password);
        userDao.save(user);
        return "redirect:/login";
    }

    @GetMapping("/admin/dashboard/")
    public String dashboard()
    {
        return "adminDashboard";
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



}
