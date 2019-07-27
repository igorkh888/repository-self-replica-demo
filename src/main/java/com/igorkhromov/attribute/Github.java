package com.igorkhromov.attribute;

public class Github {

    private String oauthURL;

    public Github(String oauthURL) {
        this.oauthURL = oauthURL;
    }

    public String getOauthURL() {
        return oauthURL;
    }

    public void setOauthURL(String oauthURL) {
        this.oauthURL = oauthURL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Github github = (Github) o;

        return oauthURL != null ? oauthURL.equals(github.oauthURL) : github.oauthURL == null;
    }

    @Override
    public int hashCode() {
        return oauthURL != null ? oauthURL.hashCode() : 0;
    }
}
