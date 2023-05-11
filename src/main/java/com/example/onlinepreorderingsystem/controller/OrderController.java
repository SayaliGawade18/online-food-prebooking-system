package com.example.onlinepreorderingsystem.controller;

import com.example.onlinepreorderingsystem.Helper.Helper;
import com.example.onlinepreorderingsystem.dao.*;
import com.example.onlinepreorderingsystem.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/orders/")
public class OrderController
{
    @Autowired
    UserDao userDao;
    @Autowired
    OrderDao orderDao;
    @Autowired
    OrderDetailsDao orderDetailsDao;
    @Autowired
    ProductDao prodDao;
    @Autowired
    CategoryDao catDao;

    @PostMapping("/addToCart/")
    public String addToCart(Model model, String email, long idProduct, int qty)
    {
        Long idCustomer = userDao.getByEmail(email).getId();

        List<OrderDetails> list = orderDetailsDao.findByIdOrderAndIdProductAndIdCustomer(-1L, idProduct, idCustomer);
        OrderDetails orderDetails = null;

        if(list.isEmpty())
        {
            orderDetails = new OrderDetails(idCustomer, idProduct, qty);
        }
        else
        {
            orderDetails = list.get(0);
            orderDetails.setQty(orderDetails.getQty() + qty);
        }

        orderDetailsDao.save(orderDetails);

        return "redirect:/customer/menu/";

    }

    @PostMapping("/add/")
    public String addOrders(Model model, Long idCustomer, LocalDateTime visitingDateTime, double paid)
    {
        System.out.println("Visiting Date Time : " + visitingDateTime);
        Orders order = new Orders(idCustomer, visitingDateTime, paid);
        Orders orderSaved = orderDao.save(order);
        orderDetailsDao.setNewOrderId(orderSaved.getId(), idCustomer);

        return "redirect:/orders/invoice/"+order.getId()+"/";

    }
    @GetMapping("/invoice/{id}/")
    public String getId(Model model, @PathVariable Long id)
    {
        List<OrderDetails> listOrderDetails = orderDetailsDao.findAllByIdOrder(id);
        System.out.println(listOrderDetails.toString());

        Orders orders = orderDao.getReferenceById(id);
        User user = userDao.getReferenceById(orders.getIdCustomer());
        Double subTotal = orderDetailsDao.getTotalByIdOrderAndIdCustomer(orders.getId(), orders.getIdCustomer());

        System.out.println("subTotal = " + subTotal);

        Double tax = 18.00;
        Long grandTotal = Math.round(subTotal + 100*tax/subTotal);
        Double paid = orders.getPaid();
        Double balance = grandTotal - paid;

        model.addAttribute("date", Helper.ddMMyyyy.format(orders.getDate()));
        model.addAttribute("listCart", listOrderDetails);
        model.addAttribute("orders", orders);
        model.addAttribute("customer", user);
        model.addAttribute("subTotal", Helper.rsFormat(subTotal));
        model.addAttribute("tax",tax);
        model.addAttribute("grandTotal", Helper.rsFormat(grandTotal));
        model.addAttribute("paid",Helper.rsFormat(paid));
        model.addAttribute("balance",Helper.rsFormat(balance));
        model.addAttribute("prodDao",prodDao);

        return "invoice";
    }

    @GetMapping("/cart/{idOrder}/")
    public String showCart(Model model,@PathVariable long idOrder)
    {
        String resultPage = "customerCart";
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User customer = userDao.getByEmail(email);

        List<OrderDetails> listCart = orderDetailsDao.findByIdOrderAndIdCustomer(idOrder, customer.getId());

        if(listCart.isEmpty())
        {
            resultPage = "menu";

            List<Category> listCat = catDao.findAll();
            List<Product> listProd = prodDao.findAll();
            model.addAttribute("msg", "Cart is Empty");
            model.addAttribute("listCat", listCat);
            model.addAttribute("idCategorySelected", -1);
            model.addAttribute("listProd", listProd);
            model.addAttribute("email", ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        }
        else
        {
            Double subTotal = orderDetailsDao.getTotalByIdOrderAndIdCustomer(idOrder, customer.getId());
            Double tax = 18.00;
            Double grandTotal = subTotal + 100*tax/subTotal;

            model.addAttribute("listCart", listCart);
            model.addAttribute("subTotal", Helper.rsFormat(subTotal));
            model.addAttribute("tax",tax);
            model.addAttribute("grandTotal",Helper.rsFormat(Math.round(grandTotal)));
            model.addAttribute("prodDao",prodDao);
            model.addAttribute("customer", customer);
            model.addAttribute("idOrder", idOrder);

        }

        signInOutCode(model);
        return resultPage;
    }


    @GetMapping("/delete/{id}/")
    public String deleteOrders(Model model, @PathVariable long id)
    {
        orderDetailsDao.deleteById(id);
        List<Orders> list = orderDao.findAll();
        model.addAttribute("list",list);
        return"redirect:/orders/cart/-1/";

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

    @GetMapping("/total/order/")
    public String totalOrders(Model model)
    {
        List<Orders> listOrders = orderDao.findAll();
        model.addAttribute("listOrders", listOrders);

         model.addAttribute("userDao", userDao);
       // model.addAttribute("prodDao",prodDao);
        // model.addAttribute("orders", orders);

        return "ordersAdmin";
    }

    @GetMapping("/status/{idOrder}/{idStatus}/")
    @ResponseBody
    public int changeStatus(@PathVariable long idOrder, @PathVariable int idStatus)
    {
        int result = 0;
        Orders order = orderDao.getReferenceById(idOrder);
        order.setIdStatus(idStatus);
        orderDao.save(order);
        result = 1;
        return result;
    }
    @GetMapping("total/delete/{id}/")
    public String delete(Model model, @PathVariable long id)
    {
        orderDetailsDao.deleteById(id);

        return"redirect:/orders/cart/";

    }

    @GetMapping("/admin/delete/{id}/")
    public String deleteOrder(Model model, @PathVariable long id)
    {
        orderDao.deleteById(id);

        List<Orders> listOrders = orderDao.findAll();
        model.addAttribute("listOrders",listOrders);

        return"ordersAdmin";

    }



}
