package com.journey.model;

public class User {
    String email;
    String gender;
    int age;
    int score;

    public User(String email, String gender, int age, int score) {
        this.email = email;
        this.gender = gender;
        this.age = age;
        this.score = score;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
