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
import fr.iut2.saeprojet.api.ResultatAppel;
import fr.iut2.saeprojet.entity.Candidature;
import fr.iut2.saeprojet.entity.CandidaturesResponse;
import fr.iut2.saeprojet.entity.CompteEtudiant;
import fr.iut2.saeprojet.entity.OffresResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends StageAppActivity {

    // View
    private LinearLayout offresView;
    private LinearLayout candidaturesView;

    private LinearLayout offreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        TextView loginView = findViewById(R.id.login);
        loginView.setText(getLogin());
        refreshMesInformations();
    }

    private void refreshNBOffres() {
        APIClient.getOffres(this, new ResultatAppel<OffresResponse>() {

            @Override
            public void traiterResultat(OffresResponse offres) {
                TextView nbOffresView = findViewById(R.id.textView8);
                nbOffresView.setText(String.valueOf(offres.offres.size()) + " " + nbOffresView.getText().toString());
            }

            @Override
            public void traiterErreur() {
            }
        });
    }

    private void refreshMesInformations() {
        APIClient.getCompteEtudiant(this, getCompteId(), new ResultatAppel<CompteEtudiant>() {
            @Override
            public void traiterResultat(CompteEtudiant compteEtudiant) {
                TextView mesOffresConsulteesView = findViewById(R.id.textView10);
                TextView mesOffresRetenuesView = findViewById(R.id.textView11);
                TextView mesCandidaturesView = findViewById(R.id.textView12);
                TextView derniereConnexionView = findViewById(R.id.textView6);
                mesOffresConsulteesView.setText(String.valueOf(compteEtudiant.offreConsultees.size()) + " " + mesOffresConsulteesView.getText().toString());
                mesOffresRetenuesView.setText(String.valueOf(compteEtudiant.offreRetenues.size()) + " " + mesOffresRetenuesView.getText().toString());
                mesCandidaturesView.setText(String.valueOf(compteEtudiant.candidatures.size()) + " " + mesCandidaturesView.getText().toString());
                derniereConnexionView.setText(compteEtudiant.derniereConnexion);
            }

            @Override
            public void traiterErreur() {
            }
        });
    }

    private void refreshCandidatures() {
        APIClient.getCandidatures(this, new ResultatAppel<CandidaturesResponse>() {
            @Override
            public void traiterResultat(CandidaturesResponse candidatures) {
                TextView nbCandidaturesView = findViewById(R.id.textView13);
                TextView nbCandidaturesRefuseesView = findViewById(R.id.textView14);

                int count = 0;
                for(Candidature c : candidatures.candidatures) {
                    if (c.etatCandidature.equals("/api/etat_candidature/3")) {
                        count ++;
                    }
                }
                nbCandidaturesRefuseesView.setText(String.valueOf(count) + " " + nbCandidaturesRefuseesView.getText().toString());
                nbCandidaturesView.setText(String.valueOf(candidatures.candidatures.size() - count) + " " + nbCandidaturesView.getText().toString());
            }

            @Override
            public void traiterErreur() {

            }
        });
    }
}