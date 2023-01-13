package fr.iut2.saeprojet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import fr.iut2.saeprojet.exemple.api.APIUserClient;
import fr.iut2.saeprojet.exemple.api.APIUserService;
import fr.iut2.saeprojet.exemple.entity.UserList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    // View
    private Button listResourcesView;
    private Button createUserView;
    private Button listUsersView;
    private Button listOffresView;

    private APIUserService apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiInterface = APIUserClient.getAPIService();

        // init View
        listResourcesView = findViewById(R.id.onListRessources);
        createUserView = findViewById(R.id.onCreateUser);
        listUsersView = findViewById(R.id.onListUsers);
        listOffresView = findViewById(R.id.onListOffres);

        //
        listResourcesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListResourcesActivity.class);
                startActivity(intent);
            }
        });

        //
        createUserView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateUserActivity.class);
                startActivity(intent);
            }
        });

        //
        listUsersView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListUsersActivity.class);
                startActivity(intent);
            }
        });

        //
        listOffresView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListOffresActivity.class);
                startActivity(intent);
            }
        });



        /**
         POST name and job Url encoded.
         **/
        Call<UserList> call3 = apiInterface.doCreateUserWithField("morpheus","leader");
        call3.enqueue(new Callback<UserList>() {
            @Override
            public void onResponse(Call<UserList> call, Response<UserList> response) {
                UserList userList = response.body();
                Integer text = userList.page;
                Integer total = userList.total;
                Integer totalPages = userList.totalPages;
                List<UserList.Datum> datumList = userList.data;
                Toast.makeText(getApplicationContext(), text + " page\n" + total + " total\n" + totalPages + " totalPages\n", Toast.LENGTH_SHORT).show();

                for (UserList.Datum datum : datumList) {
                    Toast.makeText(getApplicationContext(), "id : " + datum.id + " name: " + datum.first_name + " " + datum.last_name + " avatar: " + datum.avatar, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<UserList> call, Throwable t) {
                call.cancel();
            }
        });

    }
}