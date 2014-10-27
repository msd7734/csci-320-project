package beans;

/**
 * 
 * @author Kyle Jennings
 * Program Description: Bean for game search page
 * GameSearchBean.java
 * 
 * File:
 *	$Id$
 * 
 * Revisions:
 * 		$Log$
 *
 * Oct 22, 2014
 */

//@ViewScoped
public class GameSearchBean {
	
	private String genre;
	private String name;
	
	public GameSearchBean(){
		
	}
	
	//@PostConstruct
	public void init() {
		
	}

	//Getters and Setters
	
	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}
}
