package fr.iut2.saeprojet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import fr.iut2.saeprojet.api.APIOffreClient;
import fr.iut2.saeprojet.api.APIOffreService;
import fr.iut2.saeprojet.entity.Offre;
import fr.iut2.saeprojet.entity.OffreList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListOffresActivity extends AppCompatActivity {

    // API
    private APIOffreService apiInterface;

    // View
    private TextView listOffreText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_offres);

        // Chargement de l'API
        apiInterface = APIOffreClient.getAPIService();

        // Init view
        listOffreText = findViewById(R.id.listOffreText);

        // Chargement par default
        doGetOffreList(1);
    }

    private void doGetOffreList(int pageNumber) {

        /**
         GET List Resources
         **/
        Call<OffreList> call = apiInterface.doGetOffreList();
        call.enqueue(new Callback<OffreList>() {
            @Override
            public void onResponse(Call<OffreList> call, Response<OffreList> response) {


                Log.d("TAG",response.code()+"");

                listOffreText.clearComposingText();

                String displayResponse = "";

                OffreList offres = response.body();

                for (Offre offre : offres.offres) {
                    displayResponse += offre.id + " " + offre.intitule + "\n";
                }

                listOffreText.setText(displayResponse);

            }

            @Override
            public void onFailure(Call<OffreList> call, Throwable t) {
                call.cancel();
                Log.e("TAG",t.getMessage());

            }
        });
    }
}