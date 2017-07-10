package com.example.alvise.provafoto;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.UiThread;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static android.widget.ImageView.ScaleType.FIT_CENTER;


public class MainActivity extends FragmentActivity {

    private int count=0;
    private String text;
    private int idImageDialog =-1;
    private Repo repo = new Repo(this);
    private boolean visibilityDialog = false;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private static final int CAPTURE_IMAGE = 1;
    private static final int RECORD_REQUEST = 30;
    private ArrayList <Uri> listUri= new ArrayList<Uri>();
    private int conta=0;
    private ArrayList <Integer> videoUri = new ArrayList<Integer>();
    private int idImageViewPage = -1;
    // When requested, this adapter returns a DemoObjectFragment,
    // representing an object in the collection.
   /* DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
    ViewPager mViewPager;*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        text = "";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);


/*
        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        mDemoCollectionPagerAdapter =new DemoCollectionPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);
*/



        //per quando cambio rotazione dello schermo cosi mi carico tutti i dati e non li perdo
        if (savedInstanceState != null) {
            //i é il m=numero di cicli da fare
            int i = savedInstanceState.getInt("i");
            ImageView imageView;
            for (int j = 0; j < i; j++) {
                imageView = new ImageView(this);
                // prendo le immagini dalla numero 0
                byte[] byteArray = savedInstanceState.getByteArray("immagine" + j);
                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                imageView.setImageBitmap(bitmap);
                impostaImmagini(imageView);
            }
            int id= savedInstanceState.getInt("id");
            //nel caso avessi aperta una Dialog precedentemente, la ripristino con la stessa immagine di prima
            visibilityDialog= savedInstanceState.getBoolean("dialog");


            listUri=  savedInstanceState.getParcelableArrayList("uris");
            if (listUri != null)
                conta = savedInstanceState.getInt("conta");

            videoUri =  savedInstanceState.getIntegerArrayList("videos");

            if (id != -1 && visibilityDialog==true){
                LinearLayout sc = (LinearLayout) findViewById(R.id.fotoScroll);
                id=sc.getChildAt(id).getId();
                creaDialog(id, null, null);
            }
        }
    }

    public void takePhoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 1);
        }
    }


    public void selectPhoto(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), 2);
    }

    public void scelta(View view){
        final Dialog presentDialog = new Dialog(MainActivity.this);
        presentDialog.setTitle("Scelta");
        presentDialog.setContentView(R.layout.activity_scelta);
        Button video = (Button) presentDialog.findViewById(R.id.video);
        Button gallery = (Button) presentDialog.findViewById(R.id.gallery);

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, 200);
                }
                presentDialog.dismiss();
            }
        });


        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(Intent.createChooser(intent, "Select File"), 2);
                }

                presentDialog.dismiss();
            }
        });



        presentDialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                // Video captured and saved to fileUri specified in the Intent
                Toast.makeText(this, "Video saved to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();

               // String path= getRealPathFromURI(MainActivity.this,data.getData());
                //  Uri.parse(path)

                videoUri.add(conta, 1);
                listUri.add(conta ++, data.getData());


                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
             //   Bitmap bitmap = BitmapFactory.decodeFile("C:\\Users\\alvis\\AndroidStudioProjects\\ProvaFoto\\app\\src\\main\\res\\drawable\\ic_add_black_24dp.png", options);

                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();

                mediaMetadataRetriever.setDataSource(MainActivity.this, data.getData());
                Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime(1); //unit in microsecond

               // Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_add_black_24dp);

                ImageView imageView = new ImageView(this);
                bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true);
                imageView.setImageBitmap(bitmap);

                impostaVideo(imageView, data.getData());



            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Request Canceled\n", Toast.LENGTH_LONG).show();
            }
                // Video capture failed, advise user
        }


        if (requestCode == CAPTURE_IMAGE) {
            int result = -1;
            final int res;

            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Request Canceled\n", Toast.LENGTH_LONG).show();
            }


            if (resultCode == Activity.RESULT_OK) { // se mi arriva un'immagine dalla fotocamera

                    ImageView imageView = new ImageView(this);
                    if (resultCode == RESULT_OK) {

                        Bundle extras = data.getExtras();
                        Bitmap bit = (Bitmap) extras.get("data");
                        bit = Bitmap.createScaledBitmap(bit, 400, 400, true);
                        imageView.setImageBitmap(bit);

                        Uri uri = bitmapToUriConverter(bit);


                        videoUri.add(conta, 0);
                        listUri.add(conta ++, uri);


                        //imposto l'immagine nella ScrollView
                        impostaImmagini(imageView);
                    }
                }//end caso normale


        } else {
            if (requestCode == 2) { //se apro la galleria
                if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(this, "Request Canceled\n", Toast.LENGTH_LONG).show();
                } else {
                    if (resultCode == Activity.RESULT_OK) {
                        try {
                            ImageView imageView = new ImageView(this);
                            Bitmap supp = (MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData()));
                            supp = Bitmap.createScaledBitmap(supp, 400, 400, true);
                            imageView.setImageBitmap(supp);
                            //ho l'immagine e la setto nella ScrollView

                            Uri uri = bitmapToUriConverter(supp);

                            videoUri.add(conta, 0);
                            listUri.add(conta ++, uri);


                            impostaImmagini(imageView);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } // end if request ==2
        }
    }


    private void impostaVideo(ImageView imageView, Uri conto){
    final Uri interno=conto;
        LinearLayout scrollView = (LinearLayout) findViewById(R.id.fotoScroll);
        imageView.setPadding(5, 5, 5, 5);
        //incremento l'id personale di ogni immagine in modo da non averne mai 2 uguali
        imageView.setId(++count);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(400, 400));
        imageView.setScaleType(FIT_CENTER );
        //aggiungo l'immagine nella ScrollView
        scrollView.addView(imageView, scrollView.getChildCount());
        //prendo l'Id della mia immagine
        final int idImageView = imageView.getId();
        final Bitmap yourBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        final Bitmap bitmap = Bitmap.createScaledBitmap(yourBitmap, 500, 500, true);


        //se clicco sull'immagine
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                //apro la finestra di Dialog
                //creaDialog(idImageView, byteArray, interno);


              /*  VideoView mVideoView  = (VideoView) findViewById(R.id.prova);
                mVideoView.setMediaController(new MediaController(MainActivity.this));
                mVideoView.setVideoURI(interno);
                mVideoView.requestFocus();
                mVideoView.start(); */

                creaDialog(idImageView, byteArray, interno);


            }

        });
    }



    private void impostaImmagini(ImageView imageView){

        LinearLayout scrollView = (LinearLayout) findViewById(R.id.fotoScroll);
        imageView.setPadding(5, 5, 5, 5);
        //incremento l'id personale di ogni immagine in modo da non averne mai 2 uguali
        imageView.setId(++count);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(400, 400));
        imageView.setScaleType(FIT_CENTER );
        //aggiungo l'immagine nella ScrollView
        scrollView.addView(imageView, scrollView.getChildCount());
        //prendo l'Id della mia immagine
        final int idImageView = imageView.getId();
        final Bitmap yourBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        final Bitmap bitmap = Bitmap.createScaledBitmap(yourBitmap, 500, 500, true);


        //se clicco sull'immagine
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                //apro la finestra di Dialog
                creaDialog(idImageView, byteArray, null);
            }

        });
    }



    @Override
    @UiThread
    //metodo che mi salva i miei oggetti nel caso girassi lo schermo
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LinearLayout sc = (LinearLayout) findViewById(R.id.fotoScroll);
        ByteArrayOutputStream stream;
        ImageView supporto;
        String supp = "";
        int i;
        int idDialogCancelled=-1;
        //non essendo serializzabili, devo fare cosi per mettere le View dentro outState, prendendole 1 ad 1
        for (i = 0; i < sc.getChildCount(); i++) {
            stream = new ByteArrayOutputStream();
            supporto = (ImageView) sc.getChildAt(i);
            Bitmap bitmap = ((BitmapDrawable) supporto.getDrawable()).getBitmap();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            //questo é il nominativo delle immagini che poi avranno dentro il Bundle
            supp = "immagine" + i;
            outState.putByteArray(supp, byteArray);

            if (sc.getChildAt(i).getId()== idImageDialog)
                idDialogCancelled=i;
        }
        //mi serve per saper quanti cicli fa
        outState.putInt("i", i);

        //mi serve per poter ripristinare la Dialog che é stata distrutta inseieme alla sua immagine
        outState.putInt("id", idDialogCancelled);

        outState.putBoolean("dialog", visibilityDialog);

        outState.putParcelableArrayList("uris", listUri);

        outState.putIntegerArrayList("videos", videoUri);

        outState.putInt("conta", conta);

    }

    //mi serve per rimuovere un'immagine dalla ScrollView e dal database
    private void rimuoviImmagine(int id){
        final int idImage=id;
        final LinearLayout sc = (LinearLayout) findViewById(R.id.fotoScroll);
        //aspetto che prenda il controllo l'UI Thread
        sc.post(new Runnable() {
            public void run() {
                int supporto=0;
                for (int z = 0; z < sc.getChildCount(); ++z) {
                    int currentViewId = sc.getChildAt(z).getId();

                    //se l'id corrisposnde a quello dell'immagine da cancellare
                    if (currentViewId == idImage) {
                        supporto = z;
                        //rimuovo dal database l'immagine della ScrollView 1
                        repo.delete(1, currentViewId);
                        videoUri.remove(z);
                    }
                    if (idImageDialog ==idImage){
                        idImageDialog =-1;
                    }
                } // end for

                //invalido e rimuovo l'immagine
                sc.getChildAt(supporto).invalidate();
                sc.removeViewAt(supporto);
                //elimino l'immagine anche dai miei Uri e abbasso il conteggio degli Uri
                listUri.remove(supporto);

                conta--;
            }
        });
    }

    private void creaDialog(int id, byte[] byteArray, Uri uri){

    //prove del funzionamento del database
   /*     Repo repo = new Repo(this);
        repo.insert(10, 100, "ciao", "come va");
        repo.insert(15, 150, "tutto bene", "grazie");
        int prova = repo.getStudentById(15, 100);
        repo.getAll();
        repo.delete(15, 150);
        repo.update(10, 100, "coccode");
        repo.getAll();
        repo.insert(15, 150, "tutto bene", "grazie");
*/

        idImageViewPage=id;
        //creo la Dialog
        final Dialog presentDialog = new Dialog(MainActivity.this,android.R.style.Theme);
        presentDialog.setTitle("Titolo");
        presentDialog.setContentView(R.layout.activity_slide);
        final EditText testo = (EditText) presentDialog.findViewById(R.id.message);
       // ImageView mImageView = (ImageView) presentDialog.findViewById(R.id.stillshot_imageview);
        Button saveDescription = (Button) presentDialog.findViewById(R.id.confirm);
        Button removeImage = (Button) presentDialog.findViewById(R.id.remove);
        //FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.myFAB);

        if (byteArray != null) { //se mi arriva una bitmap la salvo semplicemente
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

            if (bmp != null) {
                //mImageView.setImageBitmap(bmp);
                repo.insert(1, id, " ", bmp.toString());
            }
        }
        else{ //se no mi é arrivata una richiesta di ripristino della Dialog distrutta, da cui ho solo l'Id dell'immagine
            LinearLayout sc = (LinearLayout) findViewById(R.id.fotoScroll);
            ImageView image=new ImageView(this);
            //cerco l'immagine da impostare nella Dialog
            for (int z = 0; z < sc.getChildCount(); ++z) {
                image = (ImageView) sc.getChildAt(z);
                int currentViewId =image.getId();
                //se ho trovato la giusta View
                if (currentViewId == id) {
                    //imposto l'immagine nella mia nuova Dialog
                    final Bitmap yourBitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
                    final Bitmap bitmap = Bitmap.createScaledBitmap(yourBitmap, 500, 500, true);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] bytearr = stream.toByteArray();
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytearr, 0, bytearr.length);
                    //se tutto é andato bene metto l'immagine nella mia Dialog
                    if (bmp != null) {
                      //  mImageView.setImageBitmap(bmp);
                        //qui inserisco l'immagine nel database e il primo é 1 poiché non ho ancora piu ScrollView
                        repo.insert(1, id, " ", bmp.toString());
                    }
                }
            }//end for
        }

        //se qualcuno clicca su bottone di salvataggio della descrizione
        saveDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message= testo.getText().toString();
                message= message.substring(0, Math.min(message.length(), 50000));
                // devo fare l'UPDATE della descrizione nel database
                repo.update(1, idImageViewPage, message);
                //tolgo la dialog
                presentDialog.dismiss();
            }
        });

        //se qualcuno clicca sul pulsante di rimozione dell'immagine
        removeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rimuoviImmagine(idImageViewPage);
                presentDialog.dismiss();
            }
        });

        //se qualcuno clicca sul FAB
      /*  myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //da implementare cosa bisogna fare
                newLayout();
            }
        }); **/

        //mi serve per capire quando la mia Dialog é attiva o meno
        presentDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //la Dialog é stata dismessa
                visibilityDialog = false;
            }
        });



        //mi serve per sapere sempre che immagine ho nella dialog
        idImageDialog =idImageViewPage;
        //tengo a mente che il la Dialog é presente
        visibilityDialog=true;
        //faccio vedere la dialog


        final ViewPager mViewPager = (ViewPager) presentDialog.findViewById(R.id.viewPageAndroid);
        AndroidImageAdapter adapterView = new AndroidImageAdapter(MainActivity.this, listUri, conta, videoUri);
        mViewPager.setAdapter(adapterView);

        LinearLayout scroll = (LinearLayout) findViewById(R.id.fotoScroll);

        //serve per impostare l'immagine con cui si apre la Dialog
        int numImmagine=0;

        for (int z = 0; z < scroll.getChildCount(); ++z) {
            int currentViewId = scroll.getChildAt(z).getId();

            //se l'id corrisposnde a quello dell'immagine da cancellare
            if (currentViewId == idImageViewPage) {
                numImmagine=z;
            }
        } // end for

        mViewPager.setCurrentItem(numImmagine);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
               LinearLayout sc = (LinearLayout) findViewById(R.id.fotoScroll);
               idImageViewPage =  sc.getChildAt(position).getId();
               idImageDialog =idImageViewPage;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        presentDialog.show();


    }

    public Uri bitmapToUriConverter(Bitmap mBitmap) {
        Uri uri = null;
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            // Calculate inSampleSize
            Bitmap newBitmap = Bitmap.createScaledBitmap(mBitmap, mBitmap.getWidth(), mBitmap.getHeight(), true);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;

            File file = new File(MainActivity.this.getFilesDir(), "Image"
                    + new Random().nextInt() + ".jpeg");
            FileOutputStream out = MainActivity.this.openFileOutput(file.getName(), MODE_PRIVATE); //sono incerto se MODE_PRIVATE sia la scelta migliore
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            //get absolute path
            String realPath = file.getAbsolutePath();
            File f = new File(realPath);
            uri = Uri.fromFile(f);

        } catch (Exception e) {
            Log.e("Your Error Message", e.getMessage());
        }
        return uri;
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }



}