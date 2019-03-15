package com.example.challenge;

import android.media.Image;
import android.widget.ImageView;

import java.util.List;

public class Plat {
    private String nom;
    private List<Action> actions;
    private int image;

    public Plat (String nom, List<Action> actions, int image) {
        this.nom = nom;
        this.actions = actions;
        this.image = image;
    }
}
