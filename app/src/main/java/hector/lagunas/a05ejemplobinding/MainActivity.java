package hector.lagunas.a05ejemplobinding;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import hector.lagunas.a05ejemplobinding.databinding.ActivityMainBinding;
import hector.lagunas.a05ejemplobinding.modelo.Alumno;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ActivityResultLauncher<Intent> launcherAlumno;
    private ArrayList<Alumno> listaAlumnos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        listaAlumnos = new ArrayList<>();
        inicializarLauncher();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //lanzar la actividad AddAlumno
                launcherAlumno.launch(new Intent(MainActivity.this, AddAlumnoActivity.class));
            }
        });
    }

    private void inicializarLauncher(){
        launcherAlumno = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK){
                            if (result.getData() != null && result.getData().getExtras() != null){
                                Alumno alumno = (Alumno) result.getData().getExtras().getSerializable("ALUMNO");
                                listaAlumnos.add(alumno);
                                mostrarAlumnos();
                            }else {
                                Toast.makeText(MainActivity.this, "Oopsi pubsi", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(MainActivity.this, "ACCIÓN CANCELADA", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    private void mostrarAlumnos() {
        //eliminar lo que haya en el Linea Layout
        binding.contentMain.contenedorMain.removeAllViews();

        for (Alumno alumno:listaAlumnos) {
            LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);

            View alumnoView = layoutInflater.inflate(R.layout.alumno_fila_view, null);
            TextView txtNombre = alumnoView.findViewById(R.id.lbNombreAlumnoView);
            TextView txtApellidos = alumnoView.findViewById(R.id.lbApellidosAlumnoView);
            TextView txtCiclo = alumnoView.findViewById(R.id.lbCicloAlumnoView);
            TextView txtGrupo = alumnoView.findViewById(R.id.lbGrupoAlumnoView);

            txtNombre.setText(alumno.getNombre());
            txtApellidos.setText(alumno.getApellidos());
            txtCiclo.setText(alumno.getCiclo());
            txtGrupo.setText(String.valueOf(alumno.getGrupo()));

            binding.contentMain.contenedorMain.addView(alumnoView);
        }
    }


}