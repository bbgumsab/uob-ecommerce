package com.example.demo.controllers;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.models.Product;
import com.example.demo.repo.ProductRepo;
import com.example.demo.repo.CategoryRepo;

@Controller
public class ProductCotroller {

    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;

    @Autowired
    public ProductCotroller(ProductRepo productRepo, CategoryRepo categoryRepo) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
    }

    @GetMapping("/products")
    public String listProducts(Model model) {
        List<Product> products =  productRepo.findAllWithCategories();        ;
        model.addAttribute("products", products);
        return "products/index";
    }

    @GetMapping("/products/create")
    public String showCreateProductForm(Model model) {
        var newProduct = new Product();
        model.addAttribute("categories", categoryRepo.findAll());
        model.addAttribute("product", newProduct);
        return "products/create";
    }

    @PostMapping("/products/create")
    public String createProduct(@Valid @ModelAttribute Product newProduct, BindingResult bindingResult, Model model) {
        System.out.println(bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryRepo.findAll());
            System.out.println("Error in product submitted data");
            return "products/create";

        }
        System.out.println("Valid product received");
        productRepo.save(newProduct);
        return "redirect:/products";
    }

    @GetMapping("/products/{id}")
    public String productDetails(@PathVariable Long id, Model model) {
        var product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        model.addAttribute("product", product);
        return "products/details";
    }

    @GetMapping("/products/{id}/edit")
    public String showUpdateProduct(@PathVariable Long id, Model model) {
        Product product = productRepo.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        model.addAttribute(product);
        model.addAttribute("categories", categoryRepo.findAll());
        return "products/edit";
    }

    @PostMapping("/products/{id}/edit")
    public String updateProduct(@PathVariable Long id, @Valid @ModelAttribute Product product,
            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryRepo.findAll());
            return "products/edit";
        }
        product.setId(id); // Ensure we're updating the correct product
        productRepo.save(product);
        return "redirect:/products";
    }

    @GetMapping("/products/{id}/delete")
    public String showDeleteProductForm(@PathVariable Long id, Model model) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        model.addAttribute("product", product);
        return "products/delete";
    }

    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        productRepo.deleteById(id);
        return "redirect:/products";
    }
}
