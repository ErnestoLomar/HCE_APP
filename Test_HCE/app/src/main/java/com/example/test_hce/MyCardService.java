package com.example.test_hce;

import android.content.Intent;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

    @Override
    public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {

        Toast.makeText(this, "Hay comunicacion", Toast.LENGTH_SHORT).show();

        String respuesta = hexToString(bytesToHex(commandApdu));

        boolean chida = isPrintable(respuesta);

        if (chida){
            //Toast.makeText(this, String.format("Recibido de raspberry: %s", respuesta), Toast.LENGTH_SHORT).show();

            Intent intentEstado = new Intent("com.example.test_hce.MESSAGE_RECEIVED_ESTADO");
            intentEstado.putExtra("estado", "Comunicado");
            sendBroadcast(intentEstado);

            Intent intentEnviado = new Intent("com.example.test_hce.MESSAGE_RECEIVED_ENVIADO");
            intentEnviado.putExtra("enviado", "ENVIADO");
            sendBroadcast(intentEnviado);

            Intent intentRecibido = new Intent("com.example.test_hce.MESSAGE_RECEIVED_RECIBIDO");
            intentRecibido.putExtra("recibido", respuesta);
            sendBroadcast(intentRecibido);
        }

        Log.d(TAG, "Received APDU: " + bytesToHex(commandApdu));

        if (isSelectApdu(commandApdu)) {
            //Toast.makeText(this, "Respondiendo OK", Toast.LENGTH_SHORT).show();
            return SELECT_RESPONSE_OK;
        }

        return new byte[]{};
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
        for (char c : str.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && !Character.isWhitespace(c) &&
                    !Character.isISOControl(c) && c != ' ') {
                return false;
            }
        }
        return true;
    }
}