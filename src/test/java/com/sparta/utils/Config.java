package com.sparta.utils;

import java.util.ResourceBundle;

public class Config {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("config");

    public static String getToken(){
        return bundle.getString("github.token");
    }

    public static String getOwner(){
        return bundle.getString("github.owner");
    }

    public static String getRepo(){
        return bundle.getString("github.repo");
    }

    public static String getRepositoryID(){
        return bundle.getString("github.repositoryID");
    }

    public static String getGitHubBaseUri(){
        return bundle.getString("github.baseuri");
    }
}
