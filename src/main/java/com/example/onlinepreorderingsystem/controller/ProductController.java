package com.example.onlinepreorderingsystem.controller;

import com.example.onlinepreorderingsystem.Helper.FileUploader;
import com.example.onlinepreorderingsystem.dao.CategoryDao;
import com.example.onlinepreorderingsystem.dao.ProductDao;
import com.example.onlinepreorderingsystem.entity.Category;
import com.example.onlinepreorderingsystem.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class ProductController
{
    @Autowired
    ProductDao productDao;
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    FileUploader uploader;

    @GetMapping("/product/")
    public String category(Model model)
    {
        List<Category> listCategory = categoryDao.findAll();
        List<Product> listProduct = productDao.findAll();
        model.addAttribute("catDao", categoryDao);
        model.addAttribute("listProduct",listProduct);
        model.addAttribute("listCategory",listCategory);
        return "productDetails";
    }

    @PostMapping("/product/add/")
    public String addProduct(Model model, Long id, String name, String description, double price, double discount, Long idCategory, MultipartFile file)
    {
//        Product product = null;
//
//        if(id != null)
//        {
//            product = productDao.getReferenceById(id);
//            product.setName(name);
//            product.setDescription(description);
//            product.setPrice(price);
//            product.setDiscount(discount);
//            product.setIdCategory(idCategory);
//            product.setFile(file);
//        }
//        else
//        {
//            product = new Product(idCategory, name, description, price, discount,file);
//        }

        String fileName = file.getOriginalFilename();
        String extension = fileName.substring(fileName.indexOf(".")+1);
        String fileNewName = "product_" + name + "." + extension;
        uploader.uploadFile(file, fileNewName);

        Product product = new Product(idCategory, name, description, price, discount, fileNewName);
        if(id != null)
        {
            product.setId(id);
        }
        Product savedProduct = productDao.save(product);

        List<Category> listCategory = categoryDao.findAll();
        List<Product> listProduct = productDao.findAll();
        model.addAttribute("catDao", categoryDao);
        model.addAttribute("listProduct",listProduct);
        model.addAttribute("listCategory",listCategory);

        model.addAttribute("msg","Product  added successfully");

        return "productDetails";

    }

    @GetMapping("/product/delete/{id}/")
    public String deleteProduct(Model model, @PathVariable long id)
    {
        productDao.deleteById(id);

        List<Category> listCategory = categoryDao.findAll();
        List<Product> listProduct = productDao.findAll();
        model.addAttribute("catDao", categoryDao);
        model.addAttribute("listProduct",listProduct);
        model.addAttribute("listCategory",listCategory);

        return"ProductDetails";

    }

    @GetMapping("/product/update/{id}")
    public String updateProduct(Model model, Product product)
    {
        productDao.save(product);

        List<Category> listCategory = categoryDao.findAll();
        List<Product> listProduct = productDao.findAll();
        model.addAttribute("catDao", categoryDao);
        model.addAttribute("listProduct",listProduct);
        model.addAttribute("listCategory",listCategory);

        return "productDetails";
    }

}
