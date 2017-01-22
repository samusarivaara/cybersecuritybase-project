package sec.project.repository;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class SignupRepositoryImpl implements SignupRepositoryCustom{
    
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<String> getSignupsByName(String name, String owner) {
        
        // Vulnerability #1 OWASP 2013-A1-Injection.
        // Q: How to fix this?
        // A: Use parameter binding http://www.thoughts-on-java.org/jpa-native-queries/ 
        // or declare findByName query on JPA repository (SignupRepository) interface.
        
        // Fix me
        // Query query = em.createNativeQuery("SELECT name from Signup WHERE owner = ? AND name = ?");
        // query.setParameter(1, owner);
        // query.setParameter(2, name);
        
        Query query = em.createQuery("SELECT name FROM Signup WHERE owner='" + owner + "' AND name = '"+name+"'");
        List<String> names = query.getResultList();
        return names;
    }
    
}
