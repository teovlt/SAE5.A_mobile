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
import fr.iut2.saeprojet.entity.Candidature;
import fr.iut2.saeprojet.entity.CandidaturesResponse;
import fr.iut2.saeprojet.entity.Entreprise;
import fr.iut2.saeprojet.entity.Offre;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CandidatureAdapter extends ArrayAdapter<Candidature> {

    // API
    private APIService apiInterface;

    public CandidatureAdapter(Context mCtx, List<Candidature> candidatures) {
        super(mCtx, R.layout.template_offre_et_candidature, candidatures);

        // Chargement de l'API
        apiInterface = APIClient.getAPIService();
    }

    /**
     * Remplit une ligne de la listView avec les informations de la multiplication associée
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Récupération de la multiplication
        final Candidature candidature = getItem(position);

        // Charge le template XML
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.template_offre_et_candidature, parent, false);

        // Récupération des objets graphiques dans le template
        TextView intituleView = (TextView) rowView.findViewById(R.id.intitule);
        TextView entrepriseView = (TextView) rowView.findViewById(R.id.entreprise);
        refreshMesInformations(candidature, intituleView, entrepriseView);
        //TextView textViewDesc = (TextView) rowView.findViewById(R.id.textViewDesc);

        //
        intituleView.setText(candidature.etatCandidature);
        //textViewDesc.setText(task.getDescription());

        //
        return rowView;
    }

    private void refreshMesInformations(Candidature candidature, TextView intituleView, TextView entrepriseView) {

        //
        SharedPreferences sharedPref = getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String token = sharedPref.getString(getContext().getString(R.string.token_key), "no token");

        //
        Call<Offre> call = apiInterface.doGetOffre("Bearer " + token, candidature.getOffreId());
        call.enqueue(new Callback<Offre>() {
            @Override
            public void onResponse(Call<Offre> call, Response<Offre> response) {
                Offre offre = response.body();
                String intitule = offre.intitule;

                if (intitule.length() >= 34) {
                    intitule = intitule.substring(0, 31) + " ...";
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
                call.cancel();
                Log.e("TAG",t.getMessage());

            }
        });
    }

}