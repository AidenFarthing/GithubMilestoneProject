package com.sparta.utils;

import java.util.ResourceBundle;

public class Config {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("config");

    public static String getToken(){
        return bundle.getString("github.token");
    }

    public static String getOwner(){
        return bundle.getString("repository.owner");
    }

    public static String getRepo(){
        return bundle.getString("repository.name");
    }

    public static String getRepositoryID(){
        return bundle.getString("repository.id");
    }

    public static String getGitHubBaseUri(){
        return bundle.getString("graphql.url");
    }

    public static String getRESTBaseUri(){
        return bundle.getString("github.resturi");
    }

}
