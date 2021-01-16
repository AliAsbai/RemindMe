package db;

import bo.model.Category;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.Date;

public class CategoryDB {

    public static Category checkCategory(String name, DB instance) throws Exception {
        Category category = new Category();
        TypedQuery<Long> query = instance.em.createNamedQuery("Category.CategoryExists", Long.class);
        query.setParameter("name", name);
        long exists = query.getSingleResult();
        if (exists != 0) {
            TypedQuery<Category> query1 = instance.em.createNamedQuery("Category.GetByName", Category.class);
            query1.setParameter("name", name);
            category = query1.getSingleResult();
        } else {
            category.setInsertDate(new Date());
            category.setName(name);
            instance.em.persist(category);
        }
        instance.em.flush();
        return category;
    }
}
