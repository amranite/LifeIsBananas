package be.lifeisbananas.dao;
import be.lifeisbananas.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;



public interface productrepository extends JpaRepository<Product, Integer> {

    Product findByName(String name);
}
