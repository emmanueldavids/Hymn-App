package com.example.hymnapp;

import androidx.work.WorkerParameters;

import com.google.firebase.firestore.FirebaseFirestore;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;


public class SyncWorker extends Worker {

    public SyncWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        List<Hymn> localHymns = HymnUtils.readHymnsFromFile(getApplicationContext());

        Task<QuerySnapshot> task = db.collection("hymns").get();
        CountDownLatch latch = new CountDownLatch(1);

        final List<Hymn>[] result = new List[]{new ArrayList<>()};

        task.addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                Hymn hymn = doc.toObject(Hymn.class);
                result[0].add(hymn);
            }

            List<Hymn> updated = HymnUtils.mergeHymns(localHymns, result[0]);
            HymnUtils.saveHymnsToJson(getApplicationContext(), updated);
            latch.countDown();
        }).addOnFailureListener(e -> {
            e.printStackTrace();
            latch.countDown();
        });

        try {
            latch.await(); // Wait for Firebase to complete
        } catch (InterruptedException e) {
            return Result.failure();
        }

        return Result.success();
    }
}
