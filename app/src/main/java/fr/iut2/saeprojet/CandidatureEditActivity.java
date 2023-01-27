package fr.iut2.saeprojet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.iut2.saeprojet.api.APIClient;
import fr.iut2.saeprojet.api.APIService;
import fr.iut2.saeprojet.entity.Candidature;
import fr.iut2.saeprojet.entity.EtatsCandidatures;
import fr.iut2.saeprojet.entity.Offre;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CandidatureEditActivity extends AppCompatActivity {

    //
    public static final String CANDIDADURE_KEY = "candidature_key";

    // Data
    private Candidature candidature;
    private ArrayAdapter<String> adapter;

    // API
    private APIService apiInterface;

    // View
    private TextView retourCandidaturesView;
    private TextView intituleView;
    private Button annulerView;
    private Button validerView;
    private Spinner etatsCandidatureView;
    private EditText dateActionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidature_edit);

        // Data
        candidature = getIntent().getParcelableExtra(CANDIDADURE_KEY);


        // Chargement de l'API
        apiInterface = APIClient.getAPIService();

        // Init view
        retourCandidaturesView = findViewById(R.id.retourCandidatures);
        intituleView = findViewById(R.id.intitule);
        annulerView = findViewById(R.id.annuler);
        validerView = findViewById(R.id.valider);
        etatsCandidatureView = findViewById(R.id.etatsCandidature);
        dateActionView = findViewById(R.id.dateAction);

        //
        retourCandidaturesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //
        annulerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //
        validerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //
                updateCandidature();

            }
        });

        // Create an ArrayAdapter using the string array and a default spinner layout
        List<String> etats = new ArrayList<>(EtatsCandidatures.etatsCandidature.keySet());
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, etats);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        etatsCandidatureView.setAdapter(adapter);

        //
        refreshMesInformations(intituleView);
    }

    private void refreshMesInformations(TextView intituleView) {

        //
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String token = sharedPref.getString(getApplicationContext().getString(R.string.token_key), "no token");

        //
        Call<Offre> call = apiInterface.doGetOffre("Bearer " + token, candidature.getOffreId());
        call.enqueue(new Callback<Offre>() {
            @Override
            public void onResponse(Call<Offre> call, Response<Offre> response) {
                String intitule = response.body().intitule;
                intituleView.setText(intitule);
                String etat = EtatsCandidatures.etatsCandidatureInverse.get(candidature.getEtatCandidatureId());
                int spinnerPosition = adapter.getPosition(etat);
                etatsCandidatureView.setSelection(spinnerPosition);
                dateActionView.setText(candidature.dateAction);
            }

            @Override
            public void onFailure(Call<Offre> call, Throwable t) {
                call.cancel();
                Log.e("TAG",t.getMessage());

            }
        });
    }


    private void updateCandidature() {

        //
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String token = sharedPref.getString(getApplicationContext().getString(R.string.token_key), "no token");

        String etat = etatsCandidatureView.getSelectedItem().toString();
        long idEtat = EtatsCandidatures.etatsCandidature.get(etat);
        candidature.setEtatCandidatureId(idEtat);
        candidature.setDateAction(dateActionView.getText().toString());

        //
        Call<Candidature> call = apiInterface.doUpdateCandidature("Bearer " + token,"application/json", candidature.id, candidature);
        call.enqueue(new Callback<Candidature>() {
            @Override
            public void onResponse(Call<Candidature> call, Response<Candidature> response) {

                //
                Intent intent = new Intent(CandidatureEditActivity.this, ListCandidaturesActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Candidature> call, Throwable t) {
                call.cancel();
                Log.e("TAG",t.getMessage());

            }
        });
    }
}