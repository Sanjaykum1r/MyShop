package com.example.mybookshop.Buyers;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mybookshop.Admin.AdminMaintainProductActivity;
import com.example.mybookshop.Model.Products;
import com.example.mybookshop.Prevalent.Prevalent;
import com.example.mybookshop.R;
import com.example.mybookshop.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity
implements NavigationView.OnNavigationItemSelectedListener

{
    private TextView textView;
    private AppBarConfiguration mAppBarConfiguration;

    private DatabaseReference ProductRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private String type=" ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if (bundle!=null)
        {
            type=getIntent().getExtras().get("Admin").toString();
        }


        Paper.init(this);
        ProductRef= FirebaseDatabase.getInstance().getReference().child("Products");
        recyclerView=findViewById(R.id.recycle_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!type.equals("Admin"))
                {

                    Intent intent=new Intent(HomeActivity.this, CartActivity.class);
                    startActivity(intent);
                }

            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);


        View headerView=navigationView.getHeaderView(0);
        textView=headerView.findViewById(R.id.user_profile_name);
        CircleImageView circleImageView=headerView.findViewById(R.id.user_profile_image);
        if (!type.equals("Admin"))
        {

            textView.setText(Prevalent.currentOnlineUser.getName());
            Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(circleImageView);
        }



        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Products> options=
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductRef.orderByChild("ProductState").equalTo("Approved"), Products.class)
                        .build();
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter=
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull Products products) {

                        productViewHolder.txtProductName.setText(products.getPname());
                        productViewHolder.txtProductDescrition.setText(products.getDescription());
                        productViewHolder.getTxtProductprice.setText("Price "+products.getPrice()+"$");
                        Picasso.get().load(products.getImage()).into(productViewHolder.imageView);



                        productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (type.equals("Admin"))
                                {

                                    Intent intent=new Intent(HomeActivity.this, AdminMaintainProductActivity.class);

                                    intent.putExtra("pid",products.getPid());
                                    startActivity(intent);

                                }
                                else
                                {

                                    Intent intent=new Intent(HomeActivity.this, ProductDetailsActivity.class);

                                    intent.putExtra("pid",products.getPid());
                                    startActivity(intent);
                                }

                            }
                        });



                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout,parent,false);
                        ProductViewHolder holder=new ProductViewHolder(view);
                        return holder;


                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DrawerLayout drawer=findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();

        if (id== R.id.action_settings)
        {
            Intent intent=new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(intent);

        }
        if (id== R.id.nav_cart)
        {
            if (!type.equals("Admin"))
            {
                Intent intent=new Intent(HomeActivity.this,CartActivity.class);
                startActivity(intent);


            }

        }

        else if (id== R.id.nav_search)
        {
            if (!type.equals("Admin"))
            {

                Intent intent=new Intent(HomeActivity.this, SearchProductActivity.class);
                startActivity(intent);
            }


        }
        else if (id== R.id.nav_categories)
        {
            if (!type.equals("Admin"))
            {

            }

        }

        else if (id== R.id.nav_logout)
        {
            if (!type.equals("Admin"))
            {


                Paper.book().destroy();
                Intent intent=new Intent(HomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

        }





        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        int id=item.getItemId();


        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}