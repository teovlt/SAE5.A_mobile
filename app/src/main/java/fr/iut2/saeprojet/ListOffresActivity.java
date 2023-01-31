package fr.iut2.saeprojet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import fr.iut2.saeprojet.api.APIClient;
import fr.iut2.saeprojet.api.APIService;
import fr.iut2.saeprojet.api.ResultatAppel;
import fr.iut2.saeprojet.entity.Offre;
import fr.iut2.saeprojet.entity.OffresResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListOffresActivity extends StageAppActivity {

    private class ClickableEntry implements View.OnClickListener {
        public Offre offre;
        private int no_entry;
        private TextView view;

        public ClickableEntry(TextView view, int no_entry) {
            this.no_entry = no_entry;
            this.view = view;
        }

        @Override
        public void onClick(View view) {
            if (view.isEnabled() && (offre != null)) {
                Intent intent = new Intent(ListOffresActivity.this, OffreActivity.class);
                intent.putExtra("offre", offre);
                startActivity(intent);
            }
        }
    }

    // View
    private TextView nb_offres;
    private TextView [] offres = new TextView[5];
    private ClickableEntry [] entries = new ClickableEntry[5];

    private ArrayList<Offre> listeOffres = new ArrayList<>();
    private int no_page = 0, nb_pages = 0;

    private Button precedent;

    private Button suivant;

    private TextView retour;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_offres);

        // Init view
        nb_offres = findViewById(R.id.nb_offres);
        offres[0] = findViewById(R.id.offre1);
        offres[1] = findViewById(R.id.offre2);
        offres[2] = findViewById(R.id.offre3);
        offres[3] = findViewById(R.id.offre4);
        offres[4] = findViewById(R.id.offre5);
        precedent = findViewById(R.id.buttonPrec);
        suivant = findViewById(R.id.buttonNext);
        retour = findViewById(R.id.backSynthese);
        precedent.setEnabled(false);

        for(int i = 0; i < 5; i++) {
            entries[i] = new ClickableEntry(offres[i], i);
            offres[i].setOnClickListener(entries[i]);
        }

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListOffresActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        suivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (no_page < nb_pages) {
                    no_page += 1;
                    suivant.setEnabled(no_page < nb_pages);
                    precedent.setEnabled((no_page > 0) && (nb_pages > 0));

                    setIntituleOffres(no_page);
                }
            }
        });

        precedent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (0 < no_page) {
                    no_page -= 1;
                    suivant.setEnabled(no_page < nb_pages);
                    precedent.setEnabled((no_page > 0) && (nb_pages > 0));

                    setIntituleOffres(no_page);
                }
            }
        });

        // Chargement par default
        doGetOffreList();
    }

    private void setIntituleOffres(int page) {
        String intitule;
        for(int i = 0; i < 5; i++) {
            int index = i + 5 * page;

            if (index < listeOffres.size()) {
                Offre offre = listeOffres.get(i + 5 * page);
                if (offre.intitule.length() < 32) {
                    intitule = offre.intitule;
                } else {
                    intitule = offre.intitule.substring(0, 28) + " ...";
                }
                offres[i].setEnabled(true);
                entries[i].offre = offre;
            } else {
                intitule = "";
                offres[i].setEnabled(false);
                entries[i].offre = null;
            }
            offres[i].setText(intitule);
        }
    }

    private void doGetOffreList() {
        APIClient.getOffres(this, new ResultatAppel<OffresResponse>() {
            @Override
            public void traiterResultat(OffresResponse offresResponse) {
                for (Offre offre : offresResponse.offres) {
                    listeOffres.add(offre);
                }

                nb_offres.setText(listeOffres.size() + " Offres");
                nb_pages = nb_offres.length() / 5 + ((nb_offres.length() % 5) > 0 ? 1 : 0);
                suivant.setEnabled(nb_offres.length() > 5);
                setIntituleOffres(0);
            }

            @Override
            public void traiterErreur() {

            }
        });
    }
}