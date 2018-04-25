package com.codegreed_devs.and_pos;


import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.codegreed_devs.and_pos.GridView_Elements.FirebaseHelper;
import com.codegreed_devs.and_pos.GridView_Elements.Menu_Item;
import com.codegreed_devs.and_pos.Tabs_for_menus.Pager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;



public class UpdateFoodMenu extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    EditText nameT,price,food_quantity;
    Button upsave;
    Toolbar update_menu_toolbar;
    public static FirebaseHelper  firebaseHelper;
    public static DatabaseReference db;
    ImageView imageView;
    String[] category_select={"Select Category","BreakFast","Lunch","Supper","Drinks"};
    String[] menu_types={"none","Menu_Item_1/BreakFast","Menu_Item_2/Lunch","Menu_Item_3/Supper","Menu_Item_4/Drinks"};
    Spinner category_dropdown;
    String category;
    AlertDialog.Builder popDialog;
    String image_name;
    Uri resultUri;
    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    public static ViewPager viewPager;

    //    for themes
    String selected_theme;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //theme initialization
        // obtain an instance of the SharedPreferences class
        preferences= getSharedPreferences("UserTheme", MODE_PRIVATE);
        DisplayTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_food_menu);
        Toolbar toolbar=findViewById(R.id.update_menu_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initializing the tablayout
        tabLayout = findViewById(R.id.sliding_tabs);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText("BreakFast"));
        tabLayout.addTab(tabLayout.newTab().setText("Lunch"));
        tabLayout.addTab(tabLayout.newTab().setText("Supper"));
        tabLayout.addTab(tabLayout.newTab().setText("Drinks"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        //Initializing viewPager
        viewPager = findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(4);

        //Creating our pager adapter
        Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);
        tabLayout.setOnTabSelectedListener(this);
        //start of making tab indicator scroll with view pager
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        update_menu_toolbar=findViewById(R.id.update_menu_toolbar);

        /*
        end of making the tab indicator scroll with the viewpager
         */

        //start of firebase
        db=FirebaseDatabase.getInstance().getReference();
        firebaseHelper=new FirebaseHelper(db);

        nameT= findViewById(R.id.f_name);
        price= findViewById(R.id.f_price);
        upsave= findViewById(R.id.btn_save);
        food_quantity= findViewById(R.id.txt_quantity);
        imageView= findViewById(R.id.image_food);
        category_dropdown= findViewById(R.id.categories);
        UpdateColors();

       //serring values to the spinner
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,category_select);
        category_dropdown.setAdapter(arrayAdapter);
        category_dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category=menu_types[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

       //Creating the directory to save the images
        try {
            File dir = new File(Environment.getExternalStorageDirectory() + "/AndPOS/media/");
            dir.mkdirs();
        }catch (RuntimeException e){

        }

        //Add food click listener
        upsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(UpdateFoodMenu.this, ""+resultUri, Toast.LENGTH_SHORT).show();
                if(nameT.getText().toString().isEmpty() || price.getText().toString().isEmpty()||category.equalsIgnoreCase("none") || food_quantity.getText().toString().isEmpty()){
                    Toast.makeText(UpdateFoodMenu.this, "Fill in All the details", Toast.LENGTH_SHORT).show();
                }else{
                    Menu_Item menu_item=new Menu_Item();
                    menu_item.setName(nameT.getText().toString().trim());
                    menu_item.setPrice(Integer.parseInt(price.getText().toString().trim()));
                    menu_item.setImage_name(resultUri.toString());
                    firebaseHelper.save(menu_item,category);
                    Toast.makeText(UpdateFoodMenu.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                    nameT.setText("");
                    price.setText("");
                    food_quantity.setText("");
                    category_dropdown.setSelection(0);
                }

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nameT.getText().toString().isEmpty()){
                    Toast.makeText(UpdateFoodMenu.this, "Please Select A Name First", Toast.LENGTH_SHORT).show();
                }else {
                    check_permission();
                    CropImage.activity()
                            .setMinCropResultSize(640, 480)
                            .setMaxCropResultSize(640, 480)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(UpdateFoodMenu.this);
                }
            }
        });


    }

    //method for getting the uri of the image
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                imageView.setImageURI(resultUri);
                image_name=nameT.getText().toString().trim()+".jpeg";
                Picasso.with(UpdateFoodMenu.this).load(resultUri).into(picassoImageTarget(getApplicationContext(), "imageDir", "my_"+image_name));

                /*

                 */

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                result.getError();
            }
        }
    }

    //perssioms checks
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==100 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            File dir = new File(Environment.getExternalStorageDirectory() + "/AndPOS/media/");
            dir.mkdirs();

        }else {
            popDialog=new AlertDialog.Builder(UpdateFoodMenu.this);
            popDialog.setTitle("Permission")
                    .setMessage("Please grant permission for the normal working of the app")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            check_permission();

                        }
                    });
            popDialog.create().show();
        }
    }

    private void check_permission(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
//
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
                return;
            }else {
                File dir = new File(Environment.getExternalStorageDirectory() + "/AndPOS/media/");
                dir.mkdirs();
            }

        }

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    private Target picassoImageTarget(Context context, final String imageDir, final String imageName) {

        ContextWrapper cw = new ContextWrapper(context);
        final File directory = cw.getDir(imageDir, Context.MODE_PRIVATE); // path to /data/data/yourapp/app_imageDir
        return new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final File myImageFile = new File(directory, imageName); // Create image file
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(myImageFile);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        Log.i("image", "image saved to >>>" + myImageFile.getAbsolutePath());

                    }

                }).start();

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                if (placeHolderDrawable != null) {}
            }
        };
    }

    private void UpdateColors(){
        //get the saved theme from preference
        selected_theme=preferences.getString("theme",null);

        //load the user selected theme first before displaying views
        if (selected_theme!=null){
            switch (selected_theme) {

                case "green":
                    update_menu_toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryGreen));
                    imageView.setBackgroundResource(R.drawable.image_holder_green);
                    category_dropdown.setBackgroundResource(R.drawable.image_holder_green);
                    break;

                case "purple":
                    update_menu_toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryPurple));
                    imageView.setBackgroundResource(R.drawable.image_holder_purple);
                    category_dropdown.setBackgroundResource(R.drawable.image_holder_purple);
                    break;

                default:
                    update_menu_toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    imageView.setBackgroundResource(R.drawable.image_holder);
                    category_dropdown.setBackgroundResource(R.drawable.image_holder);
                    break;

            }

        }

    }


    private void DisplayTheme() {
        //get the saved theme from preference
        selected_theme=preferences.getString("theme",null);

        Utils.onActivityCreateSetTheme(this);

        //load the user selected theme first before displaying views
        if (selected_theme!=null){
            switch (selected_theme) {

                case "green":
                    setTheme(R.style.ThemeGreen);

                    break;

                case "purple":
                    setTheme(R.style.ThemePurple);

                    break;

                default:
                    setTheme(R.style.AppTheme);

                    break;

            }

        }
    }




}
