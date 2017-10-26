package mx.com.talentics.myschool.layouts;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import mx.com.talentics.myschool.R;
import mx.com.talentics.myschool.models.Alumno;
import mx.com.talentics.myschool.models.Materia;
import mx.com.talentics.myschool.models.Usuario;

public class index extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        final Context cont = this;

        Button m_materias = (Button) findViewById(R.id.btn_materias);
        Button m_profesores = (Button) findViewById(R.id.btn_profesores);
        Button m_alumnos = (Button) findViewById(R.id.btn_alumnos);


        m_materias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(cont, materias.class);
                startActivity(intent);
            }
        });

        m_profesores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(cont, profesores.class);
                startActivity(intent);
            }
        });
        m_alumnos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(cont, alumnos.class);
                startActivity(intent);
            }
        });
    }
}
