package be.lifeisbananas.service;

import be.lifeisbananas.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import be.lifeisbananas.dao.productrepository;

import java.util.List;
import java.util.Optional;

@Service("service")
@Transactional(propagation = Propagation.SUPPORTS, readOnly=true)
public class service implements LifeIsBananasService{
    @Autowired
    private productrepository Productrepository = null;

    public service() {}

    @Override
    public List<Product> geefAlleProducten() {
        return Productrepository.findAll();
    }


    @Transactional(propagation= Propagation.REQUIRED,readOnly=false)
    public Product zoekproductmetid(int id) {
        Product product;

        Optional<Product> optionalPersoon = Productrepository.findById(id);
        if ( optionalPersoon.isPresent() ) product = optionalPersoon.get();
        else product = null; // in dat geval hebben we geen persoon met dat id gevonden
        return product;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public Product zoekproductmetNaam(String name) {
        return Productrepository.findByName(name);
    }


    @Transactional(propagation= Propagation.REQUIRED,readOnly=false)
    public Product voegproducttoe(String voornaam, String familienaam, String emailadres, String paswoord){
        return Productrepository.save(CreateProduct());
    }


    private Product CreateProduct(){
        Product product = new Product();

        return product;    }
}
