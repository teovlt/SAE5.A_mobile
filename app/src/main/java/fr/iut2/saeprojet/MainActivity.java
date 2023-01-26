package fr.iut2.saeprojet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import fr.iut2.saeprojet.api.APIClient;
import fr.iut2.saeprojet.api.APIService;
import fr.iut2.saeprojet.entity.Offre;
import fr.iut2.saeprojet.entity.OffreList;
import fr.iut2.saeprojet.exempleNOTUSE.api.APIUserClient;
import fr.iut2.saeprojet.exempleNOTUSE.api.APIUserService;
import fr.iut2.saeprojet.exempleNOTUSE.entity.UserList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    // API
    private APIService apiInterface;

    // View
    private LinearLayout offresView;
    private LinearLayout candidaturesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Chargement de l'API
        apiInterface = APIClient.getAPIService();

        // init View
        offresView = findViewById(R.id.offres);
        candidaturesView = findViewById(R.id.candidatures);

        //
        offresView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListOffresActivity.class);
                startActivity(intent);
            }
        });

        //
        candidaturesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListOffresActivity.class);
                startActivity(intent);
            }
        });

        //
        refreshLogin();
        refreshOffres();
    }

    private void refreshLogin() {

        //
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String login = sharedPref.getString(getString(R.string.login_key), "no login");

        //
        TextView loginView = findViewById(R.id.login);
        loginView.setText(login);
    }

    private void refreshOffres() {

        //
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String token = sharedPref.getString(getString(R.string.token_key), "no token");

        //
        Call<OffreList> call = apiInterface.doGetOffres("Bearer " + token);
        call.enqueue(new Callback<OffreList>() {
            @Override
            public void onResponse(Call<OffreList> call, Response<OffreList> response) {

                //
                TextView nbOffresView = findViewById(R.id.textView8);
                OffreList offres = response.body();
                nbOffresView.setText(String.valueOf(offres.offres.size()) + " " + nbOffresView.getText().toString());
            }

            @Override
            public void onFailure(Call<OffreList> call, Throwable t) {
                call.cancel();
                Log.e("TAG",t.getMessage());

            }
        });
    }
}