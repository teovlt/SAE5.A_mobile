package fr.iut2.saeprojet;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

import fr.iut2.saeprojet.api.APIClient;
import fr.iut2.saeprojet.api.APIService;

public class StageAppActivity extends AppCompatActivity {
    protected APIService apiInterface = null;

    public APIService getApiInterface() {
        return apiInterface;
    }
    private static StageAppActivity instance;

    public StageAppActivity() {
        instance = this;
    }

    public static Context getContext() {
        return instance;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Chargement de l'API
        apiInterface = APIClient.getAPIService();
    }

    private SharedPreferences getSharedPreferences() {
        return getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
    }

    public String getToken() {
        return getSharedPreferences().getString(getString(R.string.token_key), "no token");
    }

    public long getCompteId() {
        return getSharedPreferences().getLong(getString(R.string.login_id_key), 0);
    }

    public String getCompte_Id() {
        return getSharedPreferences().getString(getString(R.string.login_id_path), "");
    }

    protected void setCompteId(long id, String _id) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putLong(getString(R.string.login_id_key), id);
        editor.putString(getString(R.string.login_id_path), _id);
        editor.apply();
    }

    protected void setCompteDerniereConnexion(String derniereConnexion, int nb, String _id) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString("derniere_connexion" + nb + _id, derniereConnexion);
        editor.apply();
    }

    public String getDerniereConnexion(int nb, String _id) {
        return getSharedPreferences().getString("derniere_connexion" + nb + _id, "");
    }

    protected void setAuthData(String login, String token) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(getString(R.string.token_key), token);
        editor.putString(getString(R.string.login_key), login);
        editor.apply();
    }

    protected void setEnumValues(HashMap<String, String> entries) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();

        for (String key : entries.keySet()) {
            editor.putString(key, entries.get(key));
        }

        editor.apply();
    }

    protected String getEnumValue(String key) {
        SharedPreferences prefs = getSharedPreferences();

        return prefs.getString(key, "");
    }
}
