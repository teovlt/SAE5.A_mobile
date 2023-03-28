package fr.iut2.saeprojet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import fr.iut2.saeprojet.api.APIClient;
import fr.iut2.saeprojet.api.APIService;
import fr.iut2.saeprojet.api.EtatCandidatureEnum;
import fr.iut2.saeprojet.api.ResultatAppel;
import fr.iut2.saeprojet.entity.Candidature;
import fr.iut2.saeprojet.entity.CandidatureRequest;
import fr.iut2.saeprojet.entity.CompteEtudiant;
import fr.iut2.saeprojet.entity.Entreprise;
import fr.iut2.saeprojet.entity.EntreprisesResponse;
import fr.iut2.saeprojet.entity.Offre;

public class OffreActivity extends StageAppActivity {
    public static final String OFFRE_KEY = "offre_key";
    private ImageButton retour;
    private Button candidater;
    private TextView intituleOffre;

    private ImageButton developArrow;
    private TextView statutOffre;
    private TextView nomEntreprise;
    private TextView nomVille;
    private TextView url;

    private long offre_id = -1;
    private Offre offre = null;
    private Entreprise entreprise = null;
    private Candidature candidature = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offre);

        retour = findViewById(R.id.retourDeOffresAListeOffre);
        candidater = findViewById(R.id.buttonCandidature);
        intituleOffre = findViewById(R.id.titreOffre);
        statutOffre = findViewById(R.id.statutOffre);
        nomEntreprise = findViewById(R.id.nomEntreprise);
        nomVille = findViewById(R.id.nomVille);
        url = findViewById(R.id.offreUrl);
        developArrow = findViewById(R.id.developArrow);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            offre = getIntent().getParcelableExtra(OFFRE_KEY);
            refreshOffre();
        }

        intituleOffre.setText(offre.intitule);
        if (intituleOffre.length() >= 50) {
            String text = intituleOffre.getText().toString().substring(0, 50) + " ...";
            developArrow.setImageResource(R.drawable.arrow_down_24);
            intituleOffre.setText(text);

            View.OnClickListener onClick = new View.OnClickListener() {
                boolean estDeveloppe = false;

                @Override
                public void onClick(View view) {
                    if (!estDeveloppe){
                        intituleOffre.setText(offre.intitule);
                        estDeveloppe = true;
                        developArrow.setImageResource(R.drawable.arrow_up_24);
                    } else {
                        intituleOffre.setText(text);
                        estDeveloppe = false;
                        developArrow.setImageResource(R.drawable.arrow_down_24);
                    }
                }
            };

            intituleOffre.setOnClickListener(onClick);
            developArrow.setOnClickListener(onClick);
        }


        candidater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (candidature != null) {
                    executerCandidatureActivity();
                } else {
                    creerCandidature();
                }
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
            url.setText("Lien vers l'offre");
            //url.setText(offre.urlPieceJointe);
        } else {
            url.setText("Pas de descriptif");
        }

        url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(offre.urlPieceJointe));
                PackageManager packageManager = getPackageManager();
                startActivity(intent);
            }
        });

        // Statut de l'offre
        statutOffre.setText(statutOffre.getText() + getEnumValue(offre.etatOffre));

        // Lien vers le PDF
        refreshEntreprise(offre.entreprise);

        // Bouton candidature
        if (offre.etatOffre.equals("/api/etat_offres/1")) {
            if (offre.candidatures.size() > 0) {
                checkCandidature(offre.candidatures);
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

    private void checkCandidature(List<String> candidatures) {
        APIClient.getCompteEtudiant(this, getCompteId(), new ResultatAppel<CompteEtudiant>() {
            @Override
            public void traiterResultat(CompteEtudiant compte) {
                for(String id : compte.candidatures) {
                    if (candidatures.contains(id)) {
                        refreshCandidature(id);
                        break;
                    }
                }
            }

            @Override
            public void traiterErreur() {

            }
        });
    }

    private void refreshCandidature(String _id) {
        APIClient.getCandidature(this, APIClient.getCandidatureId(_id), new ResultatAppel<Candidature>() {
            @Override
            public void traiterResultat(Candidature candid) {
                candidature = candid;
                if (candid != null) {
                    candidater.setText("Voir la candidature");
                }
            }

            @Override
            public void traiterErreur() {

            }
        });
    }

    private void creerCandidature() {
        CandidatureRequest candidatureReq = new CandidatureRequest();
        candidatureReq.compteEtudiant = getCompte_Id();
        candidatureReq.offre = offre._id;
        candidatureReq.dateAction = String.format("%1$tY-%1$tm-%1$tdT%1$tH:%1$tM:00.000Z",Calendar.getInstance().getTime());
        candidatureReq.typeAction = "Candidature à confirmer par envoi CV+lettre";
        candidatureReq.etatCandidature = EtatCandidatureEnum.OFFRE_RETENUE.get_id();
        APIClient.createCandidature(this, candidatureReq, new ResultatAppel<Candidature>() {
            @Override
            public void traiterResultat(Candidature candid) {
                candidature = candid;
                executerCandidatureActivity();
            }

            @Override
            public void traiterErreur() {

            }
        });
    }

    private void executerCandidatureActivity() {
        Intent intent = new Intent(OffreActivity.this, CandidatureActivity.class);

        intent.putExtra("offre", offre);
        intent.putExtra("candidature_key", candidature);
        startActivity(intent);
    }
}