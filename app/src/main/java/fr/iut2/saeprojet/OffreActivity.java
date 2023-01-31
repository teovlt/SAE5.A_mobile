package fr.iut2.saeprojet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import fr.iut2.saeprojet.api.APIClient;
import fr.iut2.saeprojet.api.APIService;
import fr.iut2.saeprojet.api.ResultatAppel;
import fr.iut2.saeprojet.entity.Candidature;
import fr.iut2.saeprojet.entity.CompteEtudiant;
import fr.iut2.saeprojet.entity.Entreprise;
import fr.iut2.saeprojet.entity.EntreprisesResponse;
import fr.iut2.saeprojet.entity.Offre;

public class OffreActivity extends StageAppActivity {
    private TextView retour;
    private Button candidater;
    private TextView intituleOffre;
    private TextView statutOffre;
    private TextView nomEntreprise;
    private TextView nomVille;
    private TextView url;

    private long offre_id = -1;
    private Offre offre = null;
    private Entreprise entreprise = null;
    private String candidature_id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offre);

        retour = findViewById(R.id.backOffres);
        candidater = findViewById(R.id.buttonCandidature);
        intituleOffre = findViewById(R.id.titreOffre);
        statutOffre = findViewById(R.id.statutOffre);
        nomEntreprise = findViewById(R.id.nomEntreprise);
        nomVille = findViewById(R.id.nomVille);
        url = findViewById(R.id.offreUrl);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            offre = getIntent().getParcelableExtra("offre");
            candidature_id = extras.getString("candidature_id");
            refreshOffre();
        }

        candidater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executerCandidatureActivity();
            }
        });

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OffreActivity.this, ListOffresActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void refreshOffre() {
        intituleOffre.setText(offre.intitule);
        if (offre.urlPieceJointe != null) {
            url.setText(offre.urlPieceJointe);
        } else {
            url.setText("Pas de descriptif");
        }

        // Statut de l'offre
        statutOffre.setText(statutOffre.getText() + getEnumValue(offre.etatOffre));

        // Lien vers le PDF
        refreshEntreprise(offre.entreprise);

        // Bouton candidature
        if (offre.etatOffre.equals("/api/etat_offres/1")) {
            if (offre.candidatures.size() > 0) {
                refreshCandidature(offre.candidatures);
            }
        } else {
            candidater.setEnabled(false);
        }
    }

    private void refreshEntreprise(String entreprise_id) {
        APIClient.getEntreprises(this, new ResultatAppel<EntreprisesResponse>() {
            @Override
            public void traiterResultat(EntreprisesResponse liste) {
                for(Entreprise e : liste.entreprises) {
                    if (e._id.equals(entreprise_id)) {
                        OffreActivity.this.entreprise = e;
                        nomEntreprise.setText(entreprise.raisonSociale);
                        nomVille.setText(entreprise.ville);
                    }
                }
            }

            @Override
            public void traiterErreur() {

            }
        });
    }

    private void refreshCandidature(List<String> candidatures) {
        APIClient.getCompteEtudiant(this, getCompteId(), new ResultatAppel<CompteEtudiant>() {
            @Override
            public void traiterResultat(CompteEtudiant compte) {
                for(String id : compte.candidatures) {
                    if (candidatures.contains(id)) {
                        candidater.setText("Voir Candidature");
                        candidature_id = id;
                        break;
                    }
                }
            }

            @Override
            public void traiterErreur() {

            }
        });
    }

    private void executerCandidatureActivity() {
        Intent intent = new Intent(OffreActivity.this, CandidatureActivity.class);

        intent.putExtra("offre", offre);

        if (candidature_id != null) {
            intent.putExtra("candidature_id", candidature_id);
        }

        startActivity(intent);
    }
}