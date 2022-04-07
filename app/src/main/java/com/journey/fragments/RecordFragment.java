package com.journey.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.journey.R;
import com.journey.adapter.RecordAdapter;
import com.journey.entity.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class RecordFragment extends Fragment {

    ListView mlistView;
    ArrayList<Record> records;
    ListenerRegistration lg;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        String email = null;
        if (currentUser!=null){
            email = currentUser.getEmail();
        }
        boolean emailVerified = currentUser.isEmailVerified();

        Log.d("User",email);
        View v = inflater.inflate(R.layout.fragment_record,container,false);
        records = new ArrayList<>();
        mlistView = v.findViewById(R.id.listView);
        RecordAdapter adapter = new RecordAdapter(this.getContext(),records);
        mlistView.setAdapter(adapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("record").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()){
//                    for (QueryDocumentSnapshot document: task.getResult()){
//                        Map<String, Object> data = document.getData();
//                        Record tmp = new Record((String)document.getId(),(String)data.get("departure"),(String)data.get("arrival"),((Timestamp)data.get("date")).toDate());
////                        records.add(tmp);
//                        adapter.add(tmp);
//                        Log.d("Pengbo",document.getId() + "=>" + document.getData().get("departure"));
//                    }
//                }
//            }
//        });

        lg = db.collection("record")
                .whereEqualTo("BelongTo",email)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                Log.d("listener","detected change(s)");

                if(error != null){
                    Log.w("listener","failed",error);
                }

                List<Record> record = new ArrayList<Record>();
                adapter.clear();
                for (QueryDocumentSnapshot doc: value){
                    Map<String, Object> data = doc.getData();
                    Record tmp = new Record((String)doc.getId(),
                            (String)data.get("departure"),
                            (String)data.get("arrival"),
                            ((Timestamp)data.get("date")).toDate(),
                            ((Timestamp)data.get("arrivalDate")).toDate(),
                            (String)data.get("companion"),
                            (String)data.get("cost"),
                            (String) data.get("distance"));
                    adapter.add(tmp);
                }
                Log.d("listener","current:"+record);

            }
        });

        // Inflate the layout for this fragment
        return v;
    }


}