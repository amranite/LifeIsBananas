package be.lifeisbananas.state;

import be.lifeisbananas.Product;
import be.lifeisbananas.ProductStatus;

public interface ProductState {

    String promote(Product product);

    String reject(Product product);

    ProductStatus getStatus();
}
