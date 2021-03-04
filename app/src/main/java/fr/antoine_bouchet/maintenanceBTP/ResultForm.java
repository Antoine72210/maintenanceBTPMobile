package fr.antoine_bouchet.maintenanceBTP;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ResultForm extends AppCompatActivity {
    Button btOk, btCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_form);

            String str1 = "";
            String str2 = "";
            String str3 = "";
            String str4 = "";
            String str5 = "";
            String str6 = "";
            String str7 = "";

            List<ObjectTicket> ticket = new TableControllerTicket(this).read();
            for (ObjectTicket obj : ticket) {
                int id = obj.id;
                String ticketNiveau = obj.niveau;
                String ticketZone = obj.zone;
                String ticketSalle = obj.salle;
                String ticketTitre = obj.titre;
                String ticketMateriel = obj.materiel;
                String ticketCategorie = obj.categorie;
                String ticketProbleme = obj.probleme;

                TextView textView1 = (TextView) findViewById(R.id.niveauResult);
                textView1.setText(ticketNiveau);

                TextView textView2 = (TextView) findViewById(R.id.zoneResult);
                textView2.setText(ticketZone);

                TextView textView3 = (TextView) findViewById(R.id.salleResult);
                textView3.setText(ticketSalle);

                TextView textView4 = (TextView) findViewById(R.id.titreResult);
                textView4.setText(ticketTitre);

                TextView textView5 = (TextView) findViewById(R.id.materielResult);
                textView5.setText(ticketMateriel);

                TextView textView6 = (TextView) findViewById(R.id.categorieResult);
                textView6.setText(ticketCategorie);

                TextView textView7 = (TextView) findViewById(R.id.problemeResult);
                textView7.setText(ticketProbleme);


                btOk = findViewById(R.id.bt_ok);
                btCancel = findViewById(R.id.bt_cancel);
            }

                btOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ResultForm.this, MainActivity.class);
                        startActivity(intent);
                    }
                });

                btCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ResultForm.this, MainActivity.class);
                        startActivity(intent);
                    }
                });


        }
    }
