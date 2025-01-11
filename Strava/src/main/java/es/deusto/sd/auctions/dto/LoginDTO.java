package es.deusto.sd.auctions.dto;

public class LoginDTO {
	
	private String name;

	// Constructor without parameters
	public LoginDTO() {
	}

	// Constructor with parameters
	public LoginDTO(String name) {
		this.name = name;
	}

	// Getters y Setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
