package be.lifeisbananas.state;

import be.lifeisbananas.Product;
import be.lifeisbananas.ProductStatus;

public class InDevelopmentState implements ProductState {

    @Override
    public String promote(Product product) {
        product.setState(new TestedState());
        return "Product is now TESTED";
    }

    @Override
    public String reject(Product product) {
        return "Cannot reject: product is still IN_DEVELOPMENT";
    }

    @Override
    public ProductStatus getStatus() {
        return ProductStatus.IN_DEVELOPMENT;
    }
}
