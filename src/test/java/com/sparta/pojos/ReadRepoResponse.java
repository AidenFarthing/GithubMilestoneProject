package com.sparta.pojos;

public class ReadRepoResponse {

    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data {
        private Repository repository;

        public Repository getRepository() {
            return repository;
        }
    }

    public static class Repository {
        private String id;
        private String name;
        private String description;
        private String visibility;
        private Owner owner;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getVisibility() {
            return visibility;
        }

        public Owner getOwner() {
            return owner;
        }
    }

    public static class Owner {
        private String login;

        public String getLogin() {
            return login;
        }
    }
}