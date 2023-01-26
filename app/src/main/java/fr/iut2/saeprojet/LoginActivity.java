package fr.iut2.saeprojet;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import fr.iut2.saeprojet.api.APIClient;
import fr.iut2.saeprojet.api.APIService;
import fr.iut2.saeprojet.entity.Auth;
import fr.iut2.saeprojet.entity.LoginResponse;
import fr.iut2.saeprojet.entity.OffreList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    //
    //public static final Preferences.Key<String> TOKEN_KEY = PreferencesKeys.stringKey("token_key");
    //public static RxDataStore<Preferences> dataStore;

    // API
    private APIService apiInterface;

    // View
    private EditText usernameView;
    private EditText passwordView;
    private Button loginView;
    private Button testView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Chargement de l'API
        apiInterface = APIClient.getAPIService();

        //
        //dataStore = new RxPreferenceDataStoreBuilder(this, /*name=*/ "settings").build();

        // Init view
        usernameView = findViewById(R.id.username);
        passwordView = findViewById(R.id.password);
        loginView = findViewById(R.id.login);
        testView = findViewById(R.id.test);

        //
        loginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        //
        testView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test();
            }
        });

    }

    private void login() {

        String login = usernameView.getText().toString();
        String password = passwordView.getText().toString();

        Auth auth = new Auth(login, password);

        Call<LoginResponse> call = apiInterface.login(auth);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, response.body().token, Toast.LENGTH_SHORT).show();

                    //
                    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(getString(R.string.token_key), response.body().token);
                    editor.apply();

                } else {
                    Toast.makeText(LoginActivity.this, "login not correct", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                call.cancel();
                Log.e("TAG",t.getMessage());

            }
        });
    }


    private void test() {

        //
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String token = sharedPref.getString(getString(R.string.token_key), "no token");

        //
        Call<OffreList> call = apiInterface.doGetOffres("Bearer " + token);
        call.enqueue(new Callback<OffreList>() {
            @Override
            public void onResponse(Call<OffreList> call, Response<OffreList> response) {

                if (response.isSuccessful()) {
                    String message = "nb offres = " + response.body().offres.size();
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(LoginActivity.this, "token not correct", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<OffreList> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                call.cancel();
                Log.e("TAG",t.getMessage());

            }
        });
    }
}