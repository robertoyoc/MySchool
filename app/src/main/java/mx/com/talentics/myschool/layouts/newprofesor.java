package mx.com.talentics.myschool.layouts;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import mx.com.talentics.myschool.R;
import mx.com.talentics.myschool.models.Alumno;
import mx.com.talentics.myschool.models.Profesor;

public class newprofesor extends AppCompatActivity {
    public String key;

    public EditText mname;
    public EditText mappat;
    public EditText mapmat;
    public EditText mfecha;
    public EditText mcurp;
    public EditText mrfc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newprofesor);
        final Context cont = this;

        mname = (EditText) findViewById(R.id.ptxt_name);
        mappat = (EditText) findViewById(R.id.ptxt_appat);
        mapmat = (EditText) findViewById(R.id.ptxt_apmat);
        mfecha = (EditText) findViewById(R.id.ptxt_fechanac);
        mcurp = (EditText) findViewById(R.id.ptxt_curp);
        mrfc = (EditText) findViewById(R.id.ptxt_rfc);


        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Button mnew_profesor = (Button) findViewById(R.id.btn_addprofesor);
        Button mborrar=  (Button) findViewById(R.id.btn_borrar);
        Button mactualizar = (Button) findViewById(R.id.btn_act);

        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        if(key==" "||key==null){
            mnew_profesor.setVisibility(View.VISIBLE);
            mborrar.setVisibility(View.INVISIBLE);
            mactualizar.setVisibility(View.INVISIBLE);

        }else {
            mnew_profesor.setVisibility(View.INVISIBLE);
            mborrar.setVisibility(View.VISIBLE);
            mactualizar.setVisibility(View.VISIBLE);

            Query isExisting = mDatabase.child("profesores").child(key);
            isExisting.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Profesor prof = dataSnapshot.getValue(Profesor.class);
                        mname.setText(prof.getNombre());
                        mappat.setText(prof.getAp_pat());
                        mapmat.setText(prof.getAp_mat());
                        mfecha.setText(prof.getFecha_nac());
                        mcurp.setText(prof.getCURP());
                        mrfc.setText(prof.getRFC());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        mborrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), profesores.class);
                startActivity(intent);
                newprofesor.this.finish();
                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("profesores").child(key).removeValue();
                Context context = getApplicationContext();
                String text = "Profesor borrado";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();


            }
        });
        mactualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference().child("profesores").child(key);

                if(mrfc.getText().toString().trim().equals("")){
                    Context context = getApplicationContext();
                    String text = "RFC es requerido!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }else{
                    Map<String,Object> map = new HashMap<String,Object>();
                    map.put("nombre", mname.getText().toString());
                    map.put("ap_pat", mappat.getText().toString());
                    map.put("ap_mat", mapmat.getText().toString());
                    map.put("fecha_nac", mfecha.getText().toString());
                    map.put("curp", mcurp.getText().toString());
                    map.put("rfc", mrfc.getText().toString());

                    mDatabase.updateChildren(map);

                    Context context = getApplicationContext();
                    String text = "Campos Actualizados";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                }

            }
        });


        mnew_profesor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference();


                Query isExisting = mDatabase.child("profesores").orderByChild("rfc").equalTo(mrfc.getText().toString());


                isExisting.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // dataSnapshot is the "issue" node with all children with id 0
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                Profesor prof = data.getValue(Profesor.class);
                                Context context = getApplicationContext();
                                String text = "RFC ya se encuentra registrada!";
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }
                        }
                        else{
                            DatabaseReference mDatabase;


                            if(mrfc.getText().toString().trim().equals("")) {
                                Context context = getApplicationContext();
                                String text = "RFC es requerido!";
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }else{
                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                String key = mDatabase.child("profesores").push().getKey();


                                Profesor prof = new Profesor();
                                prof.setNombre(mname.getText().toString());
                                prof.setAp_pat(mappat.getText().toString());
                                prof.setAp_mat(mapmat.getText().toString());
                                prof.setFecha_nac(mfecha.getText().toString());
                                prof.setCURP(mcurp.getText().toString());
                                prof.setRFC(mrfc.getText().toString());


                                mDatabase.child("profesores").child(key).setValue(prof);
                                Context context = getApplicationContext();
                                CharSequence text = "Profesor agregado!";
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                                Intent intent = new Intent(cont, profesores.class);
                                startActivity(intent);
                                newprofesor.this.finish();

                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
    }
}
