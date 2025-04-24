package com.example.test_hce;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    private BroadcastReceiver messageReceiverEstado = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.hasExtra("estado")) {
                String estado = intent.getStringExtra("estado");
                Log.d(TAG, "Estado recibido: " + estado);
                TextView textView = findViewById(R.id.textViewEstado);
                if (textView != null) textView.setText(estado);
            }
        }
    };

    private BroadcastReceiver messageReceiverEnviado = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.hasExtra("enviado")) {
                String enviado = intent.getStringExtra("enviado");
                Log.d(TAG, "Comando enviado: " + enviado);
                TextView textView = findViewById(R.id.textViewEnviado);
                if (textView != null) textView.setText(enviado);
            }
        }
    };

    private BroadcastReceiver messageReceiverRecibido = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.hasExtra("recibido")) {
                String recibido = intent.getStringExtra("recibido");
                Log.d(TAG, "Respuesta recibida: " + recibido);
                TextView textView = findViewById(R.id.textViewRespuesta);
                if (textView != null) textView.setText(recibido);
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            Toast.makeText(this, "Este dispositivo no soporta NFC", Toast.LENGTH_SHORT).show();
            finish(); // Cerrar la aplicación si NFC no está disponible
            return;
        }

        if (!nfcAdapter.isEnabled()) {
            Toast.makeText(this, "Por favor, habilite NFC en la configuración", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
        } else {
            Log.d(TAG, "NFC está habilitado");
        }

        // Registrar el BroadcastReceiver
        IntentFilter filterEstado = new IntentFilter("com.example.test_hce.MESSAGE_RECEIVED_ESTADO");
        registerReceiver(messageReceiverEstado, filterEstado, Context.RECEIVER_EXPORTED);

        IntentFilter filterEnviado = new IntentFilter("com.example.test_hce.MESSAGE_RECEIVED_ENVIADO");
        registerReceiver(messageReceiverEnviado, filterEnviado, Context.RECEIVER_EXPORTED);

        IntentFilter filterRecibido = new IntentFilter("com.example.test_hce.MESSAGE_RECEIVED_RECIBIDO");
        registerReceiver(messageReceiverRecibido, filterRecibido, Context.RECEIVER_EXPORTED);

        TextView textView = findViewById(R.id.textViewEstado);
        textView.setText("NFC está listo para comunicarse");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Desregistrar el BroadcastReceiver
        unregisterReceiver(messageReceiverEstado);
        unregisterReceiver(messageReceiverEnviado);
        unregisterReceiver(messageReceiverRecibido);
    }
}