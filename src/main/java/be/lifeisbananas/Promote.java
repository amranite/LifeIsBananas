package be.lifeisbananas;

import org.springframework.stereotype.Service;

@Service
public class Promote implements IPromote {

    @Override
    public String nextStatus(Product product) {
        return product.promote();
    }

    @Override
    public String reject(Product product) {
        return product.reject();
    }
}
