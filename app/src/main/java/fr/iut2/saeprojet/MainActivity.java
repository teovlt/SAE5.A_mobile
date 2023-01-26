package fr.iut2.saeprojet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import fr.iut2.saeprojet.api.APIClient;
import fr.iut2.saeprojet.api.APIService;
import fr.iut2.saeprojet.exempleNOTUSE.api.APIUserClient;
import fr.iut2.saeprojet.exempleNOTUSE.api.APIUserService;
import fr.iut2.saeprojet.exempleNOTUSE.entity.UserList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    // API
    private APIService apiInterface;

    // View
    private LinearLayout offresView;
    private LinearLayout candidaturesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Chargement de l'API
        apiInterface = APIClient.getAPIService();

        // init View
        offresView = findViewById(R.id.offres);
        candidaturesView = findViewById(R.id.candidatures);

        //
        offresView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListOffresActivity.class);
                startActivity(intent);
            }
        });

        //
        candidaturesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListOffresActivity.class);
                startActivity(intent);
            }
        });

    }
}