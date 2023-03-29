package fr.iut2.saeprojet;

import static fr.iut2.saeprojet.CandidatureActivity.CANDIDATURE_KEY;

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
import java.util.ArrayList;
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
import fr.iut2.saeprojet.entity.OffreConsultee;
import fr.iut2.saeprojet.entity.OffreConsulteeRequest;
import fr.iut2.saeprojet.entity.OffreRetenue;
import fr.iut2.saeprojet.entity.OffreRetenueRequest;
import fr.iut2.saeprojet.entity.OffresConsulteesResponse;
import fr.iut2.saeprojet.entity.OffresRetenuesResponse;

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
                    if (!estDeveloppe) {
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
        //Marque éventuellement l'offre comme consultee si pas déjà fait
        getOffresConsultees();

        candidater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creerCandidature();
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

    private void getOffresConsultees() {
        APIClient.getOffresConsultees(this, new ResultatAppel<OffresConsulteesResponse>() {
            @Override
            public void traiterResultat(OffresConsulteesResponse response) {
                boolean offrePasConsultee = true;
                for (OffreConsultee offreConsultee :
                        response.offresConsultees) {
                    //Si l'offre est déjà consultée on fait rien, sinon on l'a marque
                    if (offreConsultee.offre.equals(offre._id)) {
                        offrePasConsultee = false;
                        break;
                    }
                }
                if (offrePasConsultee) {
                    marquageConsultation();
                }
            }

            @Override
            public void traiterErreur() {

            }
        });
    }

    private void marquageConsultation() {
        OffreConsulteeRequest offreConsulteeRequest = new OffreConsulteeRequest();
        offreConsulteeRequest.offre = offre._id;
        offreConsulteeRequest.compteEtudiant = getCompte_Id();

        APIClient.createOffreConsultee(this, offreConsulteeRequest, new ResultatAppel<OffreConsultee>() {
            @Override
            public void traiterResultat(OffreConsultee response) {

            }

            @Override
            public void traiterErreur() {

            }
        });
    }

    private void refreshOffre() {
        intituleOffre.setText(offre.intitule);
        if (offre.urlPieceJointe != null) {
            url.setText("Lien vers l'offre");
        } else {
            url.setText("Pas de descriptif");
        }

        url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(offre.urlPieceJointe));
                startActivity(intent);
            }
        });

        // Statut de l'offre
        statutOffre.setText(statutOffre.getText() + getEnumValue(offre.etatOffre));

        // Lien vers le PDF
        refreshEntreprise(offre.entreprise);

    }

    private void refreshEntreprise(String entreprise_id) {
        APIClient.getEntreprises(this, new ResultatAppel<EntreprisesResponse>() {
            @Override
            public void traiterResultat(EntreprisesResponse liste) {
                for (Entreprise e : liste.entreprises) {
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
    private void creerCandidature() {
        CandidatureRequest candidatureReq = new CandidatureRequest();
        candidatureReq.compteEtudiant = getCompte_Id();
        candidatureReq.offre = offre._id;
        candidatureReq.dateAction = String.format("%1$tY-%1$tm-%1$tdT%1$tH:%1$tM:00.000Z", Calendar.getInstance().getTime());
        candidatureReq.typeAction = "Candidature à confirmer par envoi CV+lettre";
        candidatureReq.etatCandidature = EtatCandidatureEnum.OFFRE_RETENUE.get_id();
        APIClient.createCandidature(this, candidatureReq, new ResultatAppel<Candidature>() {
            @Override
            public void traiterResultat(Candidature candid) {
                getOffresRetenues();

                //On redirige l'utilisateur sur sa candidature nouvellement créée
                Intent intent = new Intent(OffreActivity.this, CandidatureActivity.class);
                intent.putExtra(OFFRE_KEY, offre);
                intent.putExtra(CANDIDATURE_KEY, candid);
                startActivity(intent);
            }
            @Override
            public void traiterErreur() {

            }
        });
    }
    private void getOffresRetenues() {
        APIClient.getOffresRetenues(this, new ResultatAppel<OffresRetenuesResponse>() {
            @Override
            public void traiterResultat(OffresRetenuesResponse response) {
                boolean offrePasRetenue = true;
                for (OffreRetenue offreRetenue :
                        response.offresRetenues) {
                    //Si l'offre est déjà retenue on fait rien, sinon on l'a marque
                    if (offreRetenue.offre.equals(offre._id)) {
                        offrePasRetenue = false;
                        break;
                    }
                }
                if (offrePasRetenue) {
                    marquageRetenue();
                }
            }

            @Override
            public void traiterErreur() {

            }
        });
    }

    private void marquageRetenue () {
        OffreRetenueRequest offreRetenueRequest = new OffreRetenueRequest();
        offreRetenueRequest.offre = offre._id;
        offreRetenueRequest.compteEtudiant = getCompte_Id();

        APIClient.createOffreRetenue(this, offreRetenueRequest, new ResultatAppel<OffreRetenue>() {
            @Override
            public void traiterResultat(OffreRetenue response) {
                System.out.println(response.offre);
            }

            @Override
            public void traiterErreur() {

            }
        });
    }
}