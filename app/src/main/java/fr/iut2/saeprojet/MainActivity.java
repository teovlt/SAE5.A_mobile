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

import androidx.appcompat.app.AppCompatActivity;

import fr.iut2.saeprojet.api.APIClient;
import fr.iut2.saeprojet.api.APIService;
import fr.iut2.saeprojet.entity.CandidaturesResponse;
import fr.iut2.saeprojet.entity.CompteEtudiant;
import fr.iut2.saeprojet.entity.OffresResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    // API
    private APIService apiInterface;

    // View
    private LinearLayout offresView;
    private LinearLayout candidaturesView;

    private LinearLayout offreView;

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
        refreshLogin();
        refreshNBOffres();
        refreshCandidatures();
    }

    private void refreshLogin() {

        //
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String login = sharedPref.getString(getString(R.string.login_key), "no login");

        //
        TextView loginView = findViewById(R.id.login);
        loginView.setText(login);

        //
        refreshMesInformations();
    }

    private void refreshNBOffres() {

        //
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String token = sharedPref.getString(getString(R.string.token_key), "no token");

        //
        Call<OffresResponse> call = apiInterface.doGetOffres("Bearer " + token);
        call.enqueue(new Callback<OffresResponse>() {
            @Override
            public void onResponse(Call<OffresResponse> call, Response<OffresResponse> response) {

                //
                TextView nbOffresView = findViewById(R.id.textView8);
                OffresResponse offres = response.body();
                nbOffresView.setText(String.valueOf(offres.offres.size()) + " " + nbOffresView.getText().toString());
            }

            @Override
            public void onFailure(Call<OffresResponse> call, Throwable t) {
                call.cancel();
                Log.e("TAG",t.getMessage());

            }
        });
    }

    private void refreshMesInformations() {

        //
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String token = sharedPref.getString(getString(R.string.token_key), "no token");
        long id = sharedPref.getLong(getString(R.string.login_id_key), 0);

        //
        Call<CompteEtudiant> call = apiInterface.doGetCompteEtudiant("Bearer " + token, id);
        call.enqueue(new Callback<CompteEtudiant>() {
            @Override
            public void onResponse(Call<CompteEtudiant> call, Response<CompteEtudiant> response) {

                //
                TextView mesOffresConsulteesView = findViewById(R.id.textView10);
                TextView mesOffresRetenuesView = findViewById(R.id.textView11);
                TextView mesCandidaturesView = findViewById(R.id.textView13);
                TextView derniereConnexionView = findViewById(R.id.textView6);
                CompteEtudiant compteEtudiant = response.body();
                mesOffresConsulteesView.setText(String.valueOf(compteEtudiant.offreConsultees.size()) + " " + mesOffresConsulteesView.getText().toString());
                mesOffresRetenuesView.setText(String.valueOf(compteEtudiant.offreRetenues.size()) + " " + mesOffresRetenuesView.getText().toString());
                mesCandidaturesView.setText(String.valueOf(compteEtudiant.candidatures.size()) + " " + mesCandidaturesView.getText().toString());
                derniereConnexionView.setText(compteEtudiant.derniereConnexion);
            }

            @Override
            public void onFailure(Call<CompteEtudiant> call, Throwable t) {
                call.cancel();
                Log.e("TAG",t.getMessage());
            }
        });
    }

    private void refreshCandidatures() {

        //
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String token = sharedPref.getString(getString(R.string.token_key), "no token");

        //
        Call<CandidaturesResponse> call = apiInterface.doGetCandidatures("Bearer " + token);
        call.enqueue(new Callback<CandidaturesResponse>() {
            @Override
            public void onResponse(Call<CandidaturesResponse> call, Response<CandidaturesResponse> response) {

                //
                TextView nbCandidaturesView = findViewById(R.id.textView12);
                CandidaturesResponse candidatures = response.body();
                nbCandidaturesView.setText(String.valueOf(candidatures.candidatures.size()) + " " + nbCandidaturesView.getText().toString());
            }

            @Override
            public void onFailure(Call<CandidaturesResponse> call, Throwable t) {
                call.cancel();
                Log.e("TAG",t.getMessage());
            }
        });
    }
}