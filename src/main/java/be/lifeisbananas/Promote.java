package be.lifeisbananas;
import org.springframework.stereotype.Component;
import be.lifeisbananas.Product;
import be.lifeisbananas.ProductStatus;
import org.springframework.stereotype.Service;
import java.util.*;


@Service
public class Promote implements IPromote {
    @Override
    public String nextStatus( Product product) {
        if (product.getStatus() == ProductStatus.IN_DEVELOPMENT)
        {
            product.setStatus(ProductStatus.TESTED);
            return "product is now being tested";
        } else if (product.getStatus() == ProductStatus.TESTED) {
            product.setStatus(ProductStatus.APPROVED);
            return "product has been approved";
        }else if (product.getStatus() == ProductStatus.APPROVED) {
            product.setStatus(ProductStatus.IN_PRODUCTION);
            return "product is production";
        }else if (product.getStatus() == ProductStatus.IN_PRODUCTION) {
            return "product already in production";
        }
            else {
            return "unknown status";
        }
    }

    public String reject (Product product){
        if (product.getStatus() == ProductStatus.TESTED)
        {
            product.setStatus(ProductStatus.REJECTED);
            return "Rejected";
        }else if (product.getStatus() == ProductStatus.IN_DEVELOPMENT) {
            return "Cannot reject while In Development";
        }else if (product.getStatus() == ProductStatus.APPROVED) {
            return "Cannot reject when already Approved";
        }else if (product.getStatus() == ProductStatus.IN_PRODUCTION) {
            return "Cannot reject when In Production";
        }else return "unknown status";
    }
}
