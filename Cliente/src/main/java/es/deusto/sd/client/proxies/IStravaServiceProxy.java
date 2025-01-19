/**
 * This code is based on solutions provided by Claude Sonnet 3.5 and 
 * adapted using GitHub Copilot. It has been thoroughly reviewed 
 * and validated to ensure correctness and that it is free of errors.
 */
package es.deusto.sd.client.proxies;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import es.deusto.sd.client.data.Article;
import es.deusto.sd.client.data.Category;
import es.deusto.sd.client.data.Reto;
import es.deusto.sd.client.data.RetoAceptado;
import es.deusto.sd.client.data.Sesion;
import es.deusto.sd.client.data.Usuario;

/**
 * IAuctionsServiceProxy interface defines a contract for communication 
 * with the AuctionsService, enabling different implementations to provide 
 * the same set of functionalities for client interactions. This interface 
 * is aligned with the Service Proxy design pattern, which aims to create 
 * an intermediary between the client and the underlying service. It includes 
 * methods for user authentication (login/logout), retrieving categories 
 * and articles, and placing bids on articles.
 * 
 * By defining a common interface, we promote loose coupling between 
 * the client code and the underlying service implementations. This allows 
 * for greater flexibility and easier maintenance, as clients can work 
 * with any implementation of the interface without needing to know the 
 * specifics of how the HTTP communication is handled. For instance, 
 * both `HttpServiceProxy` and `RestTemplateServiceProxy` can implement 
 * this interface, allowing developers to switch between different 
 * implementations based on performance requirements, error handling strategies, 
 * or other factors without modifying the client code.
 * 
 * Additionally, using an interface facilitates unit testing and mocking, 
 * as it allows for the creation of test doubles that adhere to the same 
 * contract. This leads to more robust and maintainable code, as the 
 * interface serves as a clear specification of the expected behavior 
 * for any service proxy implementation.
 * 
 * (Description generated with ChatGPT 4o mini)
 */
public interface IStravaServiceProxy {

	// Method to retrieve all categories
	List<Category> getAllCategories();

	// Method to retrieve articles by category name
	List<Article> getArticlesByCategory(String categoryName, String currency);

	// Method to get details of a specific article by ID
	Article getArticleDetails(Long articleId, String currency);

	// Method to place a bid on an article
	void makeBid(Long articleId, Float amount, String currency, String token);
	
	List<Usuario> obtenerTodosLosUsuarios();
	
	Map<String, Usuario> obtenerTokens();
	
	void registro(String nombre, String email, String tipo, String fecha_nac, float peso, int altura, int frec_car_max, int frec_car_rep);
	
	String logIn(String email, String contrasenia);
	
	void LogOut(String token);
	
	void crearReto(String nombre, String deporte, String fecha_inicio, 
			String fecha_fin, Integer distancia, Integer tiempo, String token);
	
	List<Reto> consultarReto(String deporte, String fecha, String token);
	
	void aceptarReto(Long retoId, String token);

	List<RetoAceptado> consultarRetosAceptados(String token);
	
	List<Sesion> getSesionesRecientes();
	
	List<Sesion> getSesionesPorFecha(String startDate, String endDate);
	
	void crearSesion(Long id,String titulo,String deporte, double distancia,LocalDate fechaInicio,int duracion);

	
}
