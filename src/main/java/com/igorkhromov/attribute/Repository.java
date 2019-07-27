package com.igorkhromov.attribute;

public class Repository {

    private boolean isExists;

    private String githubURL;

    private String forkURI;

    private String removeURI;

    public boolean isExists() {
        return isExists;
    }

    public void setExists(boolean exists) {
        isExists = exists;
    }

    public String getGithubURL() {
        return githubURL;
    }

    public void setGithubURL(String githubURL) {
        this.githubURL = githubURL;
    }

    public String getForkURI() {
        return forkURI;
    }

    public void setForkURI(String forkURI) {
        this.forkURI = forkURI;
    }

    public String getRemoveURI() {
        return removeURI;
    }

    public void setRemoveURI(String removeURI) {
        this.removeURI = removeURI;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Repository that = (Repository) o;

        if (isExists != that.isExists) return false;
        if (githubURL != null ? !githubURL.equals(that.githubURL) : that.githubURL != null)
            return false;
        if (forkURI != null ? !forkURI.equals(that.forkURI) : that.forkURI != null)
            return false;
        return removeURI != null ? removeURI.equals(that.removeURI) : that.removeURI == null;
    }

    @Override
    public int hashCode() {
        int result = (isExists ? 1 : 0);
        result = 31 * result + (githubURL != null ? githubURL.hashCode() : 0);
        result = 31 * result + (forkURI != null ? forkURI.hashCode() : 0);
        result = 31 * result + (removeURI != null ? removeURI.hashCode() : 0);
        return result;
    }
}
