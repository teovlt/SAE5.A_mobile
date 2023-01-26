package fr.iut2.saeprojet;

import android.content.Context;
import android.content.Intent;
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

    // API
    private APIService apiInterface;

    // View
    private EditText usernameView;
    private EditText passwordView;
    private Button loginView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Chargement de l'API
        apiInterface = APIClient.getAPIService();

        // Init view
        usernameView = findViewById(R.id.username);
        passwordView = findViewById(R.id.password);
        loginView = findViewById(R.id.login);

        //
        loginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
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
                    String message = "JWT Token : " + response.body().token;
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();

                    //
                    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(getString(R.string.token_key), response.body().token);
                    editor.putString(getString(R.string.token_key), response.body().token);
                    editor.putString(getString(R.string.login_key), login);
                    editor.apply();

                    //
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(LoginActivity.this, "non pas correquete", Toast.LENGTH_SHORT).show();
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
}