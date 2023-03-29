package fr.iut2.saeprojet;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import fr.iut2.saeprojet.api.APIClient;
import fr.iut2.saeprojet.api.ResultatAppel;
import fr.iut2.saeprojet.entity.Candidature;
import fr.iut2.saeprojet.entity.CandidaturesResponse;

public class ListCandidaturesActivity extends StageAppActivity {
    // View
    private ImageButton retour;
    private ListView candidaturesRefuseesView;
    private ListView candidaturesEnCoursView;

    private ListView candidaturesAccepteesView;
    private TextView nbcandidatures;
    // data
    private CandidatureAdapter adapterCandidatureRefusees;
    private CandidatureAdapter adapterCandidatureEnCours;
    private CandidatureAdapter adapterCandidatureAcceptees;

    private TextView textCandidatureRefusees;
    private TextView textCandidatureEnCours;

    private TextView textCandidatureAcceptees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_candidatures);

        // Init view
        retour = findViewById(R.id.retourDeListeCandidaturesAMain);
        candidaturesRefuseesView = findViewById(R.id.candidatures_refusees_list);
        candidaturesEnCoursView = findViewById(R.id.candidatures_en_cours_list);
        candidaturesAccepteesView = findViewById(R.id.candidatures_acceptees_list);

        nbcandidatures = findViewById(R.id.nbcandidatures_list);
        // Lier les adapter aux listView
        //Candidatures refusées
        adapterCandidatureRefusees = new CandidatureAdapter(this, new ArrayList<Candidature>());
        candidaturesRefuseesView.setAdapter(adapterCandidatureRefusees);
        //Candidatures en cours
        adapterCandidatureEnCours = new CandidatureAdapter(this, new ArrayList<Candidature>());
        candidaturesEnCoursView.setAdapter(adapterCandidatureEnCours);

        adapterCandidatureAcceptees = new CandidatureAdapter(this, new ArrayList<Candidature>());
        candidaturesAccepteesView.setAdapter(adapterCandidatureAcceptees);
        //
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListCandidaturesActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        candidaturesRefuseesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Récupération de la tâche cliquée à l'aide de l'adapter
                Candidature candidature = adapterCandidatureRefusees.getItem(position);

                //
                Intent intent = new Intent(ListCandidaturesActivity.this, CandidatureActivity.class);
                intent.putExtra(CandidatureActivity.CANDIDATURE_KEY, candidature);
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
                intent.putExtra(CandidatureActivity.CANDIDATURE_KEY, candidature);
                startActivity(intent);
            }
        });

        candidaturesAccepteesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Récupération de la tâche cliquée à l'aide de l'adapter
                Candidature candidature = adapterCandidatureAcceptees.getItem(position);

                //
                Intent intent = new Intent(ListCandidaturesActivity.this, CandidatureActivity.class);
                intent.putExtra(CandidatureActivity.CANDIDATURE_KEY, candidature);
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
                nbcandidatures.setText(getResources().getString(R.string.candidatures, candidatures.candidatures.size()));

                adapterCandidatureRefusees.clear();
                adapterCandidatureEnCours.clear();
                adapterCandidatureAcceptees.clear();

                for (Candidature c :
                        candidatures.candidatures) {
                    if (c.etatCandidature.equals("/api/etat_candidatures/3")) {
                        adapterCandidatureRefusees.add(c);

                    } else if (c.etatCandidature.equals("/api/etat_candidatures/6")) {
                        adapterCandidatureAcceptees.add(c);

                    } else {
                        adapterCandidatureEnCours.add(c);
                    }
                }
                adapterCandidatureRefusees.notifyDataSetChanged();
                adapterCandidatureEnCours.notifyDataSetChanged();
                adapterCandidatureAcceptees.notifyDataSetChanged();

                ajustListHeight(candidaturesRefuseesView);
                ajustListHeight(candidaturesAccepteesView);
                ajustListHeight(candidaturesEnCoursView);

                textCandidatureAcceptees = findViewById(R.id.candidatures_acceptees_none);
                textCandidatureEnCours = findViewById(R.id.candidatures_en_cours_none);
                textCandidatureRefusees = findViewById(R.id.candidatures_refusees_none);


                if (checkIfListEmpty(candidaturesAccepteesView)) {
                    textCandidatureAcceptees.setVisibility(View.VISIBLE);
                }
                if (checkIfListEmpty(candidaturesEnCoursView)) {
                    textCandidatureEnCours.setVisibility(View.VISIBLE);
                }
                if (checkIfListEmpty(candidaturesRefuseesView)) {
                    textCandidatureRefusees.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void traiterErreur() {

            }
        });
    }

    private void ajustListHeight(ListView listView) {

        ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return;
        }
        ViewGroup vg = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1) + listView.getPaddingTop() + listView.getPaddingTop());
        listView.setLayoutParams(par);
        listView.requestLayout();
    }

    private boolean checkIfListEmpty(ListView listView) {
        ListAdapter adapter = listView.getAdapter();

        return adapter.getCount() == 0;
    }
}