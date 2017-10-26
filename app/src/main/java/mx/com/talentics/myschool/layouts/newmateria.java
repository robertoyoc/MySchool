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
import java.util.Objects;

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
    public int mat;

    public String key;
    public String SelectedItem;

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
        Intent intent = getIntent();
        key = intent.getStringExtra("key");

        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Query isExisting = mDatabase.child("profesores").orderByChild("ap_pat");
        final Context cntx = this;
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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(cntx,
                        android.R.layout.simple_spinner_item, arrayspn);
                spn.setAdapter(adapter);


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });



        if(key==" "||key==null){
            mbtn_new.setVisibility(View.VISIBLE);
            mbtn_borrar.setVisibility(View.INVISIBLE);
            mbtn_actualizar.setVisibility(View.INVISIBLE);

        }else {
            mbtn_new.setVisibility(View.INVISIBLE);
            mbtn_borrar.setVisibility(View.VISIBLE);
            mbtn_actualizar.setVisibility(View.VISIBLE);
            Query isExistings = mDatabase.child("materias").child(key);
            isExistings.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Materia mat = dataSnapshot.getValue(Materia.class);
                        mtxt_nombre.setText(mat.getNombre());
                        mtxt_periodo.setText(mat.getPeriodo());
                        for (DataSnapshot child: dataSnapshot.child("profesores").getChildren()) {
                            String thekey = child.getKey();
                            int idex=0;
                            for (Element e:profesores) {
                                String elementkey = e.key;
                                if(thekey.equals(elementkey)){
                                    spn.setSelection(idex);

                                }
                                idex++;

                            }

                        }

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
                mat =1;
                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference();
                Query isExisting = mDatabase.child("materias").orderByChild("nombre").equalTo(mtxt_nombre.getText().toString());

                isExisting.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(mtxt_nombre.getText().toString().trim().equals("")) {
                            Context context = getApplicationContext();
                            String text= "Nombre es requerido!";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }else{
                            if(dataSnapshot.exists()){//existe la materia
                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    mat++; //agregando para poner el grupo adecuado
                                }

                            }

                            DatabaseReference mDatabase;
                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            String newkey = mDatabase.child("materias").push().getKey();

                            Materia mater = new Materia();
                            mater.setNombre(mtxt_nombre.getText().toString());
                            mater.setPeriodo(mtxt_periodo.getText().toString());
                            mater.setGpo(mat);

                            mDatabase.child("materias").child(newkey).setValue(mater);

                            for(Element e: profesores) {

                                Profesor prof = (Profesor) e.obj;
                                if (Objects.equals(prof.getAp_pat() + " " + prof.getNombre(), spn.getSelectedItem().toString())) {

                                    Map<String, Object> childUpdates = new HashMap<>();
                                    HashMap<String, Object> result = new HashMap<>();
                                    result.put(e.key, true);
                                    childUpdates.put("/materias/" + newkey + "/profesores", result);
                                    mDatabase.updateChildren(childUpdates);
                                }
                            }

                            Context context = getApplicationContext();
                            String text = "Materia creada";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();


                            Intent intent = new Intent(getApplicationContext(), materias.class);
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
        mbtn_actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference().child("materias").child(key);

                    if (mtxt_nombre.getText().toString().trim().equals("")) {
                        Context context = getApplicationContext();
                        String text = "Nombre es requerido!";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    } else {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("nombre", mtxt_nombre.getText().toString());
                        map.put("periodo", mtxt_periodo.getText().toString());

                        mDatabase.updateChildren(map);

                        for (Element e : profesores) {
                            Profesor prof = (Profesor) e.obj;
                                if (Objects.equals(prof.getAp_pat() + " " + prof.getNombre(), spn.getSelectedItem().toString())) {
                                    Map<String, Object> childUpdates = new HashMap<>();

                                    HashMap<String, Object> result = new HashMap<>();
                                    result.put(e.key, true);
                                    childUpdates.put( "/profesores", result);
                                    mDatabase.updateChildren(childUpdates);
                                }

                        }
                        Context context = getApplicationContext();
                        String text = "Campos Actualizados";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        Intent intent = new Intent(getApplicationContext(), materias.class);
                        startActivity(intent);                                              
                    }   newmateria.this.finish();

            }
        });

        mbtn_borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), materias.class);
                startActivity(intent);
                newmateria.this.finish();
                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("materias").child(key).removeValue();
                Context context = getApplicationContext();
                String text = "Materia borrada";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });




    }
}
