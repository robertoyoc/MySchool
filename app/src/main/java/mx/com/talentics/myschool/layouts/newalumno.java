package mx.com.talentics.myschool.layouts;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import mx.com.talentics.myschool.R;
import mx.com.talentics.myschool.models.Alumno;
import mx.com.talentics.myschool.models.Element;
import mx.com.talentics.myschool.models.Materia;
import mx.com.talentics.myschool.models.Profesor;

public class newalumno extends AppCompatActivity {

    public Button mbtn_new;
    public Button mbtn_actualizar;
    public Button mbtn_borrar;
    public EditText mnombre, mappat, mapmat, mcurp, mfechanac, mmatricula;

    public String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newalumno);

        mnombre= (EditText) findViewById(R.id.a_nombre);
        mapmat= (EditText) findViewById(R.id.a_apmat);
        mappat= (EditText) findViewById(R.id.a_appat);
        mcurp= (EditText) findViewById(R.id.a_curp);
        mfechanac= (EditText) findViewById(R.id.a_fechanac);
        mmatricula= (EditText) findViewById(R.id.a_matricula);

        mbtn_new = (Button) findViewById(R.id.a_new);
        mbtn_actualizar = (Button) findViewById(R.id.a_actualizar);
        mbtn_borrar = (Button) findViewById(R.id.a_borrar);

        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        Button magregamaterias = (Button)findViewById(R.id.btn_agregamaterias);

        magregamaterias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), agrega_materias.class);
                startActivity(intent);
            }
        });



        if(key==" "||key==null){
            mbtn_new.setVisibility(View.VISIBLE);
            mbtn_borrar.setVisibility(View.INVISIBLE);
            mbtn_actualizar.setVisibility(View.INVISIBLE);

        }else {
            DatabaseReference mDatabase;
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mbtn_new.setVisibility(View.INVISIBLE);
            mbtn_borrar.setVisibility(View.VISIBLE);
            mbtn_actualizar.setVisibility(View.VISIBLE);
            Query isExistings = mDatabase.child("alumnos").child(key);
            isExistings.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Alumno al = dataSnapshot.getValue(Alumno.class);
                        mnombre.setText(al.getNombre());
                        mappat.setText(al.getAp_pat());
                        mapmat.setText(al.getAp_mat());
                        mfechanac.setText(al.getFecha_nac());
                        mcurp.setText(al.getCURP());
                        mmatricula.setText(al.getMatricula());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }







        mbtn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference();
                Query isExisting = mDatabase.child("alumnos").orderByChild("matricula").equalTo(mmatricula.getText().toString());

                isExisting.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(mmatricula.getText().toString().trim().equals("")) {
                            Context context = getApplicationContext();
                            String text= "Matricula es requerida!";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }else{
                            if(!dataSnapshot.exists()){
                                DatabaseReference mDatabase;
                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                String newkey = mDatabase.child("alumnos").push().getKey();

                                Alumno al = new Alumno();
                                al.setNombre(mnombre.getText().toString());
                                al.setAp_pat(mappat.getText().toString());
                                al.setAp_mat(mapmat.getText().toString());
                                al.setFecha_nac(mfechanac.getText().toString());
                                al.setCURP(mcurp.getText().toString());
                                al.setMatricula(mmatricula.getText().toString());

                                mDatabase.child("alumnos").child(newkey).setValue(al);

                                Context context = getApplicationContext();
                                String text = "Alumno creado";
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();


                                Intent intent = new Intent(getApplicationContext(), alumnos.class);
                                startActivity(intent);
                                newalumno.this.finish();


                            }else {
                                Context context = getApplicationContext();
                                String text = "Matricula ya registrada";
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }







                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });







            }
        });
        mbtn_actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference();

                if (mmatricula.getText().toString().trim().equals("")) {
                    Context context = getApplicationContext();
                    String text = "Matricula es requerido!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else {
                    Query isExisting = mDatabase.child("alumnos").orderByChild("matricula").equalTo(mmatricula.getText().toString());

                    isExisting.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(!dataSnapshot.exists()){
                                DatabaseReference mDatabase;
                                mDatabase = FirebaseDatabase.getInstance().getReference().child("materias").child(key);
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("nombre", mnombre.getText().toString());
                                map.put("ap_pat", mappat.getText().toString());
                                map.put("ap_mat", mapmat.getText().toString());
                                map.put("fecha_nac", mfechanac.getText().toString());
                                map.put("curp", mcurp.getText().toString());
                                map.put("matricula", mmatricula.getText().toString());
                                mDatabase = FirebaseDatabase.getInstance().getReference().child("alumnos").child(key);

                                mDatabase.updateChildren(map);
                                Context context = getApplicationContext();
                                String text = "Campos Actualizados";
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                                Intent intent = new Intent(getApplicationContext(), alumnos.class);
                                startActivity(intent);
                                newalumno.this.finish();


                            }else {
                                Context context = getApplicationContext();
                                String text = "Matricula ya registrada";
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });




                }


            }
        });

        mbtn_borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), alumnos.class);
                startActivity(intent);
                newalumno.this.finish();
                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("alumnos").child(key).removeValue();
                Context context = getApplicationContext();
                String text = "Alumno borrado";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });






    }
}
