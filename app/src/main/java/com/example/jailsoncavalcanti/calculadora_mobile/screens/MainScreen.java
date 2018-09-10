package com.example.jailsoncavalcanti.calculadora_mobile.screens;

import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jailsoncavalcanti.calculadora_mobile.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class MainScreen extends Screen {

    private Button buttonUsers;

    private Button callWebServiceSomar;
    private Button callWebServiceSubtrair;
    private Button callWebServiceDividir;
    private Button callWebServiceMultiplicar;

    private EditText textInputA;
    private EditText textInputB;

    //private String ipServer = "192.168.1.175";
    //private String ipServer = "192.168.0.28";
    private String ipServer = "192.168.0.21";
    private String urlServer = "/calculadora/Calculadora.asmx";

    //private TextView textViewA;
    //private TextView textViewB;
    //private TextView textViewC;
    private TextView textViewD;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        findViews();
        addListeners();

    }

    private void addListeners() {
        callWebServiceSomar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callWebService("Somar");
            }
        });
        callWebServiceSubtrair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callWebService("Subtrair");
            }
        });
        callWebServiceDividir.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainScreen.this, CalculatorScreen.class));
                callWebService("Dividir");
            }
        });
        callWebServiceMultiplicar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainScreen.this, CalculatorScreen.class));
                callWebService("Multiplicar");
            }
        });

        buttonUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MainScreen.this, UserScreen.class);
                //setCallWebServiceGetUsers();
                callWebService("Get");
            }
        });

        /*openSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainScreen.this, SearchScreen.class);
                String text = textInput.getText().toString();
                intent.putExtra( SearchScreen.DETAILS_TEXT_EXTRA, text);
                startActivity(intent);
            }
        });
        openTVSeries.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainScreen.this, ResultScreen.class));
            }
        });*/

/*        textViewD.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //openSearch.setEnabled(s.length() > 0);

            }
        });*/


    }

    private void callWebService(final String tipo){
        Thread th = new Thread() {
            String res;

            @Override
            public void run() {
                String NAMESPACE = "http://demo.android.org/";
                String URL = "http://" + ipServer + urlServer;
                String METHOD_NAME = tipo;
                //String SOAP_ACTION = "http://demo.android.org/Dividir";
                String SOAP_ACTION = "http://demo.android.org/" + tipo;

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

                if(tipo.equalsIgnoreCase("Somar") || tipo.equalsIgnoreCase("Subtrair")
                        || tipo.equalsIgnoreCase("Dividir") || tipo.equalsIgnoreCase("Multiplicar") ){

                    if(isNumeric(textInputA.getText().toString())){
                        request.addProperty("x", Integer.parseInt(textInputA.getText().toString()));
                    }else{
                        request.addProperty("x", 0);
                    }
                    if(isNumeric(textInputB.getText().toString())){
                        request.addProperty("y", Integer.parseInt(textInputB.getText().toString()));
                    }else{
                        request.addProperty("y", 0);
                    }

                    //request.addProperty("x", Integer.parseInt(textInputA.getText().toString()));
                    //request.addProperty("y", Integer.parseInt(textInputB.getText().toString()));
                }

                SoapSerializationEnvelope envelope =
                        new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                HttpTransportSE transporte = new HttpTransportSE(URL);

                try {
                    transporte.call(SOAP_ACTION, envelope);
                    SoapPrimitive resultado_xml = (SoapPrimitive) envelope.getResponse();
                    res = resultado_xml.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainScreen.this, res, Toast.LENGTH_SHORT).show();
                        textViewD.setText(res);
                    }
                });
            }
        };

        th.start();
    }

    private void findViews() {

        buttonUsers = (Button) findViewById(R.id.buttonUsers);
        callWebServiceSomar = (Button) findViewById(R.id.buttonSomar);
        callWebServiceSubtrair = (Button) findViewById(R.id.buttonSubtrair);
        callWebServiceDividir = (Button) findViewById(R.id.buttonDividir);
        callWebServiceMultiplicar = (Button) findViewById(R.id.buttonMultiplicar);

        textInputA = (EditText) findViewById(R.id.editTextA);
        textInputB = (EditText) findViewById(R.id.editTextB);

        //textViewA = (TextView) findViewById(R.id.textViewA);
        //textViewB = (TextView) findViewById(R.id.textViewB);
        //textViewC = (TextView) findViewById(R.id.textViewC);
        textViewD = (TextView) findViewById(R.id.textViewD);

    }

    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }


}
