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

import id.fathi.diffablecompanion.Model.User;
import id.fathi.diffablecompanion.R;
import id.fathi.diffablecompanion.Controller.Adapter.RecyclerViewAdapterCari;


/**
 * A simple {@link Fragment} subclass.
 */
public class CariFragment extends Fragment {

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    DatabaseReference databaseReferenceDifabel, databaseReferencePendamping;
    FirebaseAuth firebaseAuth;
    private List<User> user;
    private RecyclerViewAdapterCari recyclerViewAdapterCari;
    String uid;

    public CariFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_cari, container, false);

        recyclerView = view.findViewById(R.id.recyclerviewcari);
        swipeRefreshLayout = view.findViewById(R.id.swiperefreshcari);
        databaseReferenceDifabel = FirebaseDatabase.getInstance().getReference("Difabel");
        databaseReferencePendamping = FirebaseDatabase.getInstance().getReference("Pendamping");
        firebaseAuth = FirebaseAuth.getInstance();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        load();

        return view;
    }

    private void load(){
        user = new ArrayList<>();
        recyclerViewAdapterCari = new RecyclerViewAdapterCari(getActivity(),user);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerViewAdapterCari);
        uid = firebaseAuth.getCurrentUser().getUid();
        databaseReferenceDifabel.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(uid)){
                    databaseReferencePendamping.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            user.clear();
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                                User temp = postSnapshot.getValue(User.class);
                                if (temp.getStatus().equals("Tersedia") || temp.getStatus().equals("Perlu Pendamping")) {
                                    user.add(temp);
                                }
                            }
                            recyclerViewAdapterCari.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReferencePendamping.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(uid)){
                    databaseReferenceDifabel.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            user.clear();
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                                User temp = postSnapshot.getValue(User.class);
                                user.add(temp);
                            }
                            recyclerViewAdapterCari.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
