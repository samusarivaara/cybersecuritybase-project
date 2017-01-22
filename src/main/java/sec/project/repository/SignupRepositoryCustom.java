package sec.project.repository;

import java.util.List;

public interface SignupRepositoryCustom {
    
    public List<String> getSignupsByName(String name, String owner);
}
