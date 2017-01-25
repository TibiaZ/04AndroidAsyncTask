package com.example.dam2.pmm_practica_05_javiersanzrozalen;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

/***************************************************************/
/*************** -->CLASE PRINCIPAL DEL JUEGO<-- ***************/
/***************************************************************/

public class GameActivity extends AppCompatActivity implements View.OnClickListener, CustomDialog.OnEmpezarJuegoListener{

    /** VARIABLES Y OBJETOS DE CLASE **/

    // Objetos de clase para nuestros widgets
    private TextView tvNickname;
    private TextView tvFase;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private ProgressBar progressBar;
    private TextView tvPorcentaje;

    // Objetos para nuestros Dialogs personalizados
    private CustomDialog dialogInicial;
    private String dificultadRecibida;

    // Variables de clase para el manejo y flujo de datos
    private int posicionInicial = 0;
    private int tiempoSleep = 100;
    private int tiempoSleepReiniciar = 0;
    private int contadorBotones = 1;
    private int contadorFase = 0;
    private ArrayList<Button> arrayBotones;
    private ArrayList<Integer> arrayEnteros;
    private Tarea miTarea;


    /** MÉTODO DEL CICLO DE VIDA DE GameActivity DONDE INFLAMOS NUESTROS WIDGETS **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Instancio el dialogo customizado inicial para elegir nickname y dificultad.
        dialogInicial = CustomDialog.newInstance();
        dialogInicial.show(getFragmentManager(), null);
        dialogInicial.setCancelable(false);
        dialogInicial.setOnEmpezarJuegoListener(this);

        // Inflamos nuestros widgets
        tvNickname = (TextView) findViewById(R.id.textViewNickname);
        tvFase = (TextView) findViewById(R.id.textViewFase);
        btn1 = (Button) findViewById(R.id.button1);
        btn2 = (Button) findViewById(R.id.button2);
        btn3 = (Button) findViewById(R.id.button3);
        btn4 = (Button) findViewById(R.id.button4);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tvPorcentaje = (TextView) findViewById(R.id.textViewPorcentaje);

        // Escuchamos a nuestros botones
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);

        // Añadimos nuestros botones al array de botones
        arrayBotones = new ArrayList<Button>(){{
            add(btn1);
            add(btn2);
            add(btn3);
            add(btn4);
        }};

        // Añadimos los enteros al array de números enteros para desordenarlos posteriormente
        arrayEnteros = new ArrayList<>();
        Collections.addAll(arrayEnteros, 1, 2, 3, 4);
    }


    /** MÉTODOS DE LA INTERFAZ DEL CustomDialog
     * SE ENCARGAN DE ENVIAR EL NOMBRE Y LA DIFICULTAD A LA ACTIVITY DESDE EL CustomDialog**/

    // Hacemos uso del Callback del CustomDialog para poner el nombre y dificultad
    @Override
    public void onEnviarNombre(String nickname) {
        tvNickname.setText(nickname);
    }

    @Override
    public void onEnviarDificultad(String dificultad) {
        this.dificultadRecibida = dificultad;
    }

    @Override
    public void onEmpezarAJugar() {
        switch (dificultadRecibida){
            case "Novato": tiempoSleep = 130 ;break;
            case "Medio": tiempoSleep = 70 ;break;
            case "Avanzado": tiempoSleep = 35 ;break;
        }
        // Guardamos el valor inicial del tiempo de sueño por si decidimos volver a jugar en el diálogo final
        tiempoSleepReiniciar = tiempoSleep;
        iniciarTareaSecundaria();
    }


    /** MÉTODO onClick QUE CAPTURARÁ QUÉ BOTÓN HA SIDO CLICADO **/

    @Override
    public void onClick(View v) {
        Button botonPresionado = (Button) v;

        if(Integer.parseInt(botonPresionado.getText().toString()) == contadorBotones){
            botonPresionado.setEnabled(false);
            contadorBotones++;
            if(contadorBotones == 5){
                miTarea.cancel(false);
            }
        }
    }


    /** Método que iniciará nuestra tarea asíncrona y pondrá nuestra dificultad
     * a un número mayor o menor dependiendo de lo que hayamos seleccionado en el CustomDialog
     */

    private void iniciarTareaSecundaria(){
        posicionInicial = 0;
        miTarea = new Tarea();
        miTarea.execute(tiempoSleep, posicionInicial);
    }

    /** Método que recibe un array de enteros, los desordena y los coloca en el texto de los botones **/

    private void numerarBotones(ArrayList<Integer> arrayEnteros){
        Collections.shuffle(arrayEnteros);
        for(int i = 0; i < arrayEnteros.size(); i++){
            arrayBotones.get(i).setEnabled(true);
            arrayBotones.get(i).setText(String.valueOf(arrayEnteros.get(i)));
        }
    }


    /** Método para activar todos los botones **/

    private void activarBotones(){
        for(int i = 0; i < arrayBotones.size(); i++){
            arrayBotones.get(i).setEnabled(true);
        }
    }


    /***************************************************************/
    /*** CLASE INTERNA PARA EL MANEJO DE LOS HILOS CON AsyncTask ***/
    /***************************************************************/

    private class Tarea extends AsyncTask<Integer, Integer, Integer> implements CuestomDialogEnd.OnJuegoFinalizado{

        private CuestomDialogEnd dialogEnd;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Activamos botones y ponemos el progressbar y el texview del porcentaje a 0
            activarBotones();

            // Ponemos nuestro progress bar a 0 en el hilo principal
            progressBar.setProgress(0);

            // Actualizo el TextView del porcentaje al inicial el nivel
            tvPorcentaje.setText("0/100");
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            int i;
            for(i = params[1]; i <= 100; i++){
                // Hago un commit de las modificaciones de la UI
                publishProgress(i);

                // Duermo el hilo
                SystemClock.sleep(params[0]);

                // Compruebo si se ha cancelado ->  Entonces salimos del bucle
                if(isCancelled()){
                    break;
                }
            }
            return i;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int porcentaje = values[0];

            // Actualizo la barra de progreso en el hilo principal
            progressBar.setProgress(porcentaje);

            // Actualizo el textView del porcentaje en el hilo principal
            tvPorcentaje.setText(String.valueOf(porcentaje) + "/100");
        }

        @Override
        protected void onPostExecute(Integer integer) {
            // Iniciamos el Dialog customizado final con los resultados
            progressBar.setEnabled(false);
            dialogEnd = CuestomDialogEnd.newInstance(tvNickname.getText().toString(), contadorFase);
            dialogEnd.show(getSupportFragmentManager(), null);
            dialogEnd.setCancelable(false);
            dialogEnd.setOnJuegoFinalizado(this);

            Toast.makeText(getApplicationContext(),
                    "Juego finalizado en dificultad: " + dificultadRecibida,
                    Toast.LENGTH_LONG)
                    .show();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            // Aumentamos el contador de la fase, reseteamos el contador de botones y reducimos el tiempo sleep del hilo
            // Ponemos en el text view el número de la fase por la que vamos y volvemos a iniciar la tarea
            contadorFase++;
            contadorBotones = 1;
            tiempoSleep -= 15;
            tvFase.setText("Fase " + String.valueOf(contadorFase));
            numerarBotones(arrayEnteros);
            iniciarTareaSecundaria();
        }

        /** MÉTODOS IMPLEMENTADOS DE LA INTERFAZ DE NUESTRO CustomDialogEnd **/

        @Override
        public void onCerrar() {
            finish();
        }

        @Override
        public void onSeguirJugando() {
            // Cerramos el diálogo, reseteamos el contador de fase, contador de botones y el tiempo de sueño
            dialogEnd.dismiss();
            contadorFase = 0;
            contadorBotones = 1;
            tiempoSleep = tiempoSleepReiniciar;
            tvFase.setText("Fase " + String.valueOf(contadorFase));
            iniciarTareaSecundaria();
        }
    }

}
