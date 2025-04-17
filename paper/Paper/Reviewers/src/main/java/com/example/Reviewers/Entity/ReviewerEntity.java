package com.example.Reviewers.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "reviewers")
public class ReviewerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String affiliation;

    @Column
    private String expertise;

    // Default constructor
    public ReviewerEntity() {
    }

    // Parameterized constructor
    public ReviewerEntity(int id, String name, String email, String affiliation, String expertise) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.affiliation = affiliation;
        this.expertise = expertise;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public String getExpertise() {
        return expertise;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    @Override
    public String toString() {
        return "Reviewer [id=" + id + 
               ", name=" + name + 
               ", email=" + email + 
               ", affiliation=" + affiliation + 
               ", expertise=" + expertise + "]";
    }
}