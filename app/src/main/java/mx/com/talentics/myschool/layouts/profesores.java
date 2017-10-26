package mx.com.talentics.myschool.layouts;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import mx.com.talentics.myschool.R;
import mx.com.talentics.myschool.models.Profesor;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class profesores extends AppCompatActivity {

    public TableLayout tl;
    public String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profesores);
        final Context cont = this;

        tl = (TableLayout) findViewById(R.id.list_profesores);


        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Query isExisting = mDatabase.child("profesores").orderByChild("ap_pat");


        isExisting.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tl.removeAllViews();
                for (final DataSnapshot data : dataSnapshot.getChildren()) {
                    Profesor prof = data.getValue(Profesor.class);

                    TableRow tr = new TableRow(getApplicationContext());
                    tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    Button b = new Button(getApplicationContext());

                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(), newprofesor.class);
                            String key = data.getKey();
                            intent.putExtra("key", key);
                            startActivity(intent);
                        }
                    });
                    b.setText(prof.getAp_pat()+ " " + prof.getAp_mat() + " " + prof.getNombre());
                    b.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    tr.addView(b);
                    tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






        Button m_nvoprofesor = (Button) findViewById(R.id.btn_nvoprofesor);
        m_nvoprofesor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(cont, newprofesor.class);
                startActivity(intent);
            }
        });
    }
}
