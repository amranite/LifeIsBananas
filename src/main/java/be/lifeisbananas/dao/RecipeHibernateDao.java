package be.lifeisbananas.dao;

import be.lifeisbananas.Recipe;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("recipeDao") // Vertelt Spring: "Dit is een database-hulpje"
@Transactional(propagation= Propagation.SUPPORTS, readOnly=true)
public class RecipeHibernateDao implements RecipeDao {

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory (SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    private Session currentSession(){
        return sessionFactory.getCurrentSession();
    }

    @Transactional(propagation= Propagation.REQUIRED, readOnly=false)
    public Recipe addRecipe(Recipe recipe) {
        currentSession().persist(recipe); // 'persist' betekent: voeg toe aan database
        currentSession().flush();
        return recipe;
    }

    @Transactional(propagation= Propagation.REQUIRED, readOnly=false)
    public void updateRecipe(Recipe recipe) {
        currentSession().merge(recipe); // Update het bestaande recept
    }

    public Recipe getRecipeById(int id) {
        return currentSession()
                .createQuery("from Recipe where recipeID = :id", Recipe.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    @Transactional(propagation= Propagation.REQUIRED, readOnly=false)
    public void deleteRecipe(Recipe recipe) {
        currentSession().remove(recipe); // Gooi het recept weg uit de database
        currentSession().flush();
    }
}