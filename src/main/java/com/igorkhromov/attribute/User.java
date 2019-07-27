package com.igorkhromov.attribute;

public class User {

    private boolean isAuthorized;

    public User(boolean isAuthorized) {
        this.isAuthorized = isAuthorized;
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }

    public void setAuthorized(boolean authorized) {
        isAuthorized = authorized;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return isAuthorized == user.isAuthorized;
    }

    @Override
    public int hashCode() {
        return (isAuthorized ? 1 : 0);
    }
}
