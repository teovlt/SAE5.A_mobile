package fr.iut2.saeprojet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import fr.iut2.saeprojet.exemple.api.APIUserClient;
import fr.iut2.saeprojet.exemple.api.APIUserService;
import fr.iut2.saeprojet.exemple.entity.MultipleResource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListResourcesActivity extends AppCompatActivity {

    // API
    private APIUserService apiInterface;

    // View
    private TextView responseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Chargement de l'API
        apiInterface = APIUserClient.getAPIService();

        // Init view
        responseText = findViewById(R.id.responseText);

        /**
         GET List Resources
         **/
        Call<MultipleResource> call = apiInterface.doGetListResources();
        call.enqueue(new Callback<MultipleResource>() {
            @Override
            public void onResponse(Call<MultipleResource> call, Response<MultipleResource> response) {


                Log.d("TAG",response.code()+"");

                String displayResponse = "";

                MultipleResource resource = response.body();
                Integer text = resource.page;
                Integer total = resource.total;
                Integer totalPages = resource.totalPages;
                List<MultipleResource.Datum> datumList = resource.data;

                displayResponse += text + " Page\n" + total + " Total\n" + totalPages + " Total Pages\n";

                for (MultipleResource.Datum datum : datumList) {
                    displayResponse += datum.id + " " + datum.name + " " + datum.pantoneValue + " " + datum.year + "\n";
                }

                responseText.setText(displayResponse);

            }

            @Override
            public void onFailure(Call<MultipleResource> call, Throwable t) {
                call.cancel();
            }
        });

    }
}