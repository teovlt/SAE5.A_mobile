package fr.iut2.saeprojet;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static fr.iut2.saeprojet.CandidatureActivity.CANDIDATURE_KEY;
import static fr.iut2.saeprojet.OffreActivity.OFFRE_KEY;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import fr.iut2.saeprojet.api.APIClient;
import fr.iut2.saeprojet.api.EtatCandidatureEnum;
import fr.iut2.saeprojet.api.ResultatAppel;
import fr.iut2.saeprojet.entity.Candidature;
import fr.iut2.saeprojet.entity.CandidatureRequest;
import fr.iut2.saeprojet.entity.Offre;
import fr.iut2.saeprojet.entity.OffreRetenue;
import fr.iut2.saeprojet.entity.OffreRetenueRequest;
import fr.iut2.saeprojet.entity.OffresRetenuesResponse;

public class CandidatureEditActivity extends StageAppActivity {

    //
    // Data
    private Candidature candidature;
    private Offre offre;
    private ArrayAdapter<EtatCandidatureEnum> adapter;
    // View
    private ImageButton retourCandidaturesView;
    private Button validerView;
    private TextView intituleOffre;
    private ImageButton developArrow;
    private Spinner etatsCandidatureView;
    private DatePicker dateActionView;
    private int currentYear, currentMonth, currentDay;

    private String initialSpinnerValue, currentSpinnerValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidature_edit);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);


        // Data
        candidature = getIntent().getParcelableExtra(CANDIDATURE_KEY);
        offre = getIntent().getParcelableExtra(OFFRE_KEY);

        // Init view
        retourCandidaturesView = findViewById(R.id.retourCandidatures);
        validerView = findViewById(R.id.valider);
        etatsCandidatureView = findViewById(R.id.etatsCandidature);
        dateActionView = findViewById(R.id.datePicker);
        intituleOffre = findViewById(R.id.intitule);
        developArrow = findViewById(R.id.developArrow);



        //Initialisation du datePicker
        Calendar calendar = Calendar.getInstance();
        dateActionView.setMinDate(calendar.getTimeInMillis());
        int day = Integer.parseInt(candidature.dateAction.substring(8,10));
        int month = Integer.parseInt(candidature.dateAction.substring(5,7));
        int year = Integer.parseInt(candidature.dateAction.substring(0,4));

        dateActionView.init(year, month-1, day, null);
        alertDialogBuilder.setTitle("Retour à la candidature");
        alertDialogBuilder.setMessage("Voulez vous sauvegarder vos changements ?");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                updateCandidature();


            }
        });

        alertDialogBuilder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(CandidatureEditActivity.this, "Changements non enregistrés", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        alertDialogBuilder.setNeutralButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();

        //
        retourCandidaturesView.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                currentYear = dateActionView.getYear();
                currentMonth = dateActionView.getMonth();
                currentDay = dateActionView.getDayOfMonth();
                currentSpinnerValue = ((EtatCandidatureEnum) etatsCandidatureView.getSelectedItem()).toString();

                if (year == currentYear && month == currentMonth+1 && day == currentDay && initialSpinnerValue.equals(currentSpinnerValue)) {
                    finish();
                } else {
                    alertDialog.show();
                }

            }
        });

        //
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
        //
        validerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCandidature();
            }
        });


        // Create an ArrayAdapter using the string array and a default spinner layout
        List<EtatCandidatureEnum> etats = Arrays.asList(EtatCandidatureEnum.values());
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, etats);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        etatsCandidatureView.setAdapter(adapter);
        refreshMesInformations(intituleOffre);
        initialSpinnerValue = ((EtatCandidatureEnum)etatsCandidatureView.getSelectedItem()).toString();
    }

    private void refreshMesInformations(TextView intituleView) {
        APIClient.getOffre(this, candidature.getOffreId(), new ResultatAppel<Offre>() {
            @Override
            public void traiterResultat(Offre offre) {
                String intitule = offre.intitule;
                intituleView.setText(intitule);
                for (EtatCandidatureEnum e : EtatCandidatureEnum.values()) {
                    if (e.get_id().equals(candidature.etatCandidature)) {
                        etatsCandidatureView.setSelection(e.ordinal());
                        break;
                    }
                }

            }
        });
    }

    private void updateCandidature() {
        currentYear = dateActionView.getYear();
        currentMonth = dateActionView.getMonth();
        currentDay = dateActionView.getDayOfMonth();
        Date currentDate = new GregorianCalendar(currentYear, currentMonth, currentDay).getTime();


        CandidatureRequest candidatureReq = new CandidatureRequest();
        candidatureReq.compteEtudiant = getCompte_Id();
        candidatureReq.offre = candidature.offre;
        candidatureReq.dateAction = String.format("%1$tY-%1$tm-%1$tdT%1$tH:%1$tM:00.000Z", currentDate);
        candidatureReq.typeAction = "Candidature à confirmer par envoi CV+lettre";
        candidatureReq.etatCandidature = ((EtatCandidatureEnum) etatsCandidatureView.getSelectedItem()).get_id();

        APIClient.updateCandidature(this, APIClient.getCandidatureId(candidature._id), candidatureReq, new ResultatAppel<Candidature>() {
            @Override
            public void traiterResultat(Candidature response) {
                //Si l'état de l'offre mise à jour est retenue on l'ajoute éventuellement en base si elle n'y était pas déjà
                //Sinon on supprime éventuellement l'offre retenue
                getOffresRetenues(!response.etatCandidature.equals("/api/etat_candidatures/1"));
                Toast.makeText(CandidatureEditActivity.this, "Changements enregistrés", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CandidatureEditActivity.this, CandidatureActivity.class);
                intent.putExtra(CANDIDATURE_KEY, response);
                intent.putExtra(OFFRE_KEY, offre);
                intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }

        });
    }

    private void getOffresRetenues(boolean supprimer) {
        APIClient.getOffresRetenues(this, new ResultatAppel<OffresRetenuesResponse>() {
            @Override
            public void traiterResultat(OffresRetenuesResponse response) {
                boolean offrePasRetenue = true;
                boolean offreASupprimer = false;
                long offer = -1;
                for (OffreRetenue offreRetenue : response.offresRetenues) {
                    //Si l'offre est déjà retenue on fait rien, sinon on l'a marque
                    if (offreRetenue.offre.equals(offre._id) && supprimer) {
                        //On supprime l'offre des offre retenues
                        offreASupprimer = true;
                        offer = offreRetenue.id;

                    } else if (offreRetenue.offre.equals(offre._id)) {
                        offrePasRetenue = false;
                        break;
                    }
                }
                if (offreASupprimer) {
                    deleteOffreRetenue(offer);
                } else if (offrePasRetenue) {
                    marquageRetenue();
                }
            }

        });
    }

    private void deleteOffreRetenue(long id) {
        APIClient.removeOffreRetenue(this, id, new ResultatAppel<OffreRetenue>() {
            @Override
            public void traiterResultat(OffreRetenue response) {
            }
        });
    }

    private void marquageRetenue() {
        OffreRetenueRequest offreRetenueRequest = new OffreRetenueRequest();
        offreRetenueRequest.offre = offre._id;
        offreRetenueRequest.compteEtudiant = getCompte_Id();

        APIClient.createOffreRetenue((StageAppActivity) intituleOffre.getContext(), offreRetenueRequest, new ResultatAppel<OffreRetenue>() {
            @Override
            public void traiterResultat(OffreRetenue response) {
            }
        });
    }
}