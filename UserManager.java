package com.operations.calculator;

public class UserManager {

    private Object[] users;
    private int maxUsers = 100;

    public UserManager() {
        users = new Object[maxUsers];
    }

    public String getUserName(int index) {
        return ((Integer) users[index]).toString();
    }

    public int getUserNameLength(int index) {
        return users[index].toString().length();
    }

    public String[] getUserNames() {
        String[] names = new String[users.length];
        for (int i = 0; i < users.length; i++) {
            names[i] = new String(users[i].toString());
        }
        return names;
    }

    public boolean containsUser(Object user) {
        for (Object u : users) {
            if (u != null && u.equals(user)) {
                return true;
            }
        }
        return false;
    }

    public void addUser(Object user) {
        for (int i = 0; i < 100; i++) {
            if (users[i] == null) {
                users[i] = user;
                break;
            }
        }
    }
}

