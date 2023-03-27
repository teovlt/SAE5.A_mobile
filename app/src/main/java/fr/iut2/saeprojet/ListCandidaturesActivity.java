package fr.iut2.saeprojet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import fr.iut2.saeprojet.api.APIClient;
import fr.iut2.saeprojet.api.APIService;
import fr.iut2.saeprojet.api.ResultatAppel;
import fr.iut2.saeprojet.entity.Candidature;
import fr.iut2.saeprojet.entity.CandidaturesResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListCandidaturesActivity extends StageAppActivity {
    // View
    private ImageButton retour;
    private ListView candidaturesRefuseesView;
    private ListView candidaturesEnCoursView;
    private TextView nbcandidatures;
    // data
    private CandidatureAdapter adapterCandidatureRefusees;
    private CandidatureAdapter adapterCandidatureEnCours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_candidatures);

        // Init view
        retour = findViewById(R.id.retourDeListeCandidaturesAMain);
        candidaturesRefuseesView = findViewById(R.id.candidatures_refusees_list);
        candidaturesEnCoursView = findViewById(R.id.candidatures_en_cours_list);
        nbcandidatures = findViewById(R.id.nbcandidatures_list);
        // Lier les adapter aux listView
        //Candidatures refusées
        adapterCandidatureRefusees = new CandidatureAdapter(this, new ArrayList<Candidature>());
        candidaturesRefuseesView.setAdapter(adapterCandidatureRefusees);
        //Candidatures en cours
        adapterCandidatureEnCours = new CandidatureAdapter(this, new ArrayList<Candidature>());
        candidaturesEnCoursView.setAdapter(adapterCandidatureEnCours);
        //
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        candidaturesRefuseesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Récupération de la tâche cliquée à l'aide de l'adapter
                Candidature candidature = adapterCandidatureRefusees.getItem(position);

                //
                Intent intent = new Intent(ListCandidaturesActivity.this, CandidatureActivity.class);
                intent.putExtra(CandidatureActivity.CANDIDADURE_KEY, candidature);
                startActivity(intent);
            }
        });
        candidaturesEnCoursView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Récupération de la tâche cliquée à l'aide de l'adapter
                Candidature candidature = adapterCandidatureEnCours.getItem(position);

                //
                Intent intent = new Intent(ListCandidaturesActivity.this, CandidatureActivity.class);
                intent.putExtra(CandidatureActivity.CANDIDADURE_KEY, candidature);
                startActivity(intent);
            }
        });
        //
        refreshMesInformations();
    }

    private void refreshMesInformations() {
        APIClient.getCandidatures(this, new ResultatAppel<CandidaturesResponse>() {
            @Override
            public void traiterResultat(CandidaturesResponse candidatures) {
                // Mettre à jour les adapter avec la liste des candidatures
                nbcandidatures.setText(getResources().getString(R.string.candidatures,candidatures.candidatures.size()));

                adapterCandidatureRefusees.clear();
                adapterCandidatureEnCours.clear();

                for (Candidature c:
                candidatures.candidatures) {
                    if(c.etatCandidature.equals("/api/etat_candidatures/3")){
                        adapterCandidatureRefusees.add(c);

                    }else{
                        adapterCandidatureEnCours.add(c);

                    }
                }
                adapterCandidatureRefusees.notifyDataSetChanged();
                adapterCandidatureEnCours.notifyDataSetChanged();

            }

            @Override
            public void traiterErreur() {

            }
        });
    }
}