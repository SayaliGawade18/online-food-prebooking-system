package com.example.onlinepreorderingsystem.controller;

import com.example.onlinepreorderingsystem.dao.FeedbackDao;
import com.example.onlinepreorderingsystem.dao.UserDao;
import com.example.onlinepreorderingsystem.entity.Category;
import com.example.onlinepreorderingsystem.entity.Feedback;
import com.example.onlinepreorderingsystem.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class FeedbackController
{
    @Autowired
    FeedbackDao feedbackDao;

    @Autowired
    UserDao userDao;

    @GetMapping("/public/feedback/")
    public String feedback(Model model)
    {
        List<Feedback> list = feedbackDao.findAll();
        model.addAttribute("list",list);

        signInOutCode(model);
        return "feedback";
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



    @PostMapping("/feedback/add/")
    public String addFeedback(Model model, Long id, String name, String message)
    {
        Feedback feedback = null;

        if(id != null)
        {
            feedback = feedbackDao.getReferenceById(id);
            feedback.setName(name);
            feedback.setMessage(message);

        }
        else
        {
            feedback = new Feedback(name, message);
        }

        feedbackDao.save(feedback);
        List<Feedback> list = feedbackDao.findAll();
        model.addAttribute("list",list);
        model.addAttribute("msg","Feedback added successfully");
        signInOutCode(model);

        return "feedback";

    }

    @GetMapping("/feedback/details/")
    public String showFeedback(Model model,Feedback feedback)
    {

        List<Feedback> list = feedbackDao.findAll();
        model.addAttribute("list",list);


        return "feedbackDetails";
    }

    @GetMapping("/feedback/delete/{id}/")
    public String deleteCategory(Model model, @PathVariable long id)
    {
        feedbackDao.deleteById(id);

        List<Feedback> list = feedbackDao.findAll();
        model.addAttribute("list",list);

        return"feedbackDetails";

    }



//    @GetMapping("/feedback/details/")
//    public String feedbackDetails(Model model)
//    {
//        List<User> listUser = userDao.findByIsAdmin(false);
//        model.addAttribute("userDao", userDao);
//        model.addAttribute("listUser", listUser);
//
//        return "feedbackDetails";
//    }









}
