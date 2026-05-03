package be.lifeisbananas.dao;

import be.lifeisbananas.Product;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository("productDao")
@Transactional(propagation= Propagation.SUPPORTS, readOnly=true)
public class ProductHibernateDao implements ProductDao {

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory (SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    private Session currentSession(){
        return sessionFactory.getCurrentSession();
    }

    @Transactional(propagation= Propagation.REQUIRED, readOnly=false)
    public Product addProduct(Product product) {
        currentSession().persist(product);
        currentSession().flush();
        return product;
    }

    public List<Product> getProductsByRecipeId(int recipeId) {
        // Haal alle producten op waarvan het 'baseRecipe.recipeID' overeenkomt met de parameter
        return currentSession()
                .createQuery("from Product p where p.baseRecipe.recipeID = :rid", Product.class)
                .setParameter("rid", recipeId)
                .list();
    }

    @Transactional(propagation= Propagation.REQUIRED, readOnly=false)
    public void deleteProduct(Product product) {
        currentSession().remove(product);
        currentSession().flush();
    }
}