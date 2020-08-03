package com.give.android_fisheries_2.farmer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import com.give.android_fisheries_2.R;
import com.give.android_fisheries_2.adapter.DocumentListAdapter;
import com.give.android_fisheries_2.adapter.RecyclerItemClickListener;

public class DocumentsActivity extends AppCompatActivity {

    String[] documentTitles;
    String[] documentLinks;
    DocumentListAdapter documentListAdapter;
    RecyclerView recyclerViewHandbook;
    String googleDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);

        googleDoc ="http://docs.google.com/gview?embedded=true&url=";

        documentTitles = new String[] {
                " Ar leh Sangha khawipawlh (Poultry cum Fish farming)",
                "Buh leh Sangha Khawipawlh (Paddy-cum-Fish Culture)",
                "Common Carp hlawhtling taka tuitir tura a puitling thlan dan leh chawm dan",
                "Injection hmanga Indian Major Carp and Exotic Carp tuitir dan (Hypophysation of Indian Major Carp)",
                "Mono-Culture of Giant Fresh Water Prawn",
                "Training Manual - Value Fish Production from fresh water fish",
                "Ebook",
                "Pradhan Mantri Matsya Sampada Yojana(PMMSY)",
                "New Economic Development Policy (NEDP)",
                "Rashtriya Krishi Vikas Yojana (RKVY) (CSS) "};

        documentLinks = new String[] {
                "https://fisheries.mizoram.gov.in/page/ar-leh-sangha-khawipawlh-poultry-cum-fish-farming-",
                "https://fisheries.mizoram.gov.in/page/buh-leh-sangha-khawipawlh",
                "https://fisheries.mizoram.gov.in/page/common-carp-hlawhtling-taka-tuitir-tura-a-puitling-thlan-dan-leh-chawm-dan",
                "https://fisheries.mizoram.gov.in/page/injection-hmanga-indian-major-carp-and-exotic-carp-tuitir-dan",
                "https://fisheries.mizoram.gov.in/page/mono-culture-of-giant-fresh-water-prawn",
                googleDoc+"https://fisheries.mizoram.gov.in/uploads/attachments/8bf5c3076314c30dd8a6707efa37e1ee/value-fish-production-from-fresh-water-fish-mizo-tawng.pdf",
                googleDoc+"https://fisheries.mizoram.gov.in/uploads/attachments/d81a4dc9244661b18417b29efc22a8fd/e-book-2019-2020.pdf",
                googleDoc+"https://fisheries.mizoram.gov.in/uploads/attachments/d09ae4d2b10cb742e266d1b417a334ea/pmmsy-guidelines24-june2020.pdf",
                "https://fisheries.mizoram.gov.in/page/draft",
                "https://fisheries.mizoram.gov.in/page/rashtriya-krishi-vikas-yojana-rkvy-css-"
                 };

        recyclerViewHandbook = findViewById(R.id.documentsRV_handbook);

        recyclerViewHandbook.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        documentListAdapter = new DocumentListAdapter(this,documentTitles);
        recyclerViewHandbook.setAdapter(documentListAdapter);

        recyclerViewHandbook.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerViewHandbook, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("TAG"," "+position);
                Intent intent = new Intent(getApplicationContext(),WebViewActivity.class);
                intent.putExtra("link",documentLinks[position]);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }



}