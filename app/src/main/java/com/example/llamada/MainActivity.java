package com.example.llamada;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    EditText editTextMensaje, editTextTelefono;
    Button button;

    public static Intent intent = new Intent();

    String[] appPermissions = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_CALL_LOG
    };

    private static final int PERMISSIONS_REQUEST_CODE = 1240;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.lblMensajeEnviar);
        editTextMensaje = findViewById(R.id.txtm);
        editTextTelefono = findViewById(R.id.txtNumeroEnviar);
        button = findViewById(R.id.btnenviar);

        if (checkAndRequestPermissions()) {
            Toast.makeText(this, "T.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void btnsend(View view) {
        editTextMensaje.setError(null);
        editTextTelefono.setError(null);

        String textoEnviar = editTextMensaje.getText().toString();
        String telefonoEnviar = editTextTelefono.getText().toString();

        if ("".equals(textoEnviar)) {
            editTextMensaje.setError("Ingrese el mensaje SMS a envíar");
            editTextMensaje.requestFocus();
            return;
        }

        if ("".equals(telefonoEnviar)) {
            editTextTelefono.setError("Ingrese el mensaje SMS a envíar");
            editTextTelefono.requestFocus();
            return;
        }

        intent.putExtra("responseText", textoEnviar);
        intent.putExtra("responseTel", telefonoEnviar);
    }



    public boolean checkAndRequestPermissions() {
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String perm : appPermissions) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(perm);
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), PERMISSIONS_REQUEST_CODE);
            return false;
        }

        return true;
    }

    public AlertDialog showDialog(String title, String msg, String positiveLavel,
                                  DialogInterface.OnClickListener positiveOnClick,
                                  String negativeLavel,
                                  DialogInterface.OnClickListener negativeOnClick,
                                  boolean isCancelAble) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setCancelable(isCancelAble);
        builder.setMessage(msg);
        builder.setPositiveButton(positiveLavel, positiveOnClick);
        builder.setNegativeButton(negativeLavel, negativeOnClick);

        AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            HashMap<String, Integer> permissionResult = new HashMap<>();
            int deniedCount = 0;

            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permissionResult.put(permissions[i], grantResults[i]);
                    deniedCount++;
                }
            }

            if (deniedCount == 0) {
                Toast.makeText(this, "permisos otorgados", Toast.LENGTH_SHORT).show();
            } else {
                for (Map.Entry<String, Integer> entry : permissionResult.entrySet()) {
                    String permName = entry.getKey();
                    int permResult = entry.getValue();

                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permName)) {
                        showDialog(
                                "",
                                "Esta aplicación necesita permisos de Teléfono y Mensaje SMS para trabajar sin problemas",
                                "Si, otorgar permisos",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        checkAndRequestPermissions();
                                    }
                                },
                                "No otorgar permisos",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                },
                                false
                        );


                    } else {


                        showDialog(
                                "",
                                "Has negado algun(os) permiso(s), concede todos los permisos en Ajustes",
                                "Ir a Ajustes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(
                                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                Uri.fromParts(
                                                        "package",
                                                        getPackageName(),
                                                        null)
                                        );
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                },
                                "No, salir",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                },
                                false
                        );
                        break;
                    }
                }
            }
        }
    }
}