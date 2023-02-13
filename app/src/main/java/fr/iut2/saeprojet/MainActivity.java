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
private TextView mesCandidaturesView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.title_activity_main);
        setContentView(R.layout.activity_main);
        //View init
        Button details_offres = findViewById(R.id.details_offres);
        Button details_candidatures = findViewById(R.id.details_candidatures);
        //Rend le bouton voir en details des offres cliquable et redirige l'utilisateur sur la liste des offres
        details_offres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListOffresActivity.class);
                startActivity(intent);
            }
        });
        //Rend le bouton voir en details des candidatures cliquable et redirige l'utilisateur sur la liste de ses candidatures
        details_candidatures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListCandidaturesActivity.class);
                startActivity(intent);
            }
        });
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
                TextView nbOffresView = findViewById(R.id.offres_titre);
                nbOffresView.setText(String.valueOf(offres.offres.size()));
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
                TextView mesOffresConsulteesView = findViewById(R.id.offres_consultees);
                TextView mesOffresRetenuesView = findViewById(R.id.offres_retenues);
                mesCandidaturesView = findViewById(R.id.candidatures_titre);
                TextView derniereConnexionView = findViewById(R.id.derniere_connexion_view);
                derniereConnexionView.setText(String.valueOf(compteEtudiant.derniereConnexion));
                mesOffresConsulteesView.setText(String.valueOf(compteEtudiant.offreConsultees.size()));
                mesOffresRetenuesView.setText(String.valueOf(compteEtudiant.offreRetenues.size()));
                mesCandidaturesView.setText(String.valueOf(compteEtudiant.candidatures.size()));

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
                TextView nbCandidaturesRefuseesView = findViewById(R.id.candidatures_refusees);
                TextView nbCandidaturesEnCoursView = findViewById(R.id.candidatures_en_cours);
                int count = 0;
                for(Candidature c : candidatures.candidatures) {
                    System.out.println(c.etatCandidature);
                    if (c.etatCandidature.equals("/api/etat_candidatures/3")) {
                        count ++;
                    }
                }
                nbCandidaturesRefuseesView.setText(String.valueOf(count));
               nbCandidaturesEnCoursView.setText(String.valueOf(Integer.parseInt((String) mesCandidaturesView.getText()) - count));
            }

            @Override
            public void traiterErreur() {

            }
        });
    }
}