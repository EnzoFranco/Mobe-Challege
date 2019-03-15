package com.example.challenge;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private List<Plat> plats;
    private List<Action> actionsBurger, actionsSalade, actionsPizza,
                    actionsHotdog, actionsFrites, actionsSushi,
                    actionsTacos, actionsDonut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView plat1 = findViewById(R.id.plat1);
        ImageView plat2 = findViewById(R.id.plat2);
        ImageView plat3 = findViewById(R.id.plat3);
        ImageView plat4 = findViewById(R.id.plat4);

        plats = new ArrayList<>();
        initActions();
        initPlats();
    }

    public void initPlats() {
        plats.add(new Plat("Burger", actionsBurger, R.drawable.burger));
        plats.add(new Plat("Salade", actionsSalade, R.drawable.salade));
        plats.add(new Plat("Pizza", actionsPizza, R.drawable.pizza));
        plats.add(new Plat("HotDog", actionsHotdog, R.drawable.hotdog));
        plats.add(new Plat("Frites", actionsFrites, R.drawable.frites));
        plats.add(new Plat("Sushi", actionsSushi, R.drawable.sushi));
        plats.add(new Plat("Tacos", actionsTacos, R.drawable.tacos));
        plats.add(new Plat("Donut", actionsDonut, R.drawable.donut));
    }

    public void initActions() {
        Action masquerLuminosite = new Action("Masquer luminosité");
        Action touchScreen = new Action ("Toucher l'écran");
        Action bougerTablette = new Action ("Bouger la tablette");
        Action parler = new Action ("Parler");

        actionsBurger = new ArrayList<>();
        actionsBurger.add(masquerLuminosite);
        actionsBurger.add(touchScreen);

        actionsDonut = new ArrayList<>();
        actionsDonut.add(parler);
        actionsDonut.add(masquerLuminosite);

        actionsFrites = new ArrayList<>();
        actionsFrites.add(bougerTablette);
        actionsFrites.add(touchScreen);
        actionsFrites.add(bougerTablette);

        actionsHotdog = new ArrayList<>();
        actionsHotdog.add(touchScreen);
        actionsHotdog.add(parler);

        actionsPizza = new ArrayList<>();
        actionsPizza.add(bougerTablette);
        actionsPizza.add(touchScreen);

        actionsSalade = new ArrayList<>();
        actionsSalade.add(bougerTablette);
        actionsSalade.add(touchScreen);

        actionsSushi = new ArrayList<>();
        actionsSushi.add(bougerTablette);
        actionsSushi.add(touchScreen);

        actionsTacos = new ArrayList<>();
        actionsTacos.add(bougerTablette);
        actionsTacos.add(touchScreen);
    }
}
