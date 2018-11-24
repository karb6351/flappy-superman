package com.example.david.testproject;

import java.io.Serializable;

public class User implements Serializable{
    public int score;
    public String name;

    public User(){
        score = 0;
        name = "User";
    }

    public User(String name, int score){
        this.name = name;
        this.score = score;
    }
}
