package be.lifeisbananas.state;

import be.lifeisbananas.Product;
import be.lifeisbananas.ProductStatus;

public class ApprovedState implements ProductState {

    @Override
    public String promote(Product product) {
        product.setState(new InProductionState());
        return "Product is now IN_PRODUCTION";
    }

    @Override
    public String reject(Product product) {
        return "Cannot reject: product is already APPROVED";
    }

    @Override
    public ProductStatus getStatus() {
        return ProductStatus.APPROVED;
    }
}
