package com.example.ezev.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ezev.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

public class BookNowFragment extends Fragment implements PaymentResultListener {
    public static String userId;
    String pn;
    String email;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_book_now,container,false);
        TextView nameTextView = (TextView) view.findViewById(R.id.textView9);
        Button bookNow = (Button)view.findViewById(R.id.button2);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot ds = task.getResult();
                if (ds.exists()) {

                    nameTextView.setText(ds.getString("full_name"));
                    pn = ds.getString("phone_number");
                    email = ds.getString("email");
                } else {

                }

            }
        });
        bookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Checkout co = new Checkout();
                JSONObject obj = new JSONObject();
                try {
                    obj.put("name",nameTextView.getText());
                    obj.put("currency","INR");
                    obj.put("amount",100);

                    JSONObject prefill = new JSONObject();
                    prefill.put("contact",pn);
                    prefill.put("email",email);
                    obj.put("prefill",prefill);

                    co.open(getActivity(),obj);

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });
        return view;
    }


    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(getContext(),"payment sucess",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getContext(),"payment failed",Toast.LENGTH_SHORT).show();
    }

}
