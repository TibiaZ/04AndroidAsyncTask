package com.example.dam2.pmm_practica_05_javiersanzrozalen;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

/***************************************************************/
/******** -->DIÁLOGO PRINCIPAL PARA INICIAR EL JUEGO<-- ********/
/***************************************************************/

public class CustomDialog extends DialogFragment implements View.OnClickListener{

    private Button btnJugar;
    private Button btnVolver;
    private EditText editTextNickname;
    private RadioButton radioButtonNovato;
    private RadioButton radioButtonMedio;
    private RadioButton radioButtonAvanzado;
    private String radioButtonElegido;
    private View view;
    private OnEmpezarJuegoListener listener;

    /** CALLBACK PARA PASAR INFORMACIÓN DEL CUSTOM DIALOG A LA ACTIVIDAD (NOMBRE/DIFICULTAD) **/

    // Método newInstance para instanciar nuestro CustomDialog
    public static CustomDialog newInstance() {
        Bundle args = new Bundle();
        CustomDialog fragment = new CustomDialog();
        fragment.setArguments(args);
        return fragment;
    }

    // Inertaz que deberemos implementar para que se la actividad se suscriba al evento del CustomDialog
    public interface OnEmpezarJuegoListener{
        void onEnviarNombre(String nickname);
        void onEnviarDificultad(String dificultad);
        void onEmpezarAJugar();
    }

    // Método para que la actividad se suscriba al evento del CustomDialog
    public void setOnEmpezarJuegoListener(OnEmpezarJuegoListener listener){
        this.listener = listener;
    }

    /** MÉTODO PARA INFLAR LOS COMPONENTES DE NUESTRO CUSTOM DIALOG Y ESTABLECER UNA VISTA (LAYOUT) **/

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Creamos el builder para nuestro custom dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Obtenemos la vista de nuestro dialog
        view = getActivity().getLayoutInflater().inflate(R.layout.activity_custom_dialog, null);

        // Inflamos nuestros widgets
        btnJugar = (Button) view.findViewById(R.id.buttonJugar);
        btnVolver = (Button) view.findViewById(R.id.buttonVolver);
        editTextNickname = (EditText) view.findViewById(R.id.editTextNickname);
        radioButtonNovato = (RadioButton) view.findViewById(R.id.radioButtonNovato);
        radioButtonMedio = (RadioButton) view.findViewById(R.id.radioButtonMedio);
        radioButtonAvanzado = (RadioButton) view.findViewById(R.id.radioButtonAvanzado);

        // Hacemos caso a nuestros botoncicos!
        btnJugar.setOnClickListener(this);
        btnVolver.setOnClickListener(this);

        // Ponemos el título a nuestro Díalog, la vista y hacemos el return del builder creado
        builder.setTitle(R.string.str_titulo_customDialog);
        builder.setView(view);
        return builder.create();
    }

    /** EVENTO ONCLICK QUE SE ENCARGARÁ DE:
     * ---> BOTÓN JUGAR : Enviará con un callback la información del nombre y dificultad
     * ---> BOTÓN VOLVER : Volveremos al MainActivity
     * **/

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.buttonJugar){
            // Definimos qué RadioButton es el que está clicado y recogemos su texto para pasarselo al método del callback
            if(radioButtonNovato.isChecked()) radioButtonElegido = radioButtonNovato.getText().toString();
            else if(radioButtonMedio.isChecked()) radioButtonElegido = radioButtonMedio.getText().toString();
            else if(radioButtonAvanzado.isChecked()) radioButtonElegido = radioButtonAvanzado.getText().toString();

            // Hcamoe uso de nuestros métodos callback
            listener.onEnviarNombre(editTextNickname.getText().toString());
            listener.onEnviarDificultad(radioButtonElegido);
            listener.onEmpezarAJugar();
            dismiss();
        }
        else if(v.getId() == R.id.buttonVolver){
            getActivity().finish();
        }
    }

}
