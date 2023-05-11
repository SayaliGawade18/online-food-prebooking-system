package com.example.onlinepreorderingsystem.controller;

import com.example.onlinepreorderingsystem.dao.HotelDao;
import com.example.onlinepreorderingsystem.entity.Hotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class HotelController
{
    @Autowired
    HotelDao hotelDao;

    @GetMapping("/hotel/")
    public String hotel(Model model)
    {
        List<Hotel> list = hotelDao.findAll();
        model.addAttribute("list",list);
        return "hotelDetails";
    }

    @PostMapping("/hotel/add/")
    public String addHotel(Model model,Long id, String name, String address, String city, String district, String email, String contact, String bankAccount, String ifsc,String upi)
    {
        Hotel hotel = null;
        
        if(id != null)
        {
            hotel = hotelDao.getReferenceById(id);
            hotel.setName(name);
            hotel.setAddress(address);
            hotel.setCity(city);
            hotel.setDistrict(district);
            hotel.setEmail(email);
            hotel.setContact(contact);
            hotel.setBankAccount(bankAccount);
            hotel.setIfsc(ifsc);
            hotel.setUpi(upi);
        }
        else
        {
            hotel = new Hotel(name,address,city, district, email, contact, bankAccount, ifsc,upi);
        }
        
        hotelDao.save(hotel);
        List<Hotel> list = hotelDao.findAll();
        model.addAttribute("list", list);
        model.addAttribute("msg", "Hotel added successfully");

        return "hotelDetails";

    }

    @GetMapping("/hotel/delete/{id}/")
    public String deleteHotel(Model model, @PathVariable long id)
    {
        hotelDao.deleteById(id);

        List<Hotel> list = hotelDao.findAll();
        model.addAttribute("list",list);

        return"hotelDetails";

    }

    @GetMapping("/hotel/update/{id}/")
    public String updateHotel(Model model, Hotel hotel)
    {
        hotelDao.save(hotel);
        model.addAttribute("category", hotel);

        return "hotelDetails";
    }
}