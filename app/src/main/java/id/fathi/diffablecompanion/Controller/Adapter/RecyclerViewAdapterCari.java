package id.fathi.diffablecompanion.Controller.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import id.fathi.diffablecompanion.Model.Request;
import id.fathi.diffablecompanion.Model.User;
import id.fathi.diffablecompanion.R;

public class RecyclerViewAdapterCari extends RecyclerView.Adapter<RecyclerViewAdapterCari.RecycleViewHolder> {

    DatabaseReference databaseReferenceDifabel, databaseReferencePendamping, databaseReferenceCompanion;
    FirebaseAuth firebaseAuth;
    public String uid, namatemp;
    Context context;
    List<User> user;
    Dialog dialog;
    User temp;
    double distance;

    public RecyclerViewAdapterCari(Context context, List<User> user) {
        this.context = context;
        this.user = user;
    }

    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_cari,parent,false);
        final RecycleViewHolder vholder = new RecycleViewHolder(v);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReferenceDifabel = FirebaseDatabase.getInstance().getReference("Difabel");
        databaseReferencePendamping = FirebaseDatabase.getInstance().getReference("Pendamping");
        databaseReferenceCompanion = FirebaseDatabase.getInstance().getReference("Companion");
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.alertdialog_cari);
        vholder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView nama = (TextView) dialog.findViewById(R.id.namadialog);
                TextView ttl = (TextView) dialog.findViewById(R.id.ttldialog);
                TextView nik = (TextView) dialog.findViewById(R.id.nikdialog);
                TextView jeniskelamin = (TextView) dialog.findViewById(R.id.jeniskelamindialog);
                TextView alamat = (TextView) dialog.findViewById(R.id.alamatdialog);
                TextView pekerjaan = (TextView) dialog.findViewById(R.id.pekerjaandialog);
                TextView nohp = (TextView) dialog.findViewById(R.id.nohpdialog);
                Button request = (Button) dialog.findViewById(R.id.request);
                Button tutup = (Button) dialog.findViewById(R.id.tutup);

                nama.setText(" : " + user.get(vholder.getAdapterPosition()).getNama());
                ttl.setText(" : " + user.get(vholder.getAdapterPosition()).getTtl());
                nik.setText(" : " + user.get(vholder.getAdapterPosition()).getNik());
                jeniskelamin.setText(" : " + user.get(vholder.getAdapterPosition()).getJeniskelamin());
                alamat.setText(" : " + user.get(vholder.getAdapterPosition()).getAlamat());
                pekerjaan.setText(" : " + user.get(vholder.getAdapterPosition()).getPekerjaan());
                nohp.setText(" : " + user.get(vholder.getAdapterPosition()).getNohp());

                databaseReferenceDifabel.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(firebaseAuth.getCurrentUser().getUid())){
                            databaseReferenceDifabel.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    temp = dataSnapshot.getValue(User.class);
                                    namatemp = temp.getNama();
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
                        if (dataSnapshot.hasChild(firebaseAuth.getCurrentUser().getUid())){
                            databaseReferencePendamping.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    temp = dataSnapshot.getValue(User.class);
                                    namatemp = temp.getNama();
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

                request.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Request request1 = new Request(firebaseAuth.getCurrentUser().getUid(), namatemp,"request");
                        databaseReferenceCompanion.child(user.get(vholder.getAdapterPosition()).getId()).child(firebaseAuth.getCurrentUser().getUid()).setValue(request1);
                        Toast.makeText(context, "Berhasil Mengirim Request Damping", Toast.LENGTH_SHORT).show();
                    }
                });

                tutup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        return vholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecycleViewHolder holder, final int position) {
        holder.nama.setText(user.get(position).getNama());
        uid = firebaseAuth.getCurrentUser().getUid();
        databaseReferenceDifabel.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(uid)) {
                    databaseReferenceDifabel.child(uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            temp = dataSnapshot.getValue(User.class);
                            Location start = new Location("");
                            start.setLatitude(temp.getLatitude());
                            start.setLongitude(temp.getLongitude());
                            Location end = new Location("");
                            end.setLatitude(user.get(position).getLatitude());
                            end.setLongitude(user.get(position).getLongitude());

                            distance = start.distanceTo(end);
                            holder.jarak.setText(String.format("%.2f", distance) + " meter");
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
                if (dataSnapshot.hasChild(uid)) {
                    databaseReferencePendamping.child(uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            temp = dataSnapshot.getValue(User.class);
                            Location start = new Location("");
                            start.setLatitude(temp.getLatitude());
                            start.setLongitude(temp.getLongitude());Location end = new Location("");
                            end.setLatitude(user.get(position).getLatitude());
                            end.setLongitude(user.get(position).getLongitude());

                            distance = start.distanceTo(end);
                            holder.jarak.setText(String.format("%.2f", distance) + " meter");
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

    @Override
    public int getItemCount() {
        return user.size();
    }

    public class RecycleViewHolder extends RecyclerView.ViewHolder{

        TextView nama, jarak;
        LinearLayout item;

        public RecycleViewHolder(@NonNull View itemView) {
            super(itemView);

            nama = (TextView) itemView.findViewById(R.id.nama);
            jarak = (TextView) itemView.findViewById(R.id.jarak);
            item = (LinearLayout) itemView.findViewById(R.id.recycler_cari);

        }
    }

}
