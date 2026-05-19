package be.lifeisbananas.service;

import be.lifeisbananas.domain.*;
import java.util.List;

public interface LifeIsBananasService {
    public List<Product> geefAlleProducten();

    public Product voegproducttoe(String voornaam, String familienaam, String emailadres, String paswoord);

    public Product zoekproductmetid(int id);

    public Product zoekproductmetNaam(String Naam);
}

