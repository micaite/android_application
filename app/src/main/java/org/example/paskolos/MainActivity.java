package org.example.paskolos;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import org.example.paskolos.model.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<VienoMenDuomenys> visiDuomenys;
    private List<VienoMenDuomenys> anuitetoDuomenys;
    private List<VienoMenDuomenys> linijinioDuomenys;
    private MokejimuAdapter adapter;

    private LocalDate pradziosData;
    private LocalDate atidejimoPradzia;
    private LocalDate filtrasNuo;
    private LocalDate filtrasIki;

    private Button btnPradziosData, btnAtidejimoPradzia, btnFiltrasNuo, btnFiltrasIki;
    private TextView tvKlaida;
    private Spinner spinnerTipas;
    private LineChart grafikas;
    private RecyclerView recyclerView;

    private com.google.android.material.textfield.TextInputEditText etSuma, etMetai,
            etMenesiai, etProcentas, etAtidejimoTrukme, etAtidejimoProcentas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        etSuma = findViewById(R.id.etSuma);
        etMetai = findViewById(R.id.etMetai);
        etMenesiai = findViewById(R.id.etMenesiai);
        etProcentas = findViewById(R.id.etProcentas);
        etAtidejimoTrukme = findViewById(R.id.etAtidejimoTrukme);
        etAtidejimoProcentas = findViewById(R.id.etAtidejimoProcentas);
        tvKlaida = findViewById(R.id.tvKlaida);
        spinnerTipas = findViewById(R.id.spinnerTipas);
        grafikas = findViewById(R.id.grafikas);
        recyclerView = findViewById(R.id.recyclerView);

        btnPradziosData = findViewById(R.id.btnPradziosData);
        btnAtidejimoPradzia = findViewById(R.id.btnAtidejimoPradzia);
        btnFiltrasNuo = findViewById(R.id.btnFiltrasNuo);
        btnFiltrasIki = findViewById(R.id.btnFiltrasIki);


        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Anuitetas", "Linijinis"});
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipas.setAdapter(spinnerAdapter);


        adapter = new MokejimuAdapter(new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        btnPradziosData.setOnClickListener(v -> rodytDatePicker(data -> {
            pradziosData = data;
            btnPradziosData.setText(data.toString());
        }));

        btnAtidejimoPradzia.setOnClickListener(v -> rodytDatePicker(data -> {
            atidejimoPradzia = data;
            btnAtidejimoPradzia.setText(data.toString());
        }));

        btnFiltrasNuo.setOnClickListener(v -> rodytDatePicker(data -> {
            filtrasNuo = data;
            btnFiltrasNuo.setText(data.toString());
        }));

        btnFiltrasIki.setOnClickListener(v -> rodytDatePicker(data -> {
            filtrasIki = data;
            btnFiltrasIki.setText(data.toString());
        }));


        findViewById(R.id.btnSkaiciuoti).setOnClickListener(v -> skaiciuoti());
        findViewById(R.id.btnFiltruoti).setOnClickListener(v -> filtruoti());
        findViewById(R.id.btnEksportuoti).setOnClickListener(v -> eksportuoti());
    }

    private void rodytDatePicker(DateCallback callback) {
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) -> {
            LocalDate data = LocalDate.of(year, month + 1, day);
            callback.onDate(data);
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    interface DateCallback {
        void onDate(LocalDate data);
    }

    private void skaiciuoti() {
        android.util.Log.d("PASKOLA", "Skaiciuoti paleistas");

        try {
            tvKlaida.setVisibility(View.GONE);


            android.util.Log.d("PASKOLA", "Suma: " + etSuma.getText().toString());
            android.util.Log.d("PASKOLA", "Data: " + pradziosData);

            double suma = Double.parseDouble(etSuma.getText().toString().replace(",", "."));
            int metai = Integer.parseInt(etMetai.getText().toString());
            int menesiai = Integer.parseInt(etMenesiai.getText().toString());
            double proc = Double.parseDouble(etProcentas.getText().toString().replace(",", "."));
            String tipas = spinnerTipas.getSelectedItem().toString();

            android.util.Log.d("PASKOLA", "Parsinta suma: " + suma);

            if (pradziosData == null) {
                android.util.Log.d("PASKOLA", "DATA NULL!");
                rodytKlaida("Pasirinkite pradžios datą!");
                return;
            }

            Paskola paskola;
            if ("Anuitetas".equals(tipas)) {
                paskola = new AnuitetinePaskola(suma, proc, metai, menesiai);
            } else {
                paskola = new LinijinePaskola(suma, proc, metai, menesiai);
            }


            android.util.Log.d("PASKOLA", "Paskola sukurta: " + tipas);

            if (atidejimoPradzia != null &&
                    etAtidejimoTrukme.getText() != null &&
                    !etAtidejimoTrukme.getText().toString().isEmpty()) {
                int trukme = Integer.parseInt(etAtidejimoTrukme.getText().toString());
                double atidProc = Double.parseDouble(etAtidejimoProcentas.getText().toString().replace(",", "."));
                paskola.setAtidejimas(new Atidejimas(atidejimoPradzia, trukme, atidProc));
            }

            visiDuomenys = paskola.generuotiGrafika(pradziosData);
            android.util.Log.d("PASKOLA", "Grafikas sugeneruotas: " + visiDuomenys.size() + " menesiu");
            adapter.atnaujinti(visiDuomenys);
            recyclerView.setVisibility(View.VISIBLE);
            findViewById(R.id.lentelesAntraste).setVisibility(View.VISIBLE);

            AnuitetinePaskola anuitetas = new AnuitetinePaskola(suma, proc, metai, menesiai);
            LinijinePaskola linijinis = new LinijinePaskola(suma, proc, metai, menesiai);
            anuitetoDuomenys = anuitetas.generuotiGrafika(pradziosData);
            linijinioDuomenys = linijinis.generuotiGrafika(pradziosData);
            rodytiGrafika();

        } catch (NumberFormatException e) {
            android.util.Log.d("PASKOLA", "NumberFormatException: " + e.getMessage());
            rodytKlaida("Įveskite tik skaičius!");
        } catch (Exception e) {
            android.util.Log.d("PASKOLA", "Klaida: " + e.getMessage());
            rodytKlaida("Klaida: " + e.getMessage());
        }
    }

    private void rodytiGrafika() {
        List<Entry> anuitetoTaskai = new ArrayList<>();
        List<Entry> linijinioTaskai = new ArrayList<>();

        for (int i = 0; i < Math.min(anuitetoDuomenys.size(), linijinioDuomenys.size()); i++) {
            anuitetoTaskai.add(new Entry(i + 1, (float) anuitetoDuomenys.get(i).getImoka()));
            linijinioTaskai.add(new Entry(i + 1, (float) linijinioDuomenys.get(i).getImoka()));
        }

        LineDataSet anuitetoLinija = new LineDataSet(anuitetoTaskai, "Anuitetas");
        anuitetoLinija.setColor(0xFF2196F3);
        anuitetoLinija.setDrawCircles(false);

        LineDataSet linijinioLinija = new LineDataSet(linijinioTaskai, "Linijinis");
        linijinioLinija.setColor(0xFFFF5722);
        linijinioLinija.setDrawCircles(false);

        grafikas.setData(new LineData(anuitetoLinija, linijinioLinija));


        grafikas.getXAxis().setPosition(com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM);
        grafikas.getXAxis().setGranularity(1f);
        grafikas.getXAxis().setLabelCount(6);


        grafikas.getAxisLeft().setAxisMinimum(0f);
        grafikas.getAxisRight().setEnabled(false);


        grafikas.getDescription().setEnabled(false);
        grafikas.setTouchEnabled(true);
        grafikas.setPinchZoom(true);


        grafikas.invalidate();
        grafikas.setVisibility(View.VISIBLE);
    }

    private void filtruoti() {
        if (visiDuomenys == null) {
            rodytKlaida("Pirma paspauskite Skaičiuoti!");
            return;
        }
        if (filtrasNuo == null || filtrasIki == null) {
            rodytKlaida("Pasirinkite filtro datas!");
            return;
        }
        List<VienoMenDuomenys> filtruoti = new ArrayList<>();
        for (VienoMenDuomenys e : visiDuomenys) {
            if (!e.getData().isBefore(filtrasNuo) && !e.getData().isAfter(filtrasIki)) {
                filtruoti.add(e);
            }
        }
        adapter.atnaujinti(filtruoti);
    }

    private void eksportuoti() {
        if (visiDuomenys == null) {
            rodytKlaida("Pirma paspauskite Skaičiuoti!");
            return;
        }
        try {
            File failas = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "ataskaita.csv");
            new Ataskaita().eksportuotiCSV(visiDuomenys, failas.getAbsolutePath());
            tvKlaida.setTextColor(0xFF00AA00);
            tvKlaida.setText("Eksportuota: " + failas.getAbsolutePath());
            tvKlaida.setVisibility(View.VISIBLE);
        } catch (IOException e) {
            rodytKlaida("Klaida eksportuojant!");
        }
    }

    private void rodytKlaida(String tekstas) {
        tvKlaida.setTextColor(0xFFFF0000);
        tvKlaida.setText(tekstas);
        tvKlaida.setVisibility(View.VISIBLE);
    }
}