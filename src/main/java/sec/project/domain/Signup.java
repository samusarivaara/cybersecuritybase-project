package sec.project.domain;

import javax.persistence.Entity;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Signup extends AbstractPersistable<Long> {

    private String enteredByUser;

    private String name;
    private String address;

    public Signup() {
        super();
    }

    public Signup(String name, String address, String enteredByUser) {
        this();
        this.name = name;
        this.address = address;
        this.enteredByUser = enteredByUser;
    }

    public String getEnteredByUser() {
        return this.enteredByUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
