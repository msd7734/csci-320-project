package model;

public class Game {

	private String name;
	private String appId;
	private String genre;
	private int owners;

	public Game() {

	}

	public Game(String appId, String name, String genre) {
		this.name = name;
		this.genre = genre;
	}
	
	public Game(String appId, String name, String genre, int owners) {
		this.appId = appId;
		this.name = name;
		this.genre = genre;
		this.setOwners(owners);
	}
	
	public void incOwners(){
		owners += 1;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the appId
	 */
	public String getAppId() {
		return appId;
	}

	/**
	 * @param appId
	 *            the appId to set
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}

	/**
	 * @return the genre
	 */
	public String getGenre() {
		return genre;
	}

	/**
	 * @param genre
	 *            the genre to set
	 */
	public void setGenre(String genre) {
		this.genre = genre;
	}

	/**
	 * @return the owners
	 */
	public int getOwners() {
		return owners;
	}

	/**
	 * @param owners the owners to set
	 */
	public void setOwners(int owners) {
		this.owners = owners;
	}

}
