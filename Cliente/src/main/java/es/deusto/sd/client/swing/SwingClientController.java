/**
 * This code is based on solutions provided by Claude Sonnet 3.5 and 
 * adapted using GitHub Copilot. It has been thoroughly reviewed 
 * and validated to ensure correctness and that it is free of errors.
 */
package es.deusto.sd.client.swing;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import es.deusto.sd.client.data.Article;
import es.deusto.sd.client.data.Category;
import es.deusto.sd.client.data.Reto;
import es.deusto.sd.client.data.RetoAceptado;
import es.deusto.sd.client.data.Sesion;
import es.deusto.sd.client.data.Usuario;
import es.deusto.sd.client.proxies.HttpServiceProxy;
import es.deusto.sd.client.proxies.IStravaServiceProxy;

/**
 * SwingClientController class acts as a Controller in the Model-View-Controller 
 * (MVC) architectural pattern, managing the interaction between the SwingClientGUI 
 * (the View) and the IAuctionsServiceProxy (the Model). This class is responsible 
 * for handling user input, communicating with the service layer, and updating 
 * the view accordingly.
 * 
 * The class encapsulates the logic for user authentication (login/logout), 
 * retrieving categories and articles, and placing bids on articles. By utilizing 
 * the IAuctionsServiceProxy interface, the controller can interact with various 
 * implementations of the service proxy, such as HttpServiceProxy or RestTemplateServiceProxy, 
 * without being tightly coupled to any specific implementation. This promotes flexibility 
 * and allows for easier testing and maintenance of the application.
 * 
 * (Description generated with ChatGPT 4o mini)
 */
public class SwingClientController {
	// Service proxy for interacting with the AuctionsService using HTTP-based implementation
	private IStravaServiceProxy serviceProxy = new HttpServiceProxy();
	// Token to be used during the session
    private String token;

	

    public List<Category> getCategories() {
        return serviceProxy.getAllCategories();
    }

    public List<Article> getArticlesByCategory(String categoryName, String currency) {
        return serviceProxy.getArticlesByCategory(categoryName, currency);
    }

    public Article getArticleDetails(Long articleId, String currency) {
        return serviceProxy.getArticleDetails(articleId, currency);
    }

    public void placeBid(Long articleId, Float amount, String currency) {
        serviceProxy.makeBid(articleId, amount, currency, token);
    }
    
    public void crearReto(String nombre, String deporte, LocalDate fecha_inicio, LocalDate fecha_fin, Integer distancia,
			Integer tiempo) {
        serviceProxy.crearReto(nombre, deporte, fecha_inicio, fecha_fin, distancia, tiempo, token);
    }
    
    public List<Reto> consultarRetos(String deporte, String fecha) {
        return serviceProxy.consultarReto(deporte, fecha, token);
    }
    
    public List<RetoAceptado> consultarRetosAceptados() {
        return serviceProxy.consultarRetosAceptados(token);
    }
    
    public void aceptarReto(Long retoId) {
        serviceProxy.aceptarReto(retoId, token);
    }
    
	public List<Usuario> obtenerTodosLosUsuarios() {
		return serviceProxy.obtenerTodosLosUsuarios();
	}
	
	public Map<String, Usuario> obtenerTokens() {
		return serviceProxy.obtenerTokens();
	}
	
	public void registro(String nombre, String email, String tipo, String fecha_nac, float peso, int altura, int frec_car_max, int frec_car_rep) {
		serviceProxy.registro(nombre, email, tipo, fecha_nac, peso, altura, frec_car_max, frec_car_rep);
	}
	
	public boolean logIn(String email, String contrasenia) {
        try {
    		token = serviceProxy.logIn(email, contrasenia);      
            return true;
        } catch (RuntimeException e) {
            throw new RuntimeException("Login failed: " + e.getMessage());

        }
	}
	
	public void LogOut() {
		serviceProxy.LogOut(token);
	}
	
	public List<Sesion> getSesionesRecientes() {
		return serviceProxy.getSesionesRecientes();
	}
	
	public List<Sesion> getSesionesPorFecha(String startDate, String endDate) {
		return serviceProxy.getSesionesPorFecha(startDate, endDate);
	}
	
	public void crearSesion(Long id,String titulo,String deporte, double distancia,LocalDate fechaInicio,int duracion) {
		serviceProxy.crearSesion(id, titulo, deporte, distancia, fechaInicio, duracion);
	}

}