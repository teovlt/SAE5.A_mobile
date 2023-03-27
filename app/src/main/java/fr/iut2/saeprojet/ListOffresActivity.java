package fr.iut2.saeprojet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import fr.iut2.saeprojet.api.APIClient;
import fr.iut2.saeprojet.api.APIService;
import fr.iut2.saeprojet.api.ResultatAppel;
import fr.iut2.saeprojet.entity.Candidature;
import fr.iut2.saeprojet.entity.Offre;
import fr.iut2.saeprojet.entity.OffresResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListOffresActivity extends StageAppActivity {
    private ArrayList<Offre> listeOffres = new ArrayList<>();
    private int no_page = 0, nb_pages = 0;
    private Button precedent;
    private Button suivant;
    private ImageButton retour;
    private TextView indic_page;
    private TextView offres_disponibles;
    private OffreAdapter adapterOffre;
    private ListView offresList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_offres);
        // Init view
        offresList = findViewById(R.id.offres_list);
        adapterOffre = new OffreAdapter(this, new ArrayList<Offre>());
        offresList.setAdapter(adapterOffre);
        precedent = findViewById(R.id.buttonPrec);
        suivant = findViewById(R.id.buttonNext);
        precedent.setEnabled(false);
        offres_disponibles = findViewById(R.id.offres_disponibles);
        retour = findViewById(R.id.retourDeListeOffresAMain);
        indic_page = findViewById(R.id.indic_page);
        //Bouton retour page principale
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        suivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (no_page < nb_pages) {
                    no_page += 1;
                    suivant.setEnabled(no_page < nb_pages-1);
                    precedent.setEnabled((no_page > 0) && (nb_pages > 0));
                    updateOffres(no_page);
                    indic_page.setText(getResources().getString(R.string.NumpageSurnbPage,no_page+1,nb_pages));
                }
            }
        });

        precedent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (no_page>0) {
                    no_page -= 1;
                    suivant.setEnabled(no_page < nb_pages);
                    precedent.setEnabled((no_page > 0) && (nb_pages > 0));
                    updateOffres(no_page);
                    indic_page.setText(getResources().getString(R.string.NumpageSurnbPage,no_page+1,nb_pages));
                }
            }
        });
        offresList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Récupération de l'offre cliquée à l'aide de l'adapter
                Offre offre = adapterOffre.getItem(position);
                //Lancement activité de l'offre correspondante
                Intent intent = new Intent(ListOffresActivity.this, OffreActivity.class);
                intent.putExtra(OffreActivity.OFFRE_KEY, offre);
                startActivity(intent);
            }
        });

        // Chargement par default
        doGetOffreList();
    }

   private void updateOffres(int no_page){
       adapterOffre.clear();
       for (int i = (no_page*5) ; i< listeOffres.size() && i <(no_page*5)+5 ; i++) {
               adapterOffre.add(listeOffres.get(i));
       }
       adapterOffre.notifyDataSetChanged();
   }

    private void doGetOffreList() {
        APIClient.getOffres(this, new ResultatAppel<OffresResponse>() {
            @Override
            public void traiterResultat(OffresResponse offresResponse) {
                listeOffres.addAll(offresResponse.offres);
                offres_disponibles.setText(getResources().getString(R.string.offres_disponibles,listeOffres.size()));
                nb_pages = listeOffres.size() / 5 + ((listeOffres.size() % 5) > 0 ? 1 : 0);
                suivant.setEnabled(listeOffres.size() > 5);
               // setIntituleOffres(0);
                indic_page.setText(getResources().getString(R.string.NumpageSurnbPage,1,nb_pages));
                //Mise à jour des 5 offres
                updateOffres(no_page);
            }

            @Override
            public void traiterErreur() {

            }
        });
    }
}