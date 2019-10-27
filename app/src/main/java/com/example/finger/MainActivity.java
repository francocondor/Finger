package com.example.finger;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Executor executor = Executors.newSingleThreadExecutor();

        final BiometricPrompt biometricPrompt = new BiometricPrompt.Builder(this)
                .setTitle("Fingerprint authentication")
                .setSubtitle("You have an exam!")
                .setDescription("If we validate your fingerprint, your exam will start")
                .setNegativeButton("Cancel", executor, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).build();

        final CardView authenticate = findViewById(R.id.authenticate);

        final MainActivity activity = this;

        authenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(new CancellationSignal(), executor, new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(MainActivity.this, "Activated", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, ExamActivity.class);
                                startActivityForResult(intent, 0);
                            }
                        });
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "Unrecognized fingerprint", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

        authenticate.setVisibility(View.GONE);

        new android.os.Handler().postDelayed(
            new Runnable() {
                public void run() {
                    authenticate.setVisibility(View.VISIBLE);
                }
            }, 3000);
    }
}
