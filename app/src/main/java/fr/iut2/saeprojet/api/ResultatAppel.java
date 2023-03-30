package fr.iut2.saeprojet.api;

import static fr.iut2.saeprojet.StageAppActivity.getContext;

import android.widget.Toast;

import fr.iut2.saeprojet.LoginActivity;
import fr.iut2.saeprojet.StageAppActivity;

public abstract class ResultatAppel<T> {
    public abstract void traiterResultat(T response);

    public void traiterErreur() {
        Toast.makeText(getContext(), "Impossible de se connecter au serveur", Toast.LENGTH_SHORT).show();
    }
}