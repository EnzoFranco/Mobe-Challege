package com.example.challenge;

public class Action {
    private String nom;
    private boolean valide;

    public Action (String nom) {
        this.nom = nom;
        valide = false;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public boolean isValide() {
        return valide;
    }

    public void setValide(boolean valide) {
        this.valide = valide;
    }
}
