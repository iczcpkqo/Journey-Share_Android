package com.journey.firebase;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.Map;

public class FirebaseUtil {


    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static FirebaseUtil instance;

    private static Map<String, Object> resultMap;


    public static FirebaseUtil getInstance() {
        if (instance == null) {
            instance = new FirebaseUtil();
        }
        return instance;
    }


    public String insert(String collectionName, Object o) {
        try {
            db.collection(collectionName).add(o);
        } catch (Exception e) {
            e.printStackTrace();
            return "failed";
        }
        return "successful";
    }

    public synchronized Map<String, Object> selectByDocumentId(String collectionName, String documentId) {
        try {
            DocumentReference document = db.collection(collectionName).document(documentId);

            document.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    resultMap = task.getResult().getData();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return resultMap;
    }

    public String updateDocumentByDocumentId(Map<String, Object> user2, String collctionName, String documentId) {
        try {
            WriteBatch batch = db.batch();
            DocumentReference ref = db.collection(collctionName).document(documentId);
            batch.update(ref, user2);
            batch.commit();
        } catch (Exception e) {
            e.printStackTrace();
            return "failed";
        }
        return "successful";

    }

    public String deleteByDoucumentId(String collctionName, String documentId){
        try {
            db.collection(collctionName).document(documentId).delete();
        }catch (Exception e){
            e.printStackTrace();
            return "failed";
        }
        return "successful";
    }
}
