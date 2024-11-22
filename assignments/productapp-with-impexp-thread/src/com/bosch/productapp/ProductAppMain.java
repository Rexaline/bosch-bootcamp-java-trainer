package com.bosch.productapp;

import com.bosch.productapp.exception.NoProductFoundException;
import com.bosch.productapp.exception.ProductException;
import com.bosch.productapp.exception.ProductValidationException;
import com.bosch.productapp.model.Product;
import com.bosch.productapp.model.ProductCategory;
import com.bosch.productapp.service.ProductService;
import com.bosch.productapp.service.ProductServiceImpl;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

public class ProductAppMain {


    public static void main(String[] args) {
        // TODO: SHOW MENU TO USER
//        1] Add Product
//        2] Update Product
//        3] Delete Product
//        4] View Product
//        5] View All Products
//        6] Print Statistics [Implement using Stream API]
//        7] Import [Product Details]
//        8] Export [Product Details]
//        9] Exit

        System.out.println("Welcome to ProductApp");
        Scanner in = new Scanner(System.in);
        ProductService service = new ProductServiceImpl();

        ExecutorService es = Executors.newCachedThreadPool();


        while(true) {
            try {
                showMenu(in, service, es);
            }catch (ProductValidationException e) {
                System.out.println("Validation Error: " + e.getMessage());
            } catch (NoProductFoundException e) {
                System.out.println("Invalid Product: " + e.getMessage());
            }catch (ProductException e) {
                System.out.println("Error: " + e.getMessage());
            }catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

    }

    private static void showMenu(Scanner in, ProductService service, ExecutorService es) throws ProductException, ExecutionException, InterruptedException {


        System.out.println("1] Add Product");
        System.out.println("2] Update Product");
        System.out.println("3] Delete Product");
        System.out.println("4] View Product");
        System.out.println("5] View All Products");
        System.out.println("6] Print Statistics");
        System.out.println("7] Import");
        System.out.println("8] Export");
        System.out.println("9] Exit");

        System.out.print("Enter your choice: ");
        int choice = in.nextInt();

        switch(choice) {
            case 1:
                Product product = captureProductDetails();
                service.addProduct(product);
                break;
            case 2:
                System.out.println("Update Product");
                break;
            case 3:
                System.out.println("Delete Product");
                break;
            case 4:
                System.out.print("Enter Product ID: ");
                int id = in.nextInt();
                Product product1 = service.viewProduct(id);
                System.out.println(product1);
                break;
            case 5:

                List<Product> products = service.viewAllProducts();
                if(products.size() > 0 ) {
                    showProductDetails(products);
                } else {
                    System.out.println("No products found.");
                }
                break;
            case 6:
                System.out.println("Print Statistics");
                break;
            case 7:
                Callable<Boolean> importTask = new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return service.importProducts();
                    }
                };
                Future<Boolean> impFuture = es.submit(importTask);
                System.out.println(impFuture.get() ? "Imported successfully" : "Import failed");

                break;
            case 8:
                Callable<Boolean> exportTask = new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return service.exportProducts();
                    }
                };
                Future<Boolean> expFuture = es.submit(exportTask);
                System.out.println(expFuture.get() ? "Exported successfully" : "Export failed");

                break;
            case 9:
                System.out.println("Thank you for using ProductApp");
                in.close();
                es.shutdown();
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice");
        }

    }

    private static Product captureProductDetails() {
        // TODO: Logic to capture the product details from user
        return new Product(ProductCategory.LAPTOPS, "Dell Inspiron", 45000, true);
    }

    private static void showProductDetails(List<Product> products) {
        for(Product product: products) {
            System.out.println(product);
        }
    }
}
