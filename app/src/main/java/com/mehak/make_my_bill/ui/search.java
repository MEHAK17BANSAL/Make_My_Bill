package com.mehak.make_my_bill.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.database.DatabaseReference;
import com.mehak.make_my_bill.R;
import com.mehak.make_my_bill.adapter.StockAdapter;
import com.mehak.make_my_bill.model.Stock;

public class search extends AppCompatActivity {

    private EditText mSearchField;
    private ImageButton mSearchBtn;
    CollectionReference usercollection;
    private RecyclerView mResultList;
FirebaseFirestore firebaseFirestore;
    private DatabaseReference mUserDatabase;
    LinearLayoutManager linearLayoutManager;
StockAdapter stockAdapter;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchactivity);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        usercollection = firebaseFirestore.collection("stock");
        linearLayoutManager = new LinearLayoutManager(this);

        mSearchField = (EditText) findViewById(R.id.search_field);
        mSearchBtn = (ImageButton) findViewById(R.id.search_btn);

        mResultList = (RecyclerView) findViewById(R.id.result_list);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(this));
mSearchField.addTextChangedListener(new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {


        if (!s.toString().isEmpty()) {
            String s1=s.toString();
            firebaseUserSearch(s1);

        } else {
            /*
             * Clear the list when editText is empty
             * */

            mResultList.removeAllViews();
        }



//String s1=s.toString();
       // firebaseUserSearch(s1);
    }
});
      /* mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String searchText = mSearchField.getText().toString();

                firebaseUserSearch(searchText);

            }
        });*/

    }

    private void firebaseUserSearch(String s1) {

        Toast.makeText(search.this, "Started Search", Toast.LENGTH_LONG).show();

        com.google.firebase.firestore.Query query = usercollection.orderBy("name").startAt(s1).endAt(s1 + "\uf8ff");


        FirestoreRecyclerOptions<Stock> options = new FirestoreRecyclerOptions.Builder<Stock>().setQuery(query, Stock.class).build();
        stockAdapter  = new StockAdapter(options,search.this);

        stockAdapter.startListening();

        mResultList.setAdapter(stockAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mResultList.getContext(),linearLayoutManager.getOrientation());
        mResultList.addItemDecoration(dividerItemDecoration);





    }


}
