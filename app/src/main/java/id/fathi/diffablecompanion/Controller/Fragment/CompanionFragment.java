package id.fathi.diffablecompanion.Controller.Fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import id.fathi.diffablecompanion.Model.Request;
import id.fathi.diffablecompanion.R;
import id.fathi.diffablecompanion.Controller.Adapter.RecyclerViewAdapterCompanion;


/**
 * A simple {@link Fragment} subclass.
 */
public class CompanionFragment extends Fragment {

    SwipeRefreshLayout swipeRefreshLayoutCompanion;
    RecyclerView recyclerView;
    DatabaseReference databaseReferenceDifabel, databaseReferencePendamping, databaseReferenceCompanion;
    FirebaseAuth firebaseAuth;
    private List<Request> request;
    private RecyclerViewAdapterCompanion recyclerViewAdapterCompanion;
    String uid;

    public CompanionFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_companion, container, false);

        recyclerView = view.findViewById(R.id.recyclerviewcompanion);
        swipeRefreshLayoutCompanion = view.findViewById(R.id.swiperefreshcomp);
        databaseReferenceDifabel = FirebaseDatabase.getInstance().getReference("Difabel");
        databaseReferencePendamping = FirebaseDatabase.getInstance().getReference("Pendamping");
        databaseReferenceCompanion = FirebaseDatabase.getInstance().getReference("Companion");
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();

        swipeRefreshLayoutCompanion.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load();
                swipeRefreshLayoutCompanion.setRefreshing(false);
            }
        });

        load();

        return view;
    }

    private void load() {
        request = new ArrayList<>();
        recyclerViewAdapterCompanion = new RecyclerViewAdapterCompanion(getActivity(),request);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerViewAdapterCompanion);
        uid = firebaseAuth.getCurrentUser().getUid();
        databaseReferenceCompanion.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                request.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Request temp = postSnapshot.getValue(Request.class);
                    request.add(temp);
                }
                recyclerViewAdapterCompanion.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
