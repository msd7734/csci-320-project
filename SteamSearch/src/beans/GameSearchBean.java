package beans;


import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class GameSearchBean {
	
	private String genre;
	private String name;
	
	public GameSearchBean(){
		
	}
	
	@PostConstruct
	public void init() {
		
	}

	//Getters and Setters
	
	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
