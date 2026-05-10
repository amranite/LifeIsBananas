package be.lifeisbananas.state;

import be.lifeisbananas.Product;
import be.lifeisbananas.ProductStatus;

public class TestedState implements ProductState {

    @Override
    public String promote(Product product) {
        product.setState(new ApprovedState());
        return "Product is APPROVED";
    }

    @Override
    public String reject(Product product) {
        product.setState(new RejectedState());
        return "Product rejected";
    }

    @Override
    public ProductStatus getStatus() {
        return ProductStatus.TESTED;
    }
}
