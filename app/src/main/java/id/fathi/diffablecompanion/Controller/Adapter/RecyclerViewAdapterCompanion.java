package id.fathi.diffablecompanion.Controller.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import id.fathi.diffablecompanion.Controller.Activity.MapsActivity;

public class RecyclerViewAdapterCompanion extends RecyclerView.Adapter<RecyclerViewAdapterCompanion.RecycleViewHolder>{

    Context context;
    List<Request> request;
    DatabaseReference databaseReferenceCompanion, databaseReferenceDifabel, databaseReferencePendamping;
    FirebaseAuth firebaseAuth;
    Dialog dialog;
    User user;

    public RecyclerViewAdapterCompanion(Context context, List<Request> request) {
        this.context = context;
        this.request = request;
    }

    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_companion,parent,false);
        final RecycleViewHolder vholder = new RecycleViewHolder(v);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReferenceDifabel = FirebaseDatabase.getInstance().getReference("Difabel");
        databaseReferencePendamping = FirebaseDatabase.getInstance().getReference("Pendamping");
        databaseReferenceCompanion = FirebaseDatabase.getInstance().getReference("Companion");

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.alertdialog_companion);
        vholder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReferenceDifabel.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(request.get(vholder.getAdapterPosition()).getId())){
                            databaseReferenceDifabel.child(request.get(vholder.getAdapterPosition()).getId()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    user = dataSnapshot.getValue(User.class);

                                    TextView nama = (TextView) dialog.findViewById(R.id.namadialog);
                                    TextView ttl = (TextView) dialog.findViewById(R.id.ttldialog);
                                    TextView nik = (TextView) dialog.findViewById(R.id.nikdialog);
                                    TextView jeniskelamin = (TextView) dialog.findViewById(R.id.jeniskelamindialog);
                                    TextView alamat = (TextView) dialog.findViewById(R.id.alamatdialog);
                                    TextView pekerjaan = (TextView) dialog.findViewById(R.id.pekerjaandialog);
                                    TextView nohp = (TextView) dialog.findViewById(R.id.nohpdialog);
                                    Button companion = (Button) dialog.findViewById(R.id.button_companion);
                                    Button tutup = (Button) dialog.findViewById(R.id.tutup);

                                    nama.setText(" : " + user.getNama());
                                    ttl.setText(" : " + user.getTtl());
                                    nik.setText(" : " + user.getNik());
                                    jeniskelamin.setText(" : " + user.getJeniskelamin());
                                    alamat.setText(" : " + user.getAlamat());
                                    pekerjaan.setText(" : " + user.getPekerjaan());
                                    nohp.setText(" : " + user.getNohp());

                                    if (request.get(vholder.getAdapterPosition()).getStatus().equals("request")){
                                        companion.setText("Terima");
                                        companion.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                databaseReferenceDifabel.child(user.getId()).child("status").setValue("Tidak Perlu Pendamping");
                                                databaseReferencePendamping.child(firebaseAuth.getCurrentUser().getUid()).child("status").setValue("Tidak Tersedia");
                                                databaseReferenceCompanion.child(firebaseAuth.getCurrentUser().getUid()).child(user.getId()).child("status").setValue("companion");
                                                databaseReferenceCompanion.child(user.getId()).child(firebaseAuth.getCurrentUser().getUid()).child("status").setValue("companion");
                                                databaseReferenceCompanion.child(user.getId()).child(firebaseAuth.getCurrentUser().getUid()).child("id").setValue(firebaseAuth.getCurrentUser().getUid());
                                                databaseReferenceDifabel.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.hasChild(firebaseAuth.getCurrentUser().getUid())){
                                                            databaseReferenceDifabel.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    final User currentuser = dataSnapshot.getValue(User.class);
                                                                    databaseReferenceCompanion.child(user.getId()).child(firebaseAuth.getCurrentUser().getUid()).child("nama").setValue(currentuser.getNama());
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
                                                                    final User currentuser = dataSnapshot.getValue(User.class);
                                                                    databaseReferenceCompanion.child(user.getId()).child(firebaseAuth.getCurrentUser().getUid()).child("nama").setValue(currentuser.getNama());
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
                                        });
                                    }
                                    if (request.get(vholder.getAdapterPosition()).getStatus().equals("companion")){
                                        companion.setText("Buka Map");
                                        companion.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent intent = new Intent(context, MapsActivity.class);
                                                intent.putExtra("uid",user.getId());
                                                context.startActivity(intent);
                                            }
                                        });
                                    }

                                    tutup.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                        }
                                    });

                                    dialog.show();

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
                        if (dataSnapshot.hasChild(request.get(vholder.getAdapterPosition()).getId())){
                            databaseReferencePendamping.child(request.get(vholder.getAdapterPosition()).getId()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    final User user = dataSnapshot.getValue(User.class);

                                    TextView nama = (TextView) dialog.findViewById(R.id.namadialog);
                                    TextView ttl = (TextView) dialog.findViewById(R.id.ttldialog);
                                    TextView nik = (TextView) dialog.findViewById(R.id.nikdialog);
                                    TextView jeniskelamin = (TextView) dialog.findViewById(R.id.jeniskelamindialog);
                                    TextView alamat = (TextView) dialog.findViewById(R.id.alamatdialog);
                                    TextView pekerjaan = (TextView) dialog.findViewById(R.id.pekerjaandialog);
                                    TextView nohp = (TextView) dialog.findViewById(R.id.nohpdialog);
                                    Button companion = (Button) dialog.findViewById(R.id.button_companion);
                                    Button tutup = (Button) dialog.findViewById(R.id.tutup);

                                    nama.setText(" : " + user.getNama());
                                    ttl.setText(" : " + user.getTtl());
                                    nik.setText(" : " + user.getNik());
                                    jeniskelamin.setText(" : " + user.getJeniskelamin());
                                    alamat.setText(" : " + user.getAlamat());
                                    pekerjaan.setText(" : " + user.getPekerjaan());
                                    nohp.setText(" : " + user.getNohp());

                                    if (request.get(vholder.getAdapterPosition()).getStatus().equals("request")){
                                        companion.setText("Terima");
                                        companion.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                databaseReferenceDifabel.child(firebaseAuth.getCurrentUser().getUid()).child("status").setValue("Tidak Perlu Pendamping");
                                                databaseReferencePendamping.child(user.getId()).child("status").setValue("Tidak Tersedia");
                                                databaseReferenceCompanion.child(firebaseAuth.getCurrentUser().getUid()).child(user.getId()).child("status").setValue("companion");
                                                databaseReferenceCompanion.child(user.getId()).child(firebaseAuth.getCurrentUser().getUid()).child("status").setValue("companion");
                                                databaseReferenceCompanion.child(user.getId()).child(firebaseAuth.getCurrentUser().getUid()).child("id").setValue(firebaseAuth.getCurrentUser().getUid());
                                                databaseReferenceDifabel.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.hasChild(firebaseAuth.getCurrentUser().getUid())){
                                                            databaseReferenceDifabel.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    final User currentuser = dataSnapshot.getValue(User.class);
                                                                    databaseReferenceCompanion.child(user.getId()).child(firebaseAuth.getCurrentUser().getUid()).child("nama").setValue(currentuser.getNama());
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
                                                                    final User currentuser = dataSnapshot.getValue(User.class);
                                                                    databaseReferenceCompanion.child(user.getId()).child(firebaseAuth.getCurrentUser().getUid()).child("nama").setValue(currentuser.getNama());
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
                                        });
                                    }
                                    if (request.get(vholder.getAdapterPosition()).getStatus().equals("companion")){
                                        companion.setText("Buka Map");
                                        companion.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent intent = new Intent(context,MapsActivity.class);
                                                intent.putExtra("uid",user.getId());
                                                context.startActivity(intent);
                                            }
                                        });
                                    }

                                    tutup.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                        }
                                    });

                                    dialog.show();

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
        });

        return  vholder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewHolder holder, int position) {
        holder.nama.setText(request.get(position).getNama());
        holder.status.setText("Status : " + request.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return request.size();
    }

    public class RecycleViewHolder extends RecyclerView.ViewHolder{

        TextView nama, status;
        LinearLayout item;

        public RecycleViewHolder(@NonNull View itemView) {
            super(itemView);

            nama = (TextView) itemView.findViewById(R.id.namacompanion);
            status = (TextView) itemView.findViewById(R.id.status);
            item = (LinearLayout) itemView.findViewById(R.id.recycler_companion);
        }
    }
}
