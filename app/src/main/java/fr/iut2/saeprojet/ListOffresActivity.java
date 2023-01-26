package fr.iut2.saeprojet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import fr.iut2.saeprojet.api.APIClient;
import fr.iut2.saeprojet.api.APIService;
import fr.iut2.saeprojet.entity.Offre;
import fr.iut2.saeprojet.entity.OffresResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListOffresActivity extends AppCompatActivity {

    // API
    private APIService apiInterface;

    // View
    private TextView listOffreText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_offres);

        // Chargement de l'API
        apiInterface = APIClient.getAPIService();

        // Init view
        listOffreText = findViewById(R.id.listOffreText);

        // Chargement par default
        doGetOffreList(1);
    }

    private void doGetOffreList(int pageNumber) {

        //
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String token = sharedPref.getString(getString(R.string.token_key), "no token");

        //
        Call<OffresResponse> call = apiInterface.doGetOffres("Bearer " + token);
        call.enqueue(new Callback<OffresResponse>() {
            @Override
            public void onResponse(Call<OffresResponse> call, Response<OffresResponse> response) {


                Log.d("TAG",response.code()+"");

                listOffreText.clearComposingText();

                String displayResponse = "";

                OffresResponse offres = response.body();

                for (Offre offre : offres.offres) {
                    displayResponse += offre.id + " " + offre.intitule + "\n";
                }

                listOffreText.setText(displayResponse);

            }

            @Override
            public void onFailure(Call<OffresResponse> call, Throwable t) {
                call.cancel();
                Log.e("TAG",t.getMessage());

            }
        });
    }
}