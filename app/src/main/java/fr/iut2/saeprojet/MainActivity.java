package fr.iut2.saeprojet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;

import fr.iut2.saeprojet.api.APIClient;
import fr.iut2.saeprojet.api.ResultatAppel;
import fr.iut2.saeprojet.entity.Candidature;
import fr.iut2.saeprojet.entity.CandidaturesResponse;
import fr.iut2.saeprojet.entity.CompteEtudiant;
import fr.iut2.saeprojet.entity.Etudiant;
import fr.iut2.saeprojet.entity.OffresResponse;

public class MainActivity extends StageAppActivity {
private TextView prenomView;
private TextView derniereConnexionView;

//OFFRES VIEWS
private Button details_offres;
private TextView offresView;
private TextView offresConsulteesView;
private TextView offresRetenuesView;
//CANDIDATURES VIEWS
private Button details_candidatures;
private TextView candidaturesView;
private TextView candidaturesRefuseesView;
private TextView candidaturesEnCoursView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.title_activity_main);
        setContentView(R.layout.activity_main);
        //Views init
        prenomView = findViewById(R.id.prenom);
        derniereConnexionView = findViewById(R.id.derniere_connexion);
        //Offres views
        offresView = findViewById(R.id.offres);
        offresConsulteesView = findViewById(R.id.offres_consultees);
        offresRetenuesView = findViewById(R.id.offres_retenues);
        details_offres = findViewById(R.id.details_offres);
        //Candidatures views
        candidaturesView = findViewById(R.id.candidatures);
        candidaturesRefuseesView = findViewById(R.id.candidatures_refusees);
        candidaturesEnCoursView = findViewById(R.id.candidatures_en_cours);
        details_candidatures = findViewById(R.id.details_candidatures);


        //Insère les informations nécessaires dans la page via divers appels API
        refreshInformations();

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
    }

    private void refreshInformations() {
        APIClient.getCompteEtudiant(this, getCompteId(), new ResultatAppel<CompteEtudiant>() {
            @Override
            public void traiterResultat(CompteEtudiant compteEtudiant) {
                offresConsulteesView.setText(getResources().getString(R.string.offres_consultees,compteEtudiant.offreConsultees.size()));
                offresRetenuesView.setText(getResources().getString(R.string.offres_retenues,compteEtudiant.offreRetenues.size()));
                candidaturesView.setText(getResources().getString(R.string.candidatures,compteEtudiant.candidatures.size()));
                refreshDerniereConnexion(compteEtudiant);
                refreshPrenom(compteEtudiant);
            }

            @Override
            public void traiterErreur() {
            }
        });
        refreshOffres();
        refreshCandidatures();
    }
private void refreshDerniereConnexion(CompteEtudiant compteEtudiant){
    Bundle extras = getIntent().getExtras();
    //Si l'utilisateur vient de la page login on récupère la derniere connexion de là bas
    if (extras != null ) {
        String extraDerniereConnexion =getIntent().getStringExtra("derniere_connexion");
        //si c'est la premiere connexion mettre la date à now
        if(extraDerniereConnexion.equals("")){
            extraDerniereConnexion = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(Calendar.getInstance().getTime());
        }
        setCompteDerniereConnexion(extraDerniereConnexion,2, compteEtudiant._id);
        derniereConnexionView.setText(getResources().getString(R.string.derniere_connexion,extraDerniereConnexion));
    }else {
        //Sinon l'utilisateur vient des activités suivantes, on récupère la derniere connexion dans un shared preferences
        derniereConnexionView.setText(getResources().getString(R.string.derniere_connexion,getDerniereConnexion(2, compteEtudiant._id)));
    }
}
private void refreshPrenom(CompteEtudiant compteEtudiant){
    APIClient.getEtudiant(this, Long.parseLong(compteEtudiant.etudiant.substring(compteEtudiant.etudiant.length() - 1)), new ResultatAppel<Etudiant>() {
        @Override
        public void traiterResultat(Etudiant response) {
            prenomView.setText(getResources().getString(R.string.prenom,response.prenom));
        }

        @Override
        public void traiterErreur() {

        }
    });

}
    private void refreshOffres() {
        APIClient.getOffres(this, new ResultatAppel<OffresResponse>() {

            @Override
            public void traiterResultat(OffresResponse offres) {
                offresView.setText(getResources().getString(R.string.offres,offres.offres.size()));
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
                int count = 0;
                for(Candidature c : candidatures.candidatures) {
                    if (c.etatCandidature.equals("/api/etat_candidatures/3")) {
                        count ++;
                    }
                }
                candidaturesRefuseesView.setText(getResources().getString(R.string.candidatures_refusees,count));
                candidaturesEnCoursView.setText(getResources().getString(R.string.candidatures_en_cours,Integer.parseInt((String) candidaturesView.getText().subSequence(0,1)) - count));
            }

            @Override
            public void traiterErreur() {

            }
        });
    }
}