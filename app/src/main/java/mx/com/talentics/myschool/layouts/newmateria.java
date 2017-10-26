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
import android.widget.TableLayout;
import android.widget.TableRow;
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

import mx.com.talentics.myschool.R;
import mx.com.talentics.myschool.models.Element;
import mx.com.talentics.myschool.models.Materia;
import mx.com.talentics.myschool.models.Profesor;

public class newmateria extends AppCompatActivity {
    public Spinner spn;
    public Button mbtn_new;
    public Button mbtn_actualizar;
    public Button mbtn_borrar;
    public EditText mtxt_nombre, mtxt_periodo;

    public String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newmateria);

        spn = (Spinner) findViewById(R.id.spn_profesor);
        mtxt_nombre = (EditText) findViewById(R.id.mtxt_nombre);
        mtxt_periodo = (EditText) findViewById(R.id.mtxt_periodo);

        mbtn_new = (Button) findViewById(R.id.mbtn_agregar);
        mbtn_actualizar = (Button) findViewById(R.id.mbtn_actualizar);
        mbtn_borrar = (Button) findViewById(R.id.mbtn_borrar);

        final ArrayList<String> arrayspn = new ArrayList<String>();

        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();


        if(key==" "||key==null){
            mbtn_new.setVisibility(View.VISIBLE);
            mbtn_borrar.setVisibility(View.INVISIBLE);
            mbtn_actualizar.setVisibility(View.INVISIBLE);

        }else {
            mbtn_new.setVisibility(View.INVISIBLE);
            mbtn_borrar.setVisibility(View.VISIBLE);
            mbtn_actualizar.setVisibility(View.VISIBLE);
            Query isExisting = mDatabase.child("materias").child(key);
            isExisting.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Materia mat = dataSnapshot.getValue(Materia.class);
                        mtxt_nombre.setText(mat.getNombre());
                        mtxt_periodo.setText(mat.getPeriodo());

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }


        Query isExisting = mDatabase.child("profesores").orderByChild("ap_pat");

        final ArrayList<Element> profesores = new ArrayList<Element>();

        isExisting.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 arrayspn.clear();
                 for (final DataSnapshot data : dataSnapshot.getChildren()) {
                     Profesor prof = data.getValue(Profesor.class);
                     Element cr_prof = new Element();

                     cr_prof.key = data.getKey();
                     cr_prof.obj = prof;
                     profesores.add(cr_prof);
                     arrayspn.add(prof.getAp_pat() + " " + prof.getNombre());
                 }


             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }

         });

        final Spinner spn = (Spinner) findViewById(R.id.spn_profesor);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arrayspn);
        spn.setAdapter(adapter);





        mbtn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference();
                Query isExisting = mDatabase.child("materias").orderByChild("nombre").equalTo(mtxt_nombre.getText().toString());

                isExisting.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // dataSnapshot is the "issue" node with all children with id 0
                            int mat =0;
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                Materia materia = data.getValue(Materia.class);

                                Context context = getApplicationContext();
                                String text = "Materia ya se encuentra registrada!";
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                                mat++;
                            }

                            DatabaseReference mDatabase;

                            if(mtxt_nombre.getText().toString().trim().equals("")) {
                                Context context = getApplicationContext();
                                String text = "Nombre es requerido!";
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }else{
                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                String key = mDatabase.child("materias").push().getKey();
                                mat++;

                                Materia mater = new Materia();
                                mater.setNombre(mtxt_nombre.getText().toString());
                                mater.setPeriodo(mtxt_periodo.getText().toString());
                                mater.setGpo(mat);

                                mDatabase.child("materias").child(key).setValue(mater);

                                for(Element e: profesores) {
                                    Profesor prof = (Profesor) e.obj;
                                    if (prof.getAp_pat() + " " + prof.getNombre() == spn.getSelectedItem().toString()) {
                                        Map<String, Object> childUpdates = new HashMap<>();

                                        HashMap<String, Object> result = new HashMap<>();
                                        result.put(e.key, true);
                                        childUpdates.put("/materias/" + key, result);
                                        mDatabase.updateChildren(childUpdates);
                                    }
                                }

                                Context context = getApplicationContext();
                                CharSequence text = "Materia agregada!";
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                                Intent intent = new Intent(context, materias.class);
                                startActivity(intent);
                                newmateria.this.finish();

                            }
                        }
                        else{
                            DatabaseReference mDatabase;
                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            String key = mDatabase.child("materias").push().getKey();

                            Materia mater = new Materia();
                            mater.setNombre(mtxt_nombre.getText().toString());
                            mater.setPeriodo(mtxt_periodo.getText().toString());
                            mater.setGpo(1);

                            mDatabase.child("materias").child(key).setValue(mater);

                            for(Element e: profesores) {
                                Profesor prof = (Profesor) e.obj;
                                if (prof.getAp_pat() + " " + prof.getNombre() == spn.getSelectedItem().toString()) {
                                    Map<String, Object> childUpdates = new HashMap<>();

                                    HashMap<String, Object> result = new HashMap<>();
                                    result.put(e.key, true);
                                    childUpdates.put("/materias/" + key, result);
                                    mDatabase.updateChildren(childUpdates);
                                }
                            }

                            Context context = getApplicationContext();
                            CharSequence text = "Materia agregada!";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                            Intent intent = new Intent(context, materias.class);
                            startActivity(intent);
                            newmateria.this.finish();
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
