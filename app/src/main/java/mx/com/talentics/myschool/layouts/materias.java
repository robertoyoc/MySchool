package mx.com.talentics.myschool.layouts;

import android.content.Context;
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
import mx.com.talentics.myschool.models.Materia;
import mx.com.talentics.myschool.models.Profesor;

public class materias extends AppCompatActivity {
    public TableLayout tl;
    public String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materias);

        final Context cont = this;
        Button m_nvamateria = (Button) findViewById(R.id.btn_nvamateria);
        m_nvamateria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(cont, newmateria.class);
                startActivity(intent);
            }
        });


        tl = (TableLayout) findViewById(R.id.lst_materias);


        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Query isExisting = mDatabase.child("materias").orderByChild("nombre");


        isExisting.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tl.removeAllViews();
                for (final DataSnapshot data : dataSnapshot.getChildren()) {
                    Materia mat = data.getValue(Materia.class);

                    TableRow tr = new TableRow(getApplicationContext());
                    tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    Button b = new Button(getApplicationContext());

                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(), newmateria.class);
                            String key = data.getKey();
                            intent.putExtra("key", key);
                            startActivity(intent);
                            materias.this.finish();
                        }
                    });
                    b.setText(mat.getNombre()+" " + mat.getPeriodo() + " " + String.valueOf(mat.getGpo()));
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
