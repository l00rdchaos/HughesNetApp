package com.hughesnet.hughesnetapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.hughesnet.hughesnetapp.adapter.RecyclerAdapterClient;
import com.hughesnet.hughesnetapp.api.ApiClient;
import com.hughesnet.hughesnetapp.api.ApiProfile;
import com.hughesnet.hughesnetapp.api.ApiRegister;
import com.hughesnet.hughesnetapp.api.ApiUpdate;
import com.hughesnet.hughesnetapp.model.Advisor;
import com.hughesnet.hughesnetapp.model.Advisor;

import java.io.ByteArrayOutputStream;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit2.Call;

public class Profile extends AppCompatActivity {
    private ApiProfile apiInterface;
    private List<Advisor> Advisores;
    public static final String ROOT_URL="http://trainingcomercial.com/HughesNetApp/userdata";
    int PICK_IMAGE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        EditText n=findViewById(R.id.id_name_registro);
        EditText a=findViewById(R.id.id_surname_registro);
        EditText t=findViewById(R.id.id_phone_registro);
        EditText c=findViewById(R.id.id_email_registro);


        //Password
        Button psww=findViewById(R.id.btn_register_advisor2);

        Button send=findViewById(R.id.btn_register_advisor);

        chargeandfillText();

psww.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent f = new Intent(Profile.this, ChangePassword.class);
        startActivity(f);
        finish();

    }
});


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String telefono=t.getText().toString().trim().replace(" ","");
                String nombre=n.getText().toString().trim();
                String apellido=a.getText().toString().trim();
                String correo=c.getText().toString().replace(" ","");


                //   Toast.makeText(valid_loggin.this, pass1+" "+pass2, Toast.LENGTH_LONG).show();

                SharedPreferences preferences=getSharedPreferences("login", Context.MODE_PRIVATE);
                String dni=preferences.getString("dni","def");


                if(camposvacios(nombre.toString())==false && camposvacios(apellido.toString())==false && validarnumero(telefono.toString())==true && correovalidar(correo.toString())==true ){




                        RestAdapter adapter = new RestAdapter.Builder()
                                .setEndpoint(ROOT_URL)
                                .build();

                        ApiUpdate api = adapter.create(ApiUpdate.class);
                        api.insertadvisor(
                                nombre,
                                apellido,
                                telefono,
                                correo,
                                dni,

                                new Callback<retrofit.client.Response>() {
                                    @Override
                                    public void success(retrofit.client.Response result, Response response) {

                                        Toast.makeText(Profile.this,"Actualizando", Toast.LENGTH_SHORT).show();



                                        Intent f = new Intent(Profile.this, MainActivity.class);
                                        startActivity(f);
                                        finish();
                                    }



                                    @Override
                                    public void failure(RetrofitError error) {
                                        Toast.makeText(Profile.this, error.toString(), Toast.LENGTH_LONG).show();

                                    }
                                });



                }else{


                    Toast.makeText(Profile.this, "Datos Incompletos (Revise y vuelva a intentar)", Toast.LENGTH_LONG).show();


                }


            }





        });
    }

    public boolean contrasena_iguales(String pass2,String pass1) {

        String pas1=pass1.trim();
        String pas2=pass2.trim();
        if (!pas1.equals(pas2)){
            return false;
        } else {
            return true;
        }
    }


    public boolean validarnumero(String cadena) {
        if (cadena.matches("[0-9]*") && cadena.length()==10) {
            return true;
        } else {
            return false;
        }
    }



    public boolean validardni(String cadena) {
        if (cadena.matches("[0-9]*") && cadena.length()==10) {
            return true;
        } else {
            return false;
        }
    }



    public boolean correovalidar(String email) {

        if(email.matches("[-\\w\\.]+@\\w+\\.\\w+")){
            return true;
        }else {
            return false;
        }



    }


    public boolean camposvacios(String campo) {

        if(campo.length()==0){
            return true;
        }else {
            return false;
        }



    }

    private void chargeandfillText() {

        EditText n=findViewById(R.id.id_name_registro);
        EditText a=findViewById(R.id.id_surname_registro);
        EditText t=findViewById(R.id.id_phone_registro);
        EditText c=findViewById(R.id.id_email_registro);

        SharedPreferences preferences=getSharedPreferences("login", Context.MODE_PRIVATE);
        String dni=preferences.getString("dni","def");
        apiInterface = ApiClient.getApiClient().create(ApiProfile.class);

        Call<List<Advisor>>call = apiInterface.getAdvisor("http://www.trainingcomercial.com/HughesNetApp/userdata/profile.php?dni="+dni);
        call.enqueue(new retrofit2.Callback<List<Advisor>>() {

            public void onResponse(Call<List<Advisor>> call, retrofit2.Response<List<Advisor>> response) {

                Toast.makeText(Profile.this, "Cargando Datos", Toast.LENGTH_SHORT).show();



                    if (response.body() != null) {
                        Advisores=response.body();
                    n.setText(Advisores.get(0).getName());
                    a.setText(Advisores.get(0).getSurname());
                    t.setText(Advisores.get(0).getPhone());
                    c.setText(Advisores.get(0).getEmail());


                    }
                    else{

                        Toast.makeText(Profile.this, "No hay datos", Toast.LENGTH_SHORT).show();
                    }


            }

            @Override
            public void onFailure(Call<List<Advisor>> call, Throwable t) {
                Log.e("Respuesta",t.toString());
            }


        });



    }

    /*public void uploadImage(String action) {
        Toast.makeText(this, "subiendo", Toast.LENGTH_SHORT).show();

        final SharedPreferences preferences=this.getSharedPreferences("bg-lgof", Context.MODE_PRIVATE);
        final String jwt=preferences.getString("jwt","0null0");
        ApiChangePicture apiInterface = ApiClient.getApiClient().create(ApiChangePicture.class);
        String imagen=getStringImagen(bitmap);
        Call<ModelSucces> call = apiInterface.Uploadphoto(getString(R.string.profileurl),jwt,actionxp,imagen);
        call.enqueue(new retrofit2.Callback<ModelSucces>() {
            @Override
            public void onResponse(Call<ModelSucces> call, retrofit2.Response<ModelSucces> response) {


                if(response.body().getStatus().equals("successs")){


                    Toast.makeText(getApplicationContext(), "Uploades Cover", Toast.LENGTH_SHORT).show();
                }
                else {

                    Toast.makeText(getApplicationContext(), "No upload cover", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ModelSucces> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Se ha producido un error", Toast.LENGTH_SHORT).show();
                Log.e("error",t.toString());

            }




        });

    }*/

    public String getStringImagen(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleciona imagen"), PICK_IMAGE_REQUEST);

    }



}