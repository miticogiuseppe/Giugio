public class Autenticazione {
private String username;
private String password;
private String tenant;

public Autenticazione() {
	
}

public Autenticazione(String username, String password, String tenant) {
	this.username = username;
	this.password = password;
	this.tenant = tenant;
}

/**
 * @return the username
 */
public String getUsername() {
	return username;
}

/**
 * @param username la username da impostare
 */
public void setUsername(String username) {
	this.username = username;
}

/**
 * @return la password
 */
public String getPassword() {
	return password;
}

/**
 * @param password la password da impostare
 */
public void setPassword(String password) {
	this.password = password;
}

/**
 * @return il tenant
 */
public String getTenant() {
	return tenant;
}

/**
 * @param tenant il tenant da impostare
 */
public void setTenant(String tenant) {
	this.tenant = tenant;
}
}
