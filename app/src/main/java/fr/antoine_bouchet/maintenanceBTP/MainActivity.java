package fr.antoine_bouchet.maintenanceBTP;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Context context;
    String idOpt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        countRecords();
        readRecords();


        Button buttonCreateTicket = (Button) findViewById(R.id.buttonCreateRequest);
        buttonCreateTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = v.getRootView().getContext();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View formElementsView = inflater.inflate(R.layout.ticket_input_form, null, false);

                final Spinner editTextTicketNiveau = (Spinner) formElementsView.findViewById(R.id.spinnerNiveau);
                final Spinner editTextTicketZone = (Spinner) formElementsView.findViewById(R.id.spinnerZone);
                final EditText editTextTicketSalle = (EditText) formElementsView.findViewById(R.id.editTextSalle);
                final EditText editTextTicketTitre = (EditText) formElementsView.findViewById(R.id.editTextTitreDemande);
                final EditText editTextTicketMateriel = (EditText) formElementsView.findViewById(R.id.editTextMateriel);
                final Spinner editTextTicketCategorie = (Spinner) formElementsView.findViewById(R.id.spinnerCategorie);
                final EditText editTextTicketProbleme = (EditText) formElementsView.findViewById(R.id.editTextProbleme);


                new AlertDialog.Builder(context)
                        .setView(formElementsView)
                        .setTitle("Réaliser une demande d'assistance")
                        .setPositiveButton("Valider",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String ticketNiveau = editTextTicketNiveau.getSelectedItem().toString();
                                        String ticketZone = editTextTicketZone.getSelectedItem().toString();
                                        String ticketSalle = editTextTicketSalle.getText().toString();
                                        String ticketTitre = editTextTicketTitre.getText().toString();
                                        String ticketMateriel = editTextTicketMateriel.getText().toString();
                                        String ticketCategorie = editTextTicketCategorie.getSelectedItem().toString();
                                        String ticketProbleme = editTextTicketProbleme.getText().toString();

                                        ObjectTicket objectTicket = new ObjectTicket();
                                        ObjectTicket.niveau = ticketNiveau;
                                        ObjectTicket.zone = ticketZone;
                                        ObjectTicket.salle = ticketSalle;
                                        ObjectTicket.titre = ticketTitre;
                                        ObjectTicket.materiel = ticketMateriel;
                                        ObjectTicket.categorie = ticketCategorie;
                                        ObjectTicket.probleme = ticketProbleme;


                                        boolean createSuccessful = new TableControllerTicket(context).create(objectTicket);
                                        if (createSuccessful) {
                                            Toast.makeText(context, "Votre demande d'assistance a bien été réalisée .",
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, "Impossible de réaliser votre demande d'assistance.",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                        countRecords();
                                        readRecords();
                                        dialog.cancel();
                                    }
                                })
                        .show();
            }
        });
        countRecords();
        readRecords();
    }

    public void countRecords() {
        int recordCount = new TableControllerTicket(this).count();

        TextView textViewRecordCount = (TextView) findViewById(R.id.textViewRecordCount);
        textViewRecordCount.setText(recordCount + " demandes trouvées");
    }

    public void readRecords() {
        LinearLayout linearLayoutRecords = (LinearLayout) findViewById(R.id.linearLayoutRecords);
        linearLayoutRecords.removeAllViews();
        List<ObjectTicket> ticket = new TableControllerTicket(this).read();
        if (ticket.size() > 0) {
            for (ObjectTicket obj : ticket) {
                int id = obj.id;
                String ticketNiveau = obj.niveau;
                String ticketZone = obj.zone;
                String ticketSalle = obj.salle;
                String ticketTitre = obj.titre;
                String ticketMateriel = obj.materiel;
                String ticketCategorie = obj.categorie;
                String ticketProbleme = obj.probleme;

                String textViewContents = ticketTitre + " - " + ticketCategorie;
                TextView textViewTicketItem = new TextView(this);
                textViewTicketItem.setPadding(0, 10, 0, 10);
                textViewTicketItem.setText(textViewContents);
                textViewTicketItem.setTag(Integer.toString(id));
                textViewTicketItem.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        context = v.getContext();
                        idOpt = v.getTag().toString();

                        final CharSequence[] items = {"Visionner","Editer", "Supprimer"};
                        new AlertDialog.Builder(context).setTitle("Demande enregistrée")
                                .setItems(items, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int item) {
                                        if (item == 0) {
                                            showRecord(Integer.parseInt(idOpt));
                                        }else if (item == 1){
                                            editRecord(Integer.parseInt(idOpt));
                                        }else if (item == 2) {
                                            boolean deleteSuccessful = new TableControllerTicket(context).delete(Integer.parseInt(idOpt));
                                            if (deleteSuccessful){
                                                Toast.makeText(context, "La demande a bien été supprimée.",
                                                        Toast.LENGTH_SHORT).show();
                                            }else{
                                                Toast.makeText(context, "Impossible de supprimer cette demande !",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                            countRecords();
                                            readRecords();
                                        }

                                        dialog.dismiss();
                                    }
                                }).show();

                        return false;
                    }
                });
                linearLayoutRecords.addView(textViewTicketItem);
            }
        } else {
            TextView locationItem = new TextView(this);
            locationItem.setPadding(8, 8, 8, 8);
            locationItem.setText("Aucune demande pour le moment !");
            linearLayoutRecords.addView(locationItem);
        }
    }

    private void editRecord(final int ticketId) {
        final TableControllerTicket tableControllerTicket = new TableControllerTicket(context);

        ObjectTicket objectTicket = tableControllerTicket.readSingleRecord(ticketId);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.ticket_input_form, null, false);

        final Spinner editTextTicketNiveau = (Spinner) formElementsView.findViewById(R.id.spinnerNiveau);
        final Spinner editTextTicketZone = (Spinner) formElementsView.findViewById(R.id.spinnerZone);
        final EditText editTextTicketSalle = (EditText) formElementsView.findViewById(R.id.editTextSalle);
        final EditText editTextTicketTitre = (EditText) formElementsView.findViewById(R.id.editTextTitreDemande);
        final EditText editTextTicketMateriel = (EditText) formElementsView.findViewById(R.id.editTextMateriel);
        final Spinner editTextTicketCategorie = (Spinner) formElementsView.findViewById(R.id.spinnerCategorie);
        final EditText editTextTicketProbleme = (EditText) formElementsView.findViewById(R.id.editTextProbleme);

        String compareValueNiveau = "some value";
        ArrayAdapter<CharSequence> adapterNiveau = ArrayAdapter.createFromResource(this, R.array.niveau, android.R.layout.simple_spinner_item);
        adapterNiveau.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editTextTicketNiveau.setAdapter(adapterNiveau);
        if (compareValueNiveau != null) {
            int spinnerPosition = adapterNiveau.getPosition(compareValueNiveau);
            editTextTicketNiveau.setSelection(spinnerPosition);
        }
        String compareValueZone = "some value";
        ArrayAdapter<CharSequence> adapterZone = ArrayAdapter.createFromResource(this, R.array.zone, android.R.layout.simple_spinner_item);
        adapterZone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editTextTicketZone.setAdapter(adapterZone);
        if (compareValueZone != null) {
            int spinnerPosition = adapterZone.getPosition(compareValueZone);
            editTextTicketZone.setSelection(spinnerPosition);
        }
        editTextTicketSalle.setText(ObjectTicket.salle);
        editTextTicketTitre.setText(ObjectTicket.titre);
        editTextTicketMateriel.setText(ObjectTicket.materiel);
        String compareValueCategorie = "some value";
        ArrayAdapter<CharSequence> adapterCategorie = ArrayAdapter.createFromResource(this, R.array.categorie, android.R.layout.simple_spinner_item);
        adapterCategorie.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editTextTicketCategorie.setAdapter(adapterCategorie);
        if (compareValueCategorie != null) {
            int spinnerPosition = adapterZone.getPosition(compareValueCategorie);
            editTextTicketCategorie.setSelection(spinnerPosition);
        }
        editTextTicketProbleme.setText(ObjectTicket.probleme);


        new AlertDialog.Builder(context)
                .setView(formElementsView)
                .setTitle("Modifier une demande")
                .setPositiveButton("Modifier",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ObjectTicket objectTicket = new ObjectTicket();
                                objectTicket.id = ticketId;
                                String ticketNiveau = editTextTicketNiveau.getSelectedItem().toString();
                                String ticketZone = editTextTicketZone.getSelectedItem().toString();
                                String ticketSalle = editTextTicketSalle.getText().toString();
                                String ticketTitre = editTextTicketTitre.getText().toString();
                                String ticketMateriel = editTextTicketMateriel.getText().toString();
                                String ticketCategorie = editTextTicketCategorie.getSelectedItem().toString();
                                String ticketProbleme = editTextTicketProbleme.getText().toString();

                                boolean updateSuccessful = tableControllerTicket.update(objectTicket);
                                if (updateSuccessful) {
                                    Toast.makeText(context, "La demande a bien été modifiée !", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Impossible de mettre a jour cette demande !",
                                            Toast.LENGTH_SHORT).show();
                                }
                                countRecords();
                                readRecords();
                                dialog.cancel();
                            }
                        }).show();
        countRecords();
        readRecords();
    }

    private void showRecord(final int ticketId) {
        final TableControllerTicket tableControllerTicket = new TableControllerTicket(context);

        ObjectTicket objectTicket = tableControllerTicket.readSingleRecord(ticketId);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.ticket_input_form, null, false);

        final Spinner editTextTicketNiveau = (Spinner) formElementsView.findViewById(R.id.spinnerNiveau);
        final Spinner editTextTicketZone = (Spinner) formElementsView.findViewById(R.id.spinnerZone);
        final EditText editTextTicketSalle = (EditText) formElementsView.findViewById(R.id.editTextSalle);
        final EditText editTextTicketTitre = (EditText) formElementsView.findViewById(R.id.editTextTitreDemande);
        final EditText editTextTicketMateriel = (EditText) formElementsView.findViewById(R.id.editTextMateriel);
        final Spinner editTextTicketCategorie = (Spinner) formElementsView.findViewById(R.id.spinnerCategorie);
        final EditText editTextTicketProbleme = (EditText) formElementsView.findViewById(R.id.editTextProbleme);

        new AlertDialog.Builder(context)
                .setView(formElementsView)
                .setTitle("Visualisation d'une demande")
                .setPositiveButton("Retour",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ObjectTicket objectTicket = new ObjectTicket();
                                objectTicket.id = ticketId;
                                String ticketNiveau = editTextTicketNiveau.getSelectedItem().toString();
                                String ticketZone = editTextTicketZone.getSelectedItem().toString();
                                String ticketSalle = editTextTicketSalle.getText().toString();
                                String ticketTitre = editTextTicketTitre.getText().toString();
                                String ticketMateriel = editTextTicketMateriel.getText().toString();
                                String ticketCategorie = editTextTicketCategorie.getSelectedItem().toString();
                                String ticketProbleme = editTextTicketProbleme.getText().toString();



                                countRecords();
                                readRecords();
                                dialog.cancel();
                            }
                        }).show();
        countRecords();
        readRecords();
    }
}