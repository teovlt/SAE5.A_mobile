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
import fr.iut2.saeprojet.entity.Offre;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OffreAdapter extends ArrayAdapter<Offre> {

    // API
    private APIService apiInterface;

    public OffreAdapter(Context mCtx, List<Offre> offres) {
        super(mCtx, R.layout.template_offre, offres);

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
        final View rowView = inflater.inflate(R.layout.template_offre, parent, false);

        // Récupération des objets graphiques dans le template
        TextView intituleView = (TextView) rowView.findViewById(R.id.intitule);
        refreshMesInformations(offre, intituleView);

        return rowView;
    }

    private void refreshMesInformations(Offre offre, TextView intituleView) {

        //
        SharedPreferences sharedPref = getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String token = sharedPref.getString(getContext().getString(R.string.token_key), "no token");

        //
        Call<Offre> call = apiInterface.doGetOffre("Bearer " + token, offre.id);
        call.enqueue(new Callback<Offre>() {
            @Override
            public void onResponse(Call<Offre> call, Response<Offre> response) {
                String intitule = response.body().intitule;

                if (intitule.length() >= 45) {
                    intitule = intitule.substring(0, 42) + " ...";

                }

                intituleView.setText(intitule);
            }

            @Override
            public void onFailure(Call<Offre> call, Throwable t) {
                call.cancel();
                Log.e("TAG",t.getMessage());

            }
        });
    }

}
