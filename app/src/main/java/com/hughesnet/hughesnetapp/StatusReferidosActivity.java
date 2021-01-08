package com.hughesnet.hughesnetapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hughesnet.hughesnetapp.adapter.RecyclerAdapterAsesor;
import com.hughesnet.hughesnetapp.api.ApiAsesor;
import com.hughesnet.hughesnetapp.api.ApiClient;
import com.hughesnet.hughesnetapp.model.Asesor;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusReferidosActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    private RecyclerAdapterAsesor adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ApiAsesor apiInterface;
    private List<Asesor> asesores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_referidos);


        final String Estado = getIntent().getStringExtra(Referidos.Estado);

        recyclerView= (RecyclerView) findViewById(R.id.idfragmen_asesores2);
        final GridLayoutManager gridLayoutManager= new GridLayoutManager(getApplicationContext(),1);
        recyclerView.setLayoutManager(gridLayoutManager);

        layoutManager = new GridLayoutManager(getApplicationContext(),1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);


        apiInterface = ApiClient.getApiClient().create(ApiAsesor.class);
        String tip=Estado;
        Toast.makeText(getApplicationContext(),tip,Toast.LENGTH_LONG).show();

        Call<List<Asesor>> call = apiInterface.getAsesores("http://trainingcomercial.com/HughesNetApp/ListaAsesores.php?t="+tip);
        call.enqueue(new Callback<List<Asesor>>() {
            @Override
            public void onResponse(Call<List<Asesor>> call, Response<List<Asesor>> response) {
                if(response.body()!=null) {
                    asesores = response.body();
                    adapter = new RecyclerAdapterAsesor(asesores);
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                    recyclerView.setHasFixedSize(true);
                    adapter.notifyDataSetChanged();
                    recyclerView.setNestedScrollingEnabled(false);
                    adapter.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            String nombreArreglo = asesores.get(recyclerView.getChildAdapterPosition(view)).getName();
                            String valorArreglo = asesores.get(recyclerView.getChildAdapterPosition(view)).getPhone();


                            Intent c = new Intent(getApplicationContext(), Referidos.class);
                            Intent re = new Intent(getApplicationContext(), Referidos.class);

                            startActivity(re);
                        }
                    });

                }     }

            @Override
            public void onFailure(Call<List<Asesor>> call, Throwable t) {

            }


        });
    }
}