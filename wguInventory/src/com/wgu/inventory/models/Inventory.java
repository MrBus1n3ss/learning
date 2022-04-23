package com.wgu.inventory.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {

        private static ObservableList<Part> allParts = FXCollections.observableArrayList();

        private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

        //Parts
        public static void addPart(Part newPart) {
            allParts.add(newPart);
        }
        public static Part lookupPart(int partId){
            for(Part part: Inventory.getAllParts()){
                if(partId == part.getId()) {
                    Part foundedPart;
                    foundedPart=part;
                    return foundedPart;
                }
            }
            return null;
        }

        public static ObservableList<Part> lookupPart(String name)
        {
            ObservableList<Part> searchedParts = FXCollections.observableArrayList();
            for(Part part: Inventory.getAllParts()){
                if(name.toLowerCase().equals(part.getName().toLowerCase())) {
                    searchedParts.add(part);
                }
            }
            return searchedParts;
        }
        public static void updatePart (int index, Part selectedPart)
    {
        allParts.set(index, selectedPart);
    }
        public static boolean deletePart (Part selectedPart)
    {
        return allParts.remove(selectedPart);
    }
        public static ObservableList<Part> getAllParts()
    {
        return allParts;
    }

        //Products
        public static void addProduct(Product newProduct)
        {
            allProducts.add(newProduct);
        }
        public static Product lookupProduct(int productId)
        {
            for(Product product: Inventory.getAllProducts()){
                if(productId == product.getId()) {
                    Product foundedProduct;
                    foundedProduct=product;
                    return foundedProduct;
                }
            }
            return null;
        }
        public static ObservableList<Product> lookupProduct(String name) {
            ObservableList<Product> searchedProducts = FXCollections.observableArrayList();
            for(Product product: Inventory.getAllProducts()){
                if(name.toLowerCase().equals(product.getName().toLowerCase())) {
                    searchedProducts.add(product);
                }
            }
            return searchedProducts;
        }
        public static void updateProduct (int index, Product selectedProduct)
        {
            allProducts.set(index, selectedProduct);
        }
        public static boolean deleteProduct (Product selectedProduct)
        {
            return allProducts.remove(selectedProduct);
        }
        public static ObservableList<Product> getAllProducts()
        {
            return allProducts;
        }
}
