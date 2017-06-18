package korenski.controller.autorizacija.dtos;

public class LoggedAndRole {
	
	private boolean logged;
	private String name;
	
	public LoggedAndRole(boolean logged, String name) {
		super();
		this.logged = logged;
		this.name = name;
	}
	
	public LoggedAndRole() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public boolean isLogged() {
		return logged;
	}
	public void setLogged(boolean logged) {
		this.logged = logged;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	

}
