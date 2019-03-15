package com.example.challenge;

import android.media.Image;
import android.widget.ImageView;

import java.util.List;

public class Plat {
    private String nom;
    private List<Action> actions;
    private int image;
    private boolean valide;

    public Plat (String nom, List<Action> actions, int image) {
        this.nom = nom;
        this.actions = actions;
        this.image = image;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public boolean isValide() {
        return valide;
    }

    public void setValide(boolean valide) {
        this.valide = valide;
    }

    @Override
    public String toString() {
        return "Plat : " + nom;
    }
}
