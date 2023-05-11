package com.example.onlinepreorderingsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Feedback
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long Id;
    String name;
    String message;

    public Feedback() {
    }

    public Feedback(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "Id=" + Id +
                ", name='" + name + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
