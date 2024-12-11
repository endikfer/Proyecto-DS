package es.deusto.ingenieria.sd.google.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class GoogleUser {
    @Id
    private String email;
    private String password;
    
    public GoogleUser(String email, String password) {
        super();
        this.email = email;
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
