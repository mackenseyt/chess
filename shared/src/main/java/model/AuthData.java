package model;


import java.util.Objects;
import java.util.UUID;

public class AuthData {
    private String authToken;
    private String username;

    public AuthData(){}

    public AuthData(String username){
        this.authToken = generateAuthToken();
        this.username = username;
    }
    public AuthData(String username, String authToken){
        this.authToken = authToken;
        this.username = username;
    }

    private String generateAuthToken() {
        return UUID.randomUUID().toString();
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "AuthData{" +
                "authToken='" + authToken + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthData authData = (AuthData) o;
        return Objects.equals(authToken, authData.authToken) && Objects.equals(username, authData.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authToken, username);
    }

}
