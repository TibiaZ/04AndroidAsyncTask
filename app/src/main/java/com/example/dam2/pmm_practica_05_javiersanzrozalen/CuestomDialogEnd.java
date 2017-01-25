package com.example.dam2.pmm_practica_05_javiersanzrozalen;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/***************************************************************/
/********* -->DIÁLOGO FINAL PARA MOSTRAR RESULTADOS<-- *********/
/***************************************************************/

public class CuestomDialogEnd extends DialogFragment implements View.OnClickListener{

    private String nombreJugador;
    private int fase;
    private TextView tvFelicidadesUsuario;
    private TextView tvFaseRecord;
    private Button btnCerrar;
    private Button btnSeguirJugando;
    private OnJuegoFinalizado listener;
    private View view;

    /** CALLBACK PARA SABER SI QUEREMOS CERRAR EL JUEGO O SEGUIR JUGANDO **/

    // Método instanciar nuestro CustomDialogEnd. Le pasaremos como parámetro el nombre de jugador y la fase
    public static CuestomDialogEnd newInstance(String nombreJugador, int fase) {
        Bundle args = new Bundle();
        CuestomDialogEnd fragment = new CuestomDialogEnd();
        fragment.nombreJugador = nombreJugador;
        fragment.fase = fase;
        fragment.setArguments(args);
        return fragment;
    }

    // Inertaz que deberemos implementar para que se la actividad se suscriba al evento del CustomDialogEnd
    public interface OnJuegoFinalizado{
        void onCerrar();
        void onSeguirJugando();
    }

    // Método para que la actividad se suscriba al evento del CustomDialogEnd
    public void setOnJuegoFinalizado(OnJuegoFinalizado listener){
        this.listener = listener;
    }


    /** MÉTODO PARA INSTANCIAR LA VISTA DE NUESTRO CustomDialogEnd E INFLAR LOS WIDGETS **/

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Creamos el builder para nuestro custom dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Obtenemos la vista de nuestro dialog
        view = getActivity().getLayoutInflater().inflate(R.layout.activity_cuestom_dialog_end, null);

        // Inflamos nuestros widgets
        tvFelicidadesUsuario = (TextView) view.findViewById(R.id.textViewFelicidadesUsuario);
        tvFaseRecord = (TextView) view.findViewById(R.id.textViewFaseRecord);
        btnCerrar = (Button) view.findViewById(R.id.buttonCerrar);
        btnSeguirJugando = (Button) view.findViewById(R.id.buttonSeguirJugando);

        // Nos suscribimos como escuchadores de nuestros botones
        btnCerrar.setOnClickListener(this);
        btnSeguirJugando.setOnClickListener(this);

        // Ponemos en los text view el usuario y la fase
        tvFelicidadesUsuario.setText("Felicidades " + nombreJugador);
        tvFaseRecord.setText("Has llegado hasta la fase " + String.valueOf(fase));

        // Ponemos el título, la vista y hacemos el return del builder creado
        builder.setTitle(R.string.str_customDialogEnd_juegoTerminado);
        builder.setView(view);
        return builder.create();
    }

    /** MÉTODO QUE SE ENCARGA DE DECIR QUÉ HACE CADA BOTÓN **/

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.buttonCerrar){
            listener.onCerrar();
        }
        else if(v.getId() == R.id.buttonSeguirJugando){
            listener.onSeguirJugando();
        }
    }


}
