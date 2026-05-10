package be.lifeisbananas.state;

import be.lifeisbananas.Product;
import be.lifeisbananas.ProductStatus;

public class RejectedState implements ProductState {

    @Override
    public String promote(Product product) {
        return "Cannot promote product from REJECTED";
    }

    @Override
    public String reject(Product product) {
        return "Cannot reject: product is REJECTED";
    }

    @Override
    public ProductStatus getStatus() {
        return ProductStatus.REJECTED;
    }
}
