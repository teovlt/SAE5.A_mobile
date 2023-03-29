package fr.iut2.saeprojet;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import fr.iut2.saeprojet.api.APIClient;
import fr.iut2.saeprojet.api.APIService;
import fr.iut2.saeprojet.api.ResultatAppel;
import fr.iut2.saeprojet.entity.Entreprise;
import fr.iut2.saeprojet.entity.Offre;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OffreAdapter extends ArrayAdapter<Offre> {

    // API
    private APIService apiInterface;

    public OffreAdapter(Context mCtx, List<Offre> offres) {
        super(mCtx, R.layout.template_offre_et_candidature, offres);

        // Chargement de l'API
        apiInterface = APIClient.getAPIService();
    }

    /**
     * Remplit une ligne de la listView avec les informations de l'offre associée
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Récupération de l'offre
        final Offre offre = getItem(position);

        // Charge le template XML
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.template_offre_et_candidature, parent, false);

        // Récupération des objets graphiques dans le template
        TextView intituleView = (TextView) rowView.findViewById(R.id.intitule);
        TextView entrepriseView = (TextView) rowView.findViewById(R.id.entreprise);
        refreshMesInformations(offre, intituleView, entrepriseView);

        return rowView;
    }

    private void refreshMesInformations(Offre offre, TextView intituleView, TextView entrepriseView) {

        //
        SharedPreferences sharedPref = getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String token = sharedPref.getString(getContext().getString(R.string.token_key), "no token");

        //
        Call<Offre> call = apiInterface.doGetOffre("Bearer " + token, offre.id);
        call.enqueue(new Callback<Offre>() {
            @Override
            public void onResponse(Call<Offre> call, Response<Offre> response) {
                Offre offre = response.body();
                String intitule = offre.intitule;
                if (intitule.length() >= 38) {
                    intitule = intitule.substring(0, 35) + " ...";
                }
                intituleView.setText(intitule);
                APIClient.getEntreprise((StageAppActivity) intituleView.getContext(), APIClient.getEntrepriseId(offre.entreprise), new ResultatAppel<Entreprise>() {
                    @Override
                    public void traiterResultat(Entreprise response) {
                        entrepriseView.setText(response.raisonSociale);
                    }

                    @Override
                    public void traiterErreur() {

                    }
                });
            }

            @Override
            public void onFailure(Call<Offre> call, Throwable t) {

            }

            ;
        });


    }

}
