package fr.iut2.saeprojet;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static fr.iut2.saeprojet.OffreActivity.OFFRE_KEY;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import fr.iut2.saeprojet.api.APIClient;
import fr.iut2.saeprojet.api.ResultatAppel;
import fr.iut2.saeprojet.entity.Candidature;
import fr.iut2.saeprojet.entity.EtatsCandidatures;
import fr.iut2.saeprojet.entity.Offre;
import fr.iut2.saeprojet.entity.OffreRetenue;
import fr.iut2.saeprojet.entity.OffresRetenuesResponse;

public class CandidatureActivity extends StageAppActivity {

    //
    public static final String CANDIDATURE_KEY = "candidature_key";

    // Data
    private Candidature candidature;
    private Offre offre;

    // View
    private Button abandonView;
    private ImageView retourCandidaturesView;
    private TextView intituleView;

    private ImageView developArrow;
    private TextView etatView;
    private TextView dateActionView;
    private Button mettreAJourView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidature);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // Data
        candidature = getIntent().getParcelableExtra(CANDIDATURE_KEY);

        // Init view
        retourCandidaturesView = findViewById(R.id.retourCandidatures);
        intituleView = findViewById(R.id.intitule);
        developArrow = findViewById(R.id.developArrow);
        etatView = findViewById(R.id.etat);
        dateActionView = findViewById(R.id.dateAction);
        mettreAJourView = findViewById(R.id.mettreajour);
        abandonView = findViewById(R.id.abandonCandidature);

        alertDialogBuilder.setTitle("Supprimer candidature");
        alertDialogBuilder.setMessage("Voulez vous supprimer cette candidature ?");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                deleteCandidature();
                Toast.makeText(CandidatureActivity.this, "Candidature supprimée", Toast.LENGTH_SHORT).show();
                finish();

            }
        });

        alertDialogBuilder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(CandidatureActivity.this, "Candidature non supprimée", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();

        //
        retourCandidaturesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CandidatureActivity.this, ListCandidaturesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        //
        mettreAJourView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CandidatureActivity.this, CandidatureEditActivity.class);
                intent.putExtra(OFFRE_KEY, offre);
                intent.putExtra(CANDIDATURE_KEY, candidature);
                startActivity(intent);
            }
        });

        //
        refreshMesInformations(candidature, intituleView);


        abandonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });
    }


    private void refreshMesInformations(Candidature candidature, TextView intituleView) {
        //
        etatView.setText(EtatsCandidatures.etatsCandidatureInverse.get(candidature.getEtatCandidatureId()));
        dateActionView.setText(candidature.dateAction);

        APIClient.getOffre(this, candidature.getOffreId(), new ResultatAppel<Offre>() {
            @Override
            public void traiterResultat(Offre resoffre) {
                offre = resoffre;
                intituleView.setText(offre.intitule);

                intituleView.setText(offre.intitule);
                if (intituleView.length() >= 50) {
                    developArrow.setImageResource(R.drawable.arrow_down_24);
                    intituleView.setText(intituleView.getText().toString().substring(0, 50) + " ...");

                    View.OnClickListener onClick = new View.OnClickListener() {
                        boolean estDeveloppe = false;

                        @Override
                        public void onClick(View view) {
                            if (!estDeveloppe) {
                                intituleView.setText(offre.intitule);
                                estDeveloppe = true;
                                developArrow.setImageResource(R.drawable.arrow_up_24);
                            } else {
                                intituleView.setText(intituleView.getText().toString().substring(0, 50) + " ...");
                                estDeveloppe = false;
                                developArrow.setImageResource(R.drawable.arrow_down_24);
                            }
                        }
                    };

                    intituleView.setOnClickListener(onClick);
                    developArrow.setOnClickListener(onClick);
                }
            }

            @Override
            public void traiterErreur() {

            }
        });
    }


    private void deleteCandidature() {
        APIClient.removeCandidature(this, APIClient.getCandidatureId(candidature._id), new ResultatAppel<Candidature>() {
            @Override
            public void traiterResultat(Candidature response) {
                getOffresRetenues();
                Intent intent = new Intent(CandidatureActivity.this, ListCandidaturesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
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
                boolean offreASupprimer = false;
                long offer = -1;
                for (OffreRetenue offreRetenue : response.offresRetenues) {
                    if (offreRetenue.offre.equals(offre._id)) {
                        //On supprime l'offre des offre retenues
                        offreASupprimer = true;
                        offer = offreRetenue.id;

                    }
                }
                if (offreASupprimer) {
                    deleteOffreRetenue(offer);
                }
            }

            @Override
            public void traiterErreur() {

            }
        });
    }

    private void deleteOffreRetenue(long id) {
        APIClient.removeOffreRetenue(this, id, new ResultatAppel<OffreRetenue>() {
            @Override
            public void traiterResultat(OffreRetenue response) {
            }

            @Override
            public void traiterErreur() {

            }
        });
    }
}