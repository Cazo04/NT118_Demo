package com.example.demont118;

public class UserTest {
    private String id;
    private String name;
    public UserTest(String id, String name) {
        this.id = id;
        this.name = name;
    }
    public UserTest() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
