package com.example.aerogearcrypto.aerogearcryptotest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.jboss.aerogear.AeroGearCrypto;
import org.jboss.aerogear.crypto.CryptoBox;
import org.jboss.aerogear.crypto.RandomUtils;
import org.jboss.aerogear.crypto.password.Pbkdf2;
import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;
import java.security.spec.InvalidKeySpecException;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();
    TextView txtSmall;
    TextView txtBig;
    TextView txtHuge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Security.addProvider(new BouncyCastleProvider());


        txtSmall = (TextView) findViewById(R.id.txtSmall);
        txtBig = (TextView) findViewById(R.id.txtBig);
        txtHuge = (TextView) findViewById(R.id.txtHuge);
    }

    public void onEncryptSmall(View view) {
        Log.i(TAG, "onEncryptSmall");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    long start = System.currentTimeMillis();

                    byte[] decrypt = encrypt("hello, this is test message".getBytes());
                    final long stop = System.currentTimeMillis() - start;
                    Log.i(TAG, "onEncryptSmall decrypt result " + new String(decrypt));
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtSmall.setText(" Duration (ms) " + stop);
                        }
                    });
                } catch (Exception e) {
                    Log.e(MainActivity.class.getSimpleName(), "can not read file", e);
                }
            }
        }).start();
    }

    public void onEncryptBig(View view) {
        Log.i(TAG, "onEncryptBig");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    int size = 10 * 1024 * 1024;

                    byte[] data = new byte[size];

                    for (int i = 0; i < size; i++) {
                        data[i] = (byte) i;
                    }


                    long start = System.currentTimeMillis();

                    encrypt(data);
                    final long stop = System.currentTimeMillis() - start;
                    Log.i(TAG, "onEncryptSmall decrypt result ");
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtBig.setText(" Duration (ms) " + stop);
                        }
                    });
                } catch (Exception e) {
                    Log.e(MainActivity.class.getSimpleName(), "can not read file", e);
                }
            }
        }).start();
    }

    public void onEncryptHuge(View view) {
        Log.i(TAG, "onEncryptHuge");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    int size = 50 * 1024 * 1024;

                    byte[] data = new byte[size];

                    for (int i = 0; i < size; i++) {
                        data[i] = (byte) i;
                    }


                    long start = System.currentTimeMillis();

                    encrypt(data);
                    final long stop = System.currentTimeMillis() - start;
                    Log.i(TAG, "onEncryptSmall decrypt result ");
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtHuge.setText(" Duration (ms) " + stop);
                        }
                    });
                } catch (Exception e) {
                    Log.e(MainActivity.class.getSimpleName(), "can not read file", e);
                }
            }
        }).start();
    }

    private byte[] encrypt(byte[] message) throws InvalidKeySpecException {

        //Generate the key
        Pbkdf2 pbkdf2 = AeroGearCrypto.pbkdf2();
        byte[] privateKey = pbkdf2.encrypt("passphrase");

        //Initializes the crypto box
        CryptoBox cryptoBox = new CryptoBox(privateKey);

        //Encryption
        byte[] IV =  RandomUtils.randomBytes();
        byte[] ciphertext = cryptoBox.encrypt(IV, message);

        //Decryption
        CryptoBox pandora = new CryptoBox(privateKey);
        byte[] messageResult = pandora.decrypt(IV, ciphertext);

        return messageResult;
    }
}
