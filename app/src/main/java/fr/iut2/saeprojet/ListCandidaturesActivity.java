package fr.iut2.saeprojet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import fr.iut2.saeprojet.api.APIClient;
import fr.iut2.saeprojet.api.APIService;
import fr.iut2.saeprojet.entity.Candidature;
import fr.iut2.saeprojet.entity.CandidaturesResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListCandidaturesActivity extends AppCompatActivity {

    // API
    private APIService apiInterface;

    // View
    private TextView retourSyntheseView;
    private ListView candidaturesView;

    // data
    private CandidatureAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_candidatures);

        // Chargement de l'API
        apiInterface = APIClient.getAPIService();

        // Init view
        retourSyntheseView = findViewById(R.id.retourSynthese);
        candidaturesView = findViewById(R.id.candidatures);

        // Lier l'adapter au listView
        adapter = new CandidatureAdapter(this, new ArrayList<Candidature>());
        candidaturesView.setAdapter(adapter);

        //
        retourSyntheseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        candidaturesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Récupération de la tâche cliquée à l'aide de l'adapter
                Candidature candidature = adapter.getItem(position);

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

        //
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String token = sharedPref.getString(getString(R.string.token_key), "no token");
        long id = sharedPref.getLong(getString(R.string.login_id_key), 0);

        //
        Call<CandidaturesResponse> call = apiInterface.doGetCandidatures("Bearer " + token);
        call.enqueue(new Callback<CandidaturesResponse>() {
            @Override
            public void onResponse(Call<CandidaturesResponse> call, Response<CandidaturesResponse> response) {

                // Mettre à jour l'adapter avec la liste de taches
                adapter.clear();
                adapter.addAll(response.body().candidatures);

                // Now, notify the adapter of the change in source
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<CandidaturesResponse> call, Throwable t) {
                call.cancel();
                Log.e("TAG",t.getMessage());

            }
        });
    }
}