package es.deusto.sd.auctions.dto;

public class RetoDTO {
	
	private String name;

	// Constructor without parameters
	public RetoDTO() {
	}

	// Constructor with parameters
	public RetoDTO(String name) {
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
