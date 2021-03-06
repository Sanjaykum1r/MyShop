package com.example.mybookshop.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybookshop.Model.Cart;
import com.example.mybookshop.R;
import com.example.mybookshop.ViewHolder.CardViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminUserProductActivity extends AppCompatActivity {

    private RecyclerView productsList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartListRef;
    private String userID= " ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_product);

        userID=getIntent().getStringExtra("uid");

        productsList=findViewById(R.id.products_list);
        productsList.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        productsList.setLayoutManager(layoutManager);

        cartListRef= FirebaseDatabase.getInstance().getReference()
                .child("Cart List")
                .child("Admin View").child(userID).child("Product");


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Cart> options=
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef, Cart.class)
                        .build();
        FirebaseRecyclerAdapter<Cart, CardViewHolder> adapter=
                new FirebaseRecyclerAdapter<Cart, CardViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CardViewHolder cartViewHolder, int i, @NonNull Cart cart) {

                        cartViewHolder.txtProductQuantity.setText("Quantity = " + cart.getQuantity());

                        cartViewHolder.txtProductPrice.setText("Price =" + cart.getPrice());

                        cartViewHolder.txtProductName.setText("Name = " + cart.getPname());

                    }

                    @NonNull
                    @Override
                    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                        CardViewHolder holder = new CardViewHolder(view);
                        return holder;
                    }
                };

        productsList.setAdapter(adapter);
        adapter.startListening();
    }
}