package be.lifeisbananas.service;

import be.lifeisbananas.domain.*;
import java.util.List;

public interface LifeIsBananasService {
    public List<Product> geefAlleProducten();

    public Product voegproducttoe(String naam, Recipe recept);

    public Product zoekproductmetid(int id);

    public Product zoekproductmetNaam(String Naam);
}

