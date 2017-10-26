package mx.com.talentics.myschool.layouts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import mx.com.talentics.myschool.R;
import mx.com.talentics.myschool.models.Alumno;
import mx.com.talentics.myschool.models.Materia;

public class alumnos extends AppCompatActivity {

    public TableLayout tl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumnos);



        Button m_nvalumno = (Button) findViewById(R.id.btn_newalumno);
        m_nvalumno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), newalumno.class);
                startActivity(intent);
            }
        });

        tl = (TableLayout) findViewById(R.id.lst_alumnos);


        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Query isExisting = mDatabase.child("alumnos").orderByChild("ap_pat");


        isExisting.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tl.removeAllViews();
                for (final DataSnapshot data : dataSnapshot.getChildren()) {
                    Alumno al = data.getValue(Alumno.class);

                    TableRow tr = new TableRow(getApplicationContext());
                    tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    Button b = new Button(getApplicationContext());

                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(), newalumno.class);
                            String key = data.getKey();
                            intent.putExtra("key", key);
                            startActivity(intent);
                        }
                    });
                    b.setText(al.getAp_pat()+" " + al.getAp_mat() + " " + al.getNombre());
                    b.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    tr.addView(b);
                    tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
