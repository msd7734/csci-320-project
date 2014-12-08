package beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import model.Game;
import model.GameCopy;
import model.SteamAccount;
import services.AccountService;
import services.FriendService;
import services.GameService;
import services.GamesService;
import services.UserService;

@ManagedBean
@ViewScoped
public class GameSearchBean implements Serializable {
	
	private static final long serialVersionUID = 123067408737606468L;

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;
	
	private String genre;
	private String name;
	private String studio;
	private String owners;
	private boolean useFriendslist;
	private boolean similarToLibrary;
	private String sortBy;
	
	private GameService gameService;
	private GamesService gamesService;
	private FriendService friendService;
	private AccountService accountService;
	private UserService userService;
	
	private String table;
	private int resultsPage;
	private int maxPages;
	private String pages;
	private ArrayList<Game> searchResults;
	private ArrayList<SteamAccount> friends;
	private ArrayList<GameCopy> games;
	private ArrayList<Game> friendsGames;
	
	
	private Set<String> gamesSet;
	
	public GameSearchBean(){
		
	}
	
	@PostConstruct
	public void init() {
		try {
			setup();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setup() throws SQLException{
		setUserService(new UserService());
		gamesService = new GamesService();
		friendService = new FriendService();
		accountService = new AccountService();
		
		gamesSet = new HashSet<String>(100);
		
		String username = sessionBean.getUsername();
		
		String steamId = userService.getSteamId(username);
		
		SteamAccount account = accountService.getSteamAccount(steamId);
		//SteamAccount account = sessionBean.getAccount();
		if(account != null) {
			//friends = friendService.getFriendList(steamId);
			games = gamesService.getAccountGames(steamId);
		} else {
			//friends = new ArrayList<SteamAccount>();
			games = new ArrayList<GameCopy>();
		}
		friendsGames = friendService.getFriendsGames(steamId);
		
		/*
		System.out.println("friends: " + friends.size());
		System.out.println("games: " + games.size());
		int gameTotal = 0;
		for(SteamAccount friend : friends){
			ArrayList<GameCopy> gamesss = gamesService.getAccountGames(friend.getSteamId());
			gameTotal += gamesss.size();
			System.out.println(gamesss.size());
			for(GameCopy friendGame : gamesss){
				gamesSet.add(friendGame.getAppId());
			}
		}
		*/
		
		/*
		for(int i = 0; i<games.size(); i++){
			String userGameID = games.get(i).getAppId();
			for(int c = 0; c<friendsGames.size(); c++){
				if(userGameID.compareTo(friendsGameIDs.get(c)) == 0){
					friendsGames.remove(c);
					friendsGameIDs.remove(c);
				}
			}
		}
		*/
		
		
		this.gameService = new GameService();
		this.genre = null;
		this.name = null;
		this.resultsPage = 1;
		this.pages = "1";
	}
	
	public void search(ActionEvent actionEvent){
		ArrayList<Game> results;
		this.resultsPage = 1;
		
		if(!useFriendslist){
			results = gameService.SearchGames(genre, name, sortBy);
			
			
			if(results != null){
				if(owners.compareTo("") != 0){
					int ownerMin = Integer.parseInt(owners);
					for(int i = 0; i< results.size(); i++){
						Game cur = results.get(i);
						if( ownerMin > cur.getOwners()){
							results.remove(i);
						}
					}
				}
				
				
			}
			
		} else {
			results = friendsGames;
			if(name.compareTo("") != 0){
				System.out.println("name match");
				for(int i = 0; i< results.size(); i++){
					Game cur = results.get(i);
					if( name.compareTo(cur.getName()) != 0){
						results.remove(i);
					}
				}
			}
			
			if(owners.compareTo("") != 0){
				System.out.println("min owners");
				int ownerMin = Integer.parseInt(owners);
				System.out.println("ownerMin: "+ ownerMin);
				for(int i = 0; i< results.size(); i++){
					Game cur = results.get(i);
					if( ownerMin > cur.getOwners()){
						System.out.println("owners: "+ cur.getOwners());
						results.remove(i);
					}
				}
			}
		}
		this.maxPages = (results.size()/25);
		if(results.size()% 25 > 0)
			this.maxPages += 1;
		this.searchResults = results;
		getCurrentPage();
		
	}
	
	public void setResultsPage(int resultsPage){
		this.resultsPage = resultsPage;
	}
	
	public void decResultsPage(){
		if(this.resultsPage> 1){
			this.resultsPage -= 1;
			getCurrentPage();
		}
	}
	
	public void incResultsPage(){
		if(this.resultsPage < this.maxPages){
			this.resultsPage += 1;
			getCurrentPage();
		}
	}

	public void getCurrentPage(){
		if((searchResults != null) && (searchResults.size() > 0)){
			String pages = "";
			String table = "";
			
			int start = (resultsPage -1) * 25;
			int finish = resultsPage * 25;
			if( resultsPage == maxPages)
				finish = start + searchResults.size() % 25;
			
			table += "<table class='table'><thead><tr class='filters'><th>#</th><th>Game Title</th><th>GameID</th><th>Owners</th></tr></thead><tbody>";
			for(int i = start; i< finish; i++){
				Game cur = searchResults.get(i);
				table += "<tr><td>" + (i + 1) + "</td>";
				table += "<td>" + cur.getName() + "</td>";
				table += "<td>" + cur.getAppId() + "</td>";
				table += "<td>" + cur.getOwners() + "</td>";
				table += "</tr>";
			}
			table += "</tbody></table>";
			
			//for(int i = 1; i<= this.maxPages; i++){
			pages += resultsPage;
			//}
			
			this.pages = pages;
			this.table = table;
		}
	}
	//Getters and Setters
	
	/**
	 * @return the userService
	 */
	public UserService getUserService() {
		return userService;
	}

	/**
	 * @param userService the userService to set
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public SessionBean getSessionBean() {
		return sessionBean;
	}

	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}
	
	public String getPages(){
		return this.pages;
	}
	
	public String getTable(){
		return this.table;
	}
	
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
	
	public String getStudio() {
		return studio;
	}

	public void setStudio(String studio) {
		this.studio = studio;
	}

	public String getOwners() {
		return owners;
	}

	public void setOwners(String owners) {
		this.owners = owners;
	}
	
	public boolean getUseFriendslist() {
		return useFriendslist;
	}

	public void setUseFriendslist(boolean useFriendslist) {
		this.useFriendslist = useFriendslist;
	}
	
	public boolean getSimilarToLibrary() {
		return similarToLibrary;
	}

	public void setSimilarToLibrary(boolean similarToLibrary) {
		this.similarToLibrary = similarToLibrary;
	}
	
	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
	
}
