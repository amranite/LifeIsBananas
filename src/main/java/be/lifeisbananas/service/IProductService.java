package be.lifeisbananas.service;

import be.lifeisbananas.Product;

public interface IProductService {

    void addProduct(int productID, String productName);

    Product getProductByID(int id);

    Product getCurrentProduct();

    String submitForTesting();

    void approve();

    String reject();

    void delete();

    void launch();
}
