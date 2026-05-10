package be.lifeisbananas.state;

import be.lifeisbananas.Product;
import be.lifeisbananas.ProductStatus;

public class InProductionState implements ProductState {

    @Override
    public String promote(Product product) {
        return "Product is already IN_PRODUCTION and cannot be promoted further";
    }

    @Override
    public String reject(Product product) {
        return "Cannot reject: product is already IN_PRODUCTION";
    }

    @Override
    public ProductStatus getStatus() {
        return ProductStatus.IN_PRODUCTION;
    }
}
