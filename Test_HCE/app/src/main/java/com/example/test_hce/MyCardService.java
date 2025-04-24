package com.example.test_hce;

import android.content.Intent;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.Arrays;

public class MyCardService extends HostApduService {

    private static final String TAG = "MyCardService";
    private static final byte[] SELECT_APDU_HEADER = {
            (byte)0x00, // CLA
            (byte)0xA4, // INS
            (byte)0x04, // P1
            (byte)0x00  // P2
    };

    private static final byte[] SELECT_RESPONSE_OK = {
            (byte)0x90, // SW1: Command successfully executed (OK).
            (byte)0x00  // SW2: Command successfully executed (OK).
    };

    private boolean appSelected = false;

    @Override
    public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {

        Log.d(TAG, "APDU recibido: " + bytesToHex(commandApdu));

        // Verifica si es SELECT AID
        if (isSelectApdu(commandApdu)) {
            appSelected = true; // Activamos el modo de recepción
            Log.d(TAG, "SELECT AID recibido, enviando OK...");
            Toast.makeText(this, "SELECT AID recibido, enviando OK...", Toast.LENGTH_SHORT).show();
            return SELECT_RESPONSE_OK;
        }

        if (appSelected) {
            String mensajeRecibido = hexToString(bytesToHex(commandApdu));
            Log.d(TAG, "Mensaje recibido: " + mensajeRecibido);
            //Toast.makeText(this, "Mensaje recibido: " + mensajeRecibido, Toast.LENGTH_SHORT).show();

            Intent intentEstado = new Intent("com.example.test_hce.MESSAGE_RECEIVED_ESTADO");
            intentEstado.putExtra("estado", "Comunicado");
            sendBroadcast(intentEstado);

            Intent intentEnviado = new Intent("com.example.test_hce.MESSAGE_RECEIVED_ENVIADO");
            intentEnviado.putExtra("enviado", "ENVIADO");
            sendBroadcast(intentEnviado);

            Intent intentRecibido = new Intent("com.example.test_hce.MESSAGE_RECEIVED_RECIBIDO");
            intentRecibido.putExtra("recibido", mensajeRecibido);
            sendBroadcast(intentRecibido);

            String mensajeDeRegreso = "Hola desde App.";
            //Toast.makeText(this, "Mensaje de regreso: " + mensajeDeRegreso, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Mensaje de regreso: " + mensajeDeRegreso);
            return mensajeDeRegreso.getBytes();
        }

        Toast.makeText(this, "No se ha recibido SELECT", Toast.LENGTH_SHORT).show();
        // Si no se ha recibido SELECT, no hacemos nada
        return new byte[] {(byte)0x6A, (byte)0x82}; // File not found
    }

    @Override
    public void onDeactivated(int reason) {
        //Toast.makeText(this, "Desactivado", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Deactivated: " + reason);
    }

    private boolean isSelectApdu(byte[] apdu) {
        if (apdu == null || apdu.length < SELECT_APDU_HEADER.length) {
            return false;
        }

        for (int i = 0; i < SELECT_APDU_HEADER.length; i++) {
            if (apdu[i] != SELECT_APDU_HEADER[i]) {
                return false;
            }
        }

        return true;
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    private String hexToString(String hex) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < hex.length(); i += 2) {
            String str = hex.substring(i, i + 2);
            sb.append((char) Integer.parseInt(str, 16));
        }

        return sb.toString();
    }

    private boolean isPrintable(String str) {
        return str.matches("\\A\\p{Print}*\\z");
    }
}