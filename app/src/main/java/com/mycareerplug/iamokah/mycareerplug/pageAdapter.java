package com.mycareerplug.iamokah.mycareerplug;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.viewpager.widget.PagerAdapter;
import androidx.appcompat.app.AlertDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class pageAdapter extends PagerAdapter {

    public static final String DIALOG_PREF = "MyPrefsFile1";

    private List<CardModel> modelList;
    private LayoutInflater layoutInflater;
    private Context context;

    FirebaseAuth mAuth;
    ArrayList<String> programNames = new ArrayList<>();
    ArrayList<String> interested_Deadlines = new ArrayList<>();
    ArrayList<Integer> interested_modelarray_pos = new ArrayList<>();

    ArrayList<CardModel> interested_modelList = new ArrayList<>();
    ArrayList<CardModel> thrash_modelList = new ArrayList<>();

    ArrayList<String> thrash_programNames = new ArrayList<>();
    ArrayList<String> thrash_Deadlines = new ArrayList<>();

    boolean isConfirmed = false;
    TinyDB tinyDB;

    public pageAdapter(List<CardModel> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        layoutInflater = LayoutInflater.from(context);
        final View cardview = layoutInflater.inflate(R.layout.card_item, container, false);

        tinyDB = new TinyDB(context);

        TextView institution = (TextView) cardview.findViewById(R.id.institutionrealid);
        TextView programname = (TextView) cardview.findViewById(R.id.programrealid);
        TextView deadline = (TextView) cardview.findViewById(R.id.event_typez);
        TextView cost = (TextView) cardview.findViewById(R.id.city);
        TextView financialaid = (TextView) cardview.findViewById(R.id.program_id);
        TextView pay = (TextView) cardview.findViewById(R.id.descrip);
        FloatingActionButton shareButton = (FloatingActionButton) cardview.findViewById(R.id.floatingActionButton3);
        final FloatingActionButton checkButton = (FloatingActionButton) cardview.findViewById(R.id.floatingActionButton2);
        FloatingActionButton thrashButton = (FloatingActionButton) cardview.findViewById(R.id.floatingActionButton);

        final CardModel model = modelList.get(position);
        institution.setText(model.getInstname());
        TextView programnameTextView = cardview.findViewById(R.id.textView16);
        if (model.getInstname().isEmpty()) {
            programnameTextView.setVisibility(View.GONE);
            programname.setVisibility(View.GONE);
        } else {
            programname.setText(model.getProgramname());
        }

        TextView deadline_text = (TextView) cardview.findViewById(R.id.deadline_tv);
        if (model.get_tense().equals("Upcoming")) {
            if (model.getOpenDate().isEmpty() || model.getOpenDate().equals("Varies")) {
                deadline.setVisibility(View.GONE);
                deadline_text.setVisibility(View.GONE);
                System.out.println("Program name " + model.getInstname() + " is empty o-date is " + model.getOpenDate());
            } else {
                deadline_text.setText("Status");
                deadline.setText("Application opens on " + model.getOpenDate());
             //   int days = Days.daysBetween(date1, date2).getDays();
                DateFormat dateFormat = new SimpleDateFormat("MM/dd");
                Date date = new Date();
                Date c_date = stringToDate(dateFormat.format(date));
                Date o_date = stringToDate(model.getOpenDate());
               // System.out.println("c_date " + c_date.toString() + " o_date " + o_date.toString() + " getdays is " + getDaysDifference(c_date, o_date));
                if (c_date != null) {
                    if (Math.abs(getDaysDifference(c_date, o_date)) != 0) {
                        deadline.setText("Application opens in " + Math.abs(getDaysDifference(c_date, o_date)) + " days.");
                    }
                }

            }

        } else if (model.get_tense().equals("Current")) {
            if (model.getDeadline().isEmpty() || model.getDeadline().equals("Varies")) {
                if (!model.getOpenDate().isEmpty()) {
                    deadline_text.setText("Status");
                    deadline.setText("Application Open");
                } else {
                    deadline.setVisibility(View.GONE);
                    deadline_text.setVisibility(View.GONE);
                }
            } else {
                deadline.setText(model.getDeadline());
            }
        } else {
            if (model.getDeadline().equals("") || model.getDeadline().equals("Varies")) {
                deadline.setVisibility(View.GONE);
                deadline_text.setVisibility(View.GONE);
            } else {
                deadline.setText(model.getDeadline());
            }
        }
        TextView cost_text = (TextView) cardview.findViewById(R.id.textView11);
        if (model.getCost().equals("")) {
            cost_text.setVisibility(View.GONE);
            cost.setVisibility(View.GONE);
        } else {
            cost.setText(model.getCost());
        }
        TextView financialaid_text = cardview.findViewById(R.id.event_type);

        if (model.getFinancialaid().equals("")) {
            financialaid_text.setVisibility(View.GONE);
            financialaid.setVisibility(View.GONE);
        } else {
            financialaid.setText(model.getFinancialaid());
        }
        TextView pay_text = cardview.findViewById(R.id.program_text);
        if (model.getPay().equals("")) {
            pay_text.setVisibility(View.GONE);
            pay.setVisibility(View.GONE);
        } else {
            pay.setText(model.getPay());
        }

        if (model.get_tense().equals("Interested")) {
            System.out.println("Inside interested model");
            checkButton.setEnabled(false);

            checkButton.setBackgroundTintList(ColorStateList.valueOf
                    (context.getResources().getColor(R.color.darkthemelight)));
        } else {
            checkButton.setEnabled(true);
            checkButton.setBackgroundTintList(ColorStateList.valueOf
                    (context.getResources().getColor(R.color.darkthemeblack)));
        }


        TextView tag_text = cardview.findViewById(R.id.tag_id);

        String tagString = "";
        tagString = setTagString(model.getCategory());
        //replace two or more spaces with 1 space.
       // tagString = tagString.replaceAll("\\s{2,}","  ");
       // tagString = tagString.replaceAll("\\s{2,}",", ");
        tag_text.setText(tagString);
       // System.out.println(tagString);


        final TextView notification_tv = (TextView) cardview.findViewById(R.id.textView18);

       /* if (model.get_is_new()) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    // yourMethod();
                    notification_tv.setVisibility(View.GONE);
                    model.set_is_new(false);
                }
            }, 5000);   //5 seconds
        } else {
            notification_tv.setVisibility(View.GONE);
        } */

       if (model.get_tense().equals("Current") || model.get_tense().equals("Upcoming")) {
           System.out.println("INSIDE current or upcoming get_new is " + model.get_is_new());
           if (model.get_is_new() == false) {
               System.out.println("inside model not new for " + model.getInstname());
               notification_tv.setVisibility(View.GONE);
           }
       } /*else if (model.get_tense().equals("Upcoming")) {
           Handler handler = new Handler();
           handler.postDelayed(new Runnable() {
               public void run() {
                   // yourMethod();
                   notification_tv.setVisibility(View.GONE);
                   model.set_is_new(false);
               }
           }, 5000);   //5 seconds
       }*/

       ImageView imageView = cardview.findViewById(R.id.media_image);
       TextView youtube_text_hint = cardview.findViewById(R.id.youtube_text);
       if (!model.getVideo_link().isEmpty()) {
           youtube_text_hint.setVisibility(View.VISIBLE);
           youtube_text_hint.setText("Tap on youtube logo to watch video.");
           imageView.setClickable(true);
           imageView.setImageResource(R.drawable.youtube_image);

           imageView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   String url = model.getVideo_link();
                   Intent i = new Intent(Intent.ACTION_VIEW);
                   i.setData(Uri.parse(url));
                   try {
                       context.startActivity(i);
                   } catch (ActivityNotFoundException e) {
                       e.printStackTrace();
                      // Toast.makeText(context,"No additional information on this item.", Toast.LENGTH_LONG).show();
                   }
               }
           });
       } else {
           youtube_text_hint.setVisibility(View.GONE);
           imageView.setClickable(false);
           imageView.setImageResource(R.drawable.business);
       }



        TextView learnmore = cardview.findViewById(R.id.learn_more_btn);

        learnmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.get_is_new()) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            // yourMethod();
                            notification_tv.setVisibility(View.GONE);
                            model.set_is_new(false);
                        }
                    }, 2000);   //5 seconds
                }

                final FirebaseAuth mAuth = FirebaseAuth.getInstance();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.child("user").child("user_data").child(mAuth.getUid()).child("old_values").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        System.out.println("printing children " + dataSnapshot.getChildrenCount());
                        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
                        if (dataSnapshot.getChildrenCount() == 0) {
                            mRef.child("user").child("user_data").child(mAuth.getUid()).child("old_values").child("0").setValue(
                                    new CardModel(model.getInstname(), model.getProgramname(), model.getDeadline(),
                                            model.getCost(), model.getFinancialaid() , model.getPay()
                                            , model.getUrlLink(),model.getOpenDate(), model.getType_of_event(),
                                            model.getVideo_link(), model.getArray_pos(), model.get_tense(),
                                            model.getCategory(), false), new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                            if (databaseError != null) {
                                                System.out.println("OLD Data could not be saved " + databaseError.getMessage());
                                            } else {
                                                System.out.println("OLD Data saved successfully.");
                                             //   Toast.makeText(context, "Saving " + model
                                             //           .getProgramname(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            boolean duplicate = false;
                            for(int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                                String inst = dataSnapshot.child(Integer.toString(i)).child("instname").getValue().toString();
                                String programname = dataSnapshot.child(Integer.toString(i)).child("programname").getValue().toString();
                                CardModel dataModel = dataSnapshot.child(Integer.toString(i)).getValue(CardModel.class);
                                //  if (inst.equals(model.getInstname()) && programname.equals(model.getProgramname())) {
                                if (model.equals(dataModel)) {
                                    duplicate = true;
                                  //  Toast.makeText(context, "Item already saved", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                                System.out.println("printing inst " + inst);
                            }
                            if (duplicate == false) {
                                mRef.child("user").child("user_data").child(mAuth.getUid()).child("old_values").child(Long.toString(dataSnapshot.getChildrenCount())).setValue(
                                        new CardModel(model.getInstname(), model.getProgramname(), model.getDeadline(),
                                                model.getCost(), model.getFinancialaid() , model.getPay()
                                                , model.getUrlLink(),model.getOpenDate(), model.getType_of_event(),
                                                model.getVideo_link(), model.getArray_pos(), model.get_tense(),
                                                model.getCategory(), false), new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                                if (databaseError != null) {
                                                    System.out.println("OLD Data could not be saved " + databaseError.getMessage());
                                                } else {
                                                    System.out.println("OLD Data saved successfully.");
                                               //     Toast.makeText(context, "Saving " + model
                                                //            .getProgramname(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                String url = model.getUrlLink();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));

                try {
                    context.startActivity(i);
                } catch ( ActivityNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(context,"No additional information on this item.", Toast.LENGTH_LONG).show();
                }

            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* FirebaseAuth mAuth = FirebaseAuth.getInstance();
                DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
                Toast.makeText(context,"Share button not yet implemented.", Toast.LENGTH_LONG).show();
                //WRITE TO DATABASE
                mRef.child("user").child("user_data").child(mAuth.getUid()).setValue(
                        new User("Name1", "Email1", "Phone1"), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    System.out.println("Data could not be saved " + databaseError.getMessage());
                                } else {
                                    System.out.println("Data saved successfully.");
                                }
                            }
                        });*/

               //READ PHONE DATA
               /* DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.child("user").child("user_data").orderByChild("phone").equalTo("4436825503").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       // Log.v("LOGINACTIVITY", "datasnapshot " + dataSnapshot);
                        if (dataSnapshot.exists()) {
                            Log.v("LOGINACTIVITY", "datasnapshot " + dataSnapshot);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });*/

             /*   final FirebaseAuth mAuth = FirebaseAuth.getInstance();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.child("user").child("user_data").child(mAuth.getUid()).child("interested").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        System.out.println("printing children " + dataSnapshot.getChildrenCount());
                        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
                        if (dataSnapshot.getChildrenCount() == 0) {

                            mRef.child("user").child("user_data").child(mAuth.getUid()).child("interested").child("0").setValue(
                                    new CardModel(model.getInstname(), model.getProgramname(), model.getDeadline(),
                                            model.getCost(), model.getFinancialaid() , model.getPay()
                                            , model.getUrlLink(),model.getOpenDate(), model.getType_of_event(),
                                            model.getVideo_link(), model.getArray_pos(), model.get_tense(),
                                            model.getCategory(), model.get_is_new()), new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                            if (databaseError != null) {
                                                System.out.println("Data could not be saved " + databaseError.getMessage());
                                            } else {
                                                System.out.println("INTEREST Data saved successfully.");
                                            }
                                        }
                                    });
                        } else {
                            boolean duplicate = false;
                            for(int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                                String inst = dataSnapshot.child(Integer.toString(i)).child("instname").getValue().toString();
                                String programname = dataSnapshot.child(Integer.toString(i)).child("programname").getValue().toString();

                                if (inst.equals(model.getInstname()) && programname.equals(model.getProgramname())) {
                                    duplicate = true;
                                    Toast.makeText(context, "Item already saved", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                                System.out.println("printing inst " + inst);
                            }
                            if (duplicate == false) {
                                mRef.child("user").child("user_data").child(mAuth.getUid()).child("interested").child(Long.toString(dataSnapshot.getChildrenCount())).setValue(
                                        new CardModel(model.getInstname(), model.getProgramname(), model.getDeadline(),
                                                model.getCost(), model.getFinancialaid() , model.getPay()
                                                , model.getUrlLink(),model.getOpenDate(), model.getType_of_event(),
                                                model.getVideo_link(), model.getArray_pos(), model.get_tense(),
                                                model.getCategory(), model.get_is_new()), new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                                if (databaseError != null) {
                                                    System.out.println("Data could not be saved " + databaseError.getMessage());
                                                } else {
                                                    System.out.println("INTEREST Data saved successfully.");
                                                }
                                            }
                                        });
                            }
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }); */



                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = model.getUrlLink();
                //displayed as subject in emails like gmail.
                String subject = "CareerPlug presents an opportunity";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                context.startActivity(Intent.createChooser(sharingIntent, "Share via"));

            }
        });

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          /*      SharedPreferences prefs = PreferenceManager
                        .getDefaultSharedPreferences(context);
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(1);
                jsonArray.put(2);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("key", jsonArray.toString());
                System.out.println(jsonArray.toString());
                editor.commit(); */
          /*      DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();

                interested_modelList = tinyDB.getListObject("interested_modelList_key", CardModel.class);
                CardModel interestModel = model;
                interestModel.setGet_tense("Interested");
                System.out.println("GetTense " + interestModel.get_tense());


                mRef.child("user").child("user_data").child(mAuth.getUid()).child("interested").push().setValue(
                        new CardModel(model.getInstname(), model.getProgramname(), model.getDeadline(),
                                model.getCost(), model.getFinancialaid() , model.getPay()
                                , model.getUrlLink(),model.getOpenDate(), model.getType_of_event(),
                                model.getVideo_link(), model.getArray_pos(), model.get_tense(),
                                model.getCategory(), model.get_is_new()), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    System.out.println("Data could not be saved " + databaseError.getMessage());
                                } else {
                                    System.out.println("INTEREST Data saved successfully.");
                                }
                            }
                        });
                checkButton.setEnabled(false);

                if (interested_modelList.size() == 0) {

                    interested_modelList.add(interestModel);
                    tinyDB.putListObject("interested_modelList_key", interested_modelList);
                    /*programNames.add(modelList.get(position).getProgramname());
                    interested_Deadlines.add(modelList.get(position).getDeadline());
                    interested_modelarray_pos.add(modelList.get(position).getArray_pos()); */

                    //WRITE TO DATABASE

            /*
                    Toast.makeText(context, "Saving " + modelList.get(position)
                            .getProgramname(), Toast.LENGTH_SHORT).show();

                } else {
                    if (!interested_modelList.contains(model)) {
                        interested_modelList.add(interestModel);
                        tinyDB.putListObject("interested_modelList_key", interested_modelList);
                        /*programNames.add(modelList.get(position).getProgramname());
                        interested_Deadlines.add(modelList.get(position).getDeadline());
                        interested_modelarray_pos.add(modelList.get(position).getArray_pos());
                        Toast.makeText(context, "Saving " + modelList.get(position)
                                .getProgramname(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Item already saved", Toast.LENGTH_SHORT).show();
                    }
                }


                System.out.println("Printing POSITION " + position);

                System.out.println("Printing programName size " + programNames.size() +  " int_deadline size " + interested_Deadlines.size());

          //      saveArrayList(programNames, "interested_program_key");
           //     saveArrayList(interested_Deadlines, "interested_deadline_key");
               // saveArrayList(interested_modelarray_pos, "interested_arraypos_key");
               // TinyDB tinyDB = new TinyDB(context);
             /*   tinyDB.putListInt("interested_arraypos_key", interested_modelarray_pos);
                tinyDB.putListString("interested_program_keyz", programNames);
                tinyDB.putListString("interested_deadline_keyz", interested_Deadlines); */
                final FirebaseAuth mAuth = FirebaseAuth.getInstance();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.child("user").child("user_data").child(mAuth.getUid()).child("interested").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        System.out.println("printing children " + dataSnapshot.getChildrenCount());
                        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
                        if (dataSnapshot.getChildrenCount() == 0) {

                            mRef.child("user").child("user_data").child(mAuth.getUid()).child("interested").child("0").setValue(
                                    new CardModel(model.getInstname(), model.getProgramname(), model.getDeadline(),
                                            model.getCost(), model.getFinancialaid() , model.getPay()
                                            , model.getUrlLink(),model.getOpenDate(), model.getType_of_event(),
                                            model.getVideo_link(), model.getArray_pos(), model.get_tense(),
                                            model.getCategory(), model.get_is_new()), new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                            if (databaseError != null) {
                                                System.out.println("Data could not be saved " + databaseError.getMessage());
                                            } else {
                                                System.out.println("INTEREST Data saved successfully.");
                                                Toast.makeText(context, "Saving " + model
                                                        .getProgramname(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            boolean duplicate = false;
                            for(int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                                String inst = dataSnapshot.child(Integer.toString(i)).child("instname").getValue().toString();
                                String programname = dataSnapshot.child(Integer.toString(i)).child("programname").getValue().toString();
                                CardModel dataModel = dataSnapshot.child(Integer.toString(i)).getValue(CardModel.class);
                              //  if (inst.equals(model.getInstname()) && programname.equals(model.getProgramname())) {
                                if (model.equals(dataModel)) {
                                    duplicate = true;
                                    Toast.makeText(context, "Item already saved", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                                System.out.println("printing inst " + inst);
                            }
                            if (duplicate == false) {
                                mRef.child("user").child("user_data").child(mAuth.getUid()).child("interested").child(Long.toString(dataSnapshot.getChildrenCount())).setValue(
                                        new CardModel(model.getInstname(), model.getProgramname(), model.getDeadline(),
                                                model.getCost(), model.getFinancialaid() , model.getPay()
                                                , model.getUrlLink(),model.getOpenDate(), model.getType_of_event(),
                                                model.getVideo_link(), model.getArray_pos(), model.get_tense(),
                                                model.getCategory(), model.get_is_new()), new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                                if (databaseError != null) {
                                                    System.out.println("Data could not be saved " + databaseError.getMessage());
                                                } else {
                                                    System.out.println("INTEREST Data saved successfully.");
                                                    Toast.makeText(context, "Saving " + model
                                                            .getProgramname(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });

        thrashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCancelDialog(model, position);
            //    saveArrayList(thrash_programNames, "thrash_program_key");
             //   saveArrayList(thrash_Deadlines, "thrash_deadline_key");

           //     TinyDB tinyDB = new TinyDB(context);
                //tinyDB.putListInt("interested_arraypos_key", interested_modelarray_pos);
            //    tinyDB.putListString("thrash_program_keyz", thrash_programNames);
            //    tinyDB.putListString("thrash_deadline_keyz", thrash_Deadlines);

            }
        });

        container.addView(cardview, 0);

        return cardview;
    }

    public void removeView(int index) {
        modelList.remove(index);
        notifyDataSetChanged();
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    private String setTagString(String tagString) {
        int counter = 0;
        String tag = "";
        if (context != null) {
            TinyDB tinyDB = new TinyDB(context);
            ArrayList<String> get_interest_array = tinyDB.getListString("interestArray");

    /*    for (int i = 0; i < get_interest_array.size(); i++) {
            System.out.println("PRINTING INTEREST ARRAY " + i + " val" + get_interest_array.get(i));
        } */
            for (int i = 0; i < get_interest_array.size(); i++) {
                // System.out.println("Catagory is " + catagory);
                // System.out.println("getInterestVal " + get_interest_array.get(i));
                if (tagString.contains(get_interest_array.get(i))) {
                    //System.out.println("return true ");

                    if (counter == 0) {
                        if (get_interest_array.size() > 1) {
                            tag += get_interest_array.get(i) + ", ";
                        } else {
                            tag += get_interest_array.get(i);
                        }
                        counter++;
                    } else if (counter == 1) {
                        tag += get_interest_array.get(i);
                        counter++;
                    } else {
                        break;
                    }
                }
            }
        }
        System.out.println("COUNTER IS " + counter);

        if (tag.contains(",") && counter == 1) {
            tag = tag.substring(0, tag.length() - 2);
        }


        return tag;
    }



    public static int getDaysDifference(Date fromDate,Date toDate) {
        if(fromDate==null||toDate==null)
            return 0;

        return (int)( (toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24));
    }

    private static Date stringToDate(String date) {
        //   System.out.println("date " + date);
        try {
            if (date.contains("-")) {
                return new SimpleDateFormat("dd-MMM").parse(date);
            } else if (date.contains("/")) {
                return new SimpleDateFormat("MM/dd").parse(date);
            }
        } catch (ParseException e) {
            return null;
        }


        return null;
    }

    private void showCancelDialog(final CardModel model, final int position) {
        System.out.println("start showcancel");
        //  isConfirmed = false;
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        LayoutInflater adbInflater = LayoutInflater.from(context);
        View mView = adbInflater.inflate(R.layout.checkbox, null);
        final CheckBox dontShowAgain = mView.findViewById(R.id.skip);
        final HomeActivity activity = (HomeActivity) context;
        SharedPreferences settings = activity.getSharedPreferences(DIALOG_PREF, Context.MODE_PRIVATE);
        String skipMessage = settings.getString("skipMessage", "NOT checked");

        mBuilder.setTitle("Confirm Not Interested?");
        mBuilder.setMessage("Are you sure you are not interested in this item? The record will be removed and sent to not interested.");
        mBuilder.setView(mView);

        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String checkBoxResult = "NOT checked";

                if (dontShowAgain.isChecked()) {
                    checkBoxResult = "checked";
                }

                SharedPreferences settings = activity.getSharedPreferences(DIALOG_PREF, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();

                editor.putString("skipMessage", checkBoxResult);
                editor.commit();

                // Do what you want to do on "OK" action
                //     isConfirmed = true;

                System.out.println("inside thrashbtn");

                final FirebaseAuth mAuth = FirebaseAuth.getInstance();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.child("user").child("user_data").child(mAuth.getUid()).child("thrash").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        System.out.println("printing children " + dataSnapshot.getChildrenCount());
                        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
                        if (dataSnapshot.getChildrenCount() == 0) {

                            mRef.child("user").child("user_data").child(mAuth.getUid()).child("thrash").child("0").setValue(
                                    new CardModel(model.getInstname(), model.getProgramname(), model.getDeadline(),
                                            model.getCost(), model.getFinancialaid() , model.getPay()
                                            , model.getUrlLink(),model.getOpenDate(), model.getType_of_event(),
                                            model.getVideo_link(), model.getArray_pos(), model.get_tense(),
                                            model.getCategory(), model.get_is_new()), new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                            if (databaseError != null) {
                                                System.out.println("Data could not be saved " + databaseError.getMessage());
                                            } else {
                                                System.out.println("THRASH Data saved successfully.");
                                                Toast.makeText(context, "Sending " + model
                                                        .getProgramname() + " to not interested.", Toast.LENGTH_SHORT).show();
                                                removeView(position);
                                            }
                                        }
                                    });
                        } else {
                            boolean duplicate = false;
                            for(int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                                String inst = dataSnapshot.child(Integer.toString(i)).child("instname").getValue().toString();
                                String programname = dataSnapshot.child(Integer.toString(i)).child("programname").getValue().toString();
                                CardModel dataModel = dataSnapshot.child(Integer.toString(i)).getValue(CardModel.class);
                                //  if (inst.equals(model.getInstname()) && programname.equals(model.getProgramname())) {
                                if (model.equals(dataModel)) {
                                    duplicate = true;
                                    Toast.makeText(context, "Item already sent to  not interested", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                                System.out.println("printing inst " + inst);
                            }
                            if (duplicate == false) {
                                mRef.child("user").child("user_data").child(mAuth.getUid()).child("thrash").child(Long.toString(dataSnapshot.getChildrenCount())).setValue(
                                        new CardModel(model.getInstname(), model.getProgramname(), model.getDeadline(),
                                                model.getCost(), model.getFinancialaid() , model.getPay()
                                                , model.getUrlLink(),model.getOpenDate(), model.getType_of_event(),
                                                model.getVideo_link(), model.getArray_pos(), model.get_tense(),
                                                model.getCategory(), model.get_is_new()), new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                                if (databaseError != null) {
                                                    System.out.println("Data could not be saved " + databaseError.getMessage());
                                                } else {
                                                    System.out.println("THRASH Data saved successfully.");
                                                    Toast.makeText(context, "Sending " + model
                                                            .getProgramname() + " to not interested.", Toast.LENGTH_SHORT).show();
                                                    removeView(position);
                                                }
                                            }
                                        });
                            }
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                /*
                thrash_modelList = tinyDB.getListObject("thrash_modelList_key", CardModel.class);

                if (thrash_modelList.size() == 0) {
                   /* thrash_programNames.add(modelList.get(position).getProgramname());
                    thrash_Deadlines.add(modelList.get(position).getDeadline());
                    thrash_modelList.add(model);
                    tinyDB.putListObject("thrash_modelList_key", thrash_modelList);

                    Toast.makeText(context, "Sending " + modelList.get(position)
                            .getProgramname() + " to not interested.", Toast.LENGTH_LONG).show();

                    removeView(position);

                } else {
                    if (!thrash_modelList.contains(model)) {
                        thrash_modelList.add(model);
                        tinyDB.putListObject("thrash_modelList_key", thrash_modelList);

                        /*thrash_programNames.add(modelList.get(position).getProgramname());
                        thrash_Deadlines.add(modelList.get(position).getDeadline()); */
              /*          Toast.makeText(context, "Sending " + modelList.get(position)
                                .getProgramname() + "to not interested.", Toast.LENGTH_LONG).show();
                        removeView(position);
                    } else {
                        Toast.makeText(context, "Item already sent to  not interested", Toast.LENGTH_SHORT).show();
                    }
                }
                */


                dialog.dismiss();
                System.out.println("End of yes btn.");
            }
        });

        mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String checkBoxResult = "NOT checked";

                if (dontShowAgain.isChecked()) {
                    checkBoxResult = "checked";
                }

                SharedPreferences settings = activity.getSharedPreferences(DIALOG_PREF, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();

                editor.putString("skipMessage", checkBoxResult);
                editor.commit();

                // Do what you want to do on "CANCEL" action
                isConfirmed = false;
            }
        });

        if (!skipMessage.equals("checked")) {
            mBuilder.show();
        } else {
            System.out.println("Cancel btn not showing");

            final FirebaseAuth mAuth = FirebaseAuth.getInstance();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.child("user").child("user_data").child(mAuth.getUid()).child("thrash").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    System.out.println("printing children " + dataSnapshot.getChildrenCount());
                    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
                    if (dataSnapshot.getChildrenCount() == 0) {

                        mRef.child("user").child("user_data").child(mAuth.getUid()).child("thrash").child("0").setValue(
                                new CardModel(model.getInstname(), model.getProgramname(), model.getDeadline(),
                                        model.getCost(), model.getFinancialaid() , model.getPay()
                                        , model.getUrlLink(),model.getOpenDate(), model.getType_of_event(),
                                        model.getVideo_link(), model.getArray_pos(), model.get_tense(),
                                        model.getCategory(), model.get_is_new()), new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        if (databaseError != null) {
                                            System.out.println("Data could not be saved " + databaseError.getMessage());
                                        } else {
                                            System.out.println("THRASH Data saved successfully.");
                                            Toast.makeText(context, "Sending " + model
                                                    .getProgramname() + " to not interested.", Toast.LENGTH_SHORT).show();
                                            removeView(position);
                                        }
                                    }
                                });
                    } else {
                        boolean duplicate = false;
                        for(int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                            String inst = dataSnapshot.child(Integer.toString(i)).child("instname").getValue().toString();
                            String programname = dataSnapshot.child(Integer.toString(i)).child("programname").getValue().toString();
                            CardModel dataModel = dataSnapshot.child(Integer.toString(i)).getValue(CardModel.class);
                            //  if (inst.equals(model.getInstname()) && programname.equals(model.getProgramname())) {
                            if (model.equals(dataModel)) {
                                duplicate = true;
                                Toast.makeText(context, "Item already sent to  not interested", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            System.out.println("printing inst " + inst);
                        }
                        if (duplicate == false) {
                            mRef.child("user").child("user_data").child(mAuth.getUid()).child("thrash").child(Long.toString(dataSnapshot.getChildrenCount())).setValue(
                                    new CardModel(model.getInstname(), model.getProgramname(), model.getDeadline(),
                                            model.getCost(), model.getFinancialaid() , model.getPay()
                                            , model.getUrlLink(),model.getOpenDate(), model.getType_of_event(),
                                            model.getVideo_link(), model.getArray_pos(), model.get_tense(),
                                            model.getCategory(), model.get_is_new()), new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                            if (databaseError != null) {
                                                System.out.println("Data could not be saved " + databaseError.getMessage());
                                            } else {
                                                System.out.println("THRASH Data saved successfully.");
                                                Toast.makeText(context, "Sending " + model
                                                        .getProgramname() + " to not interested.", Toast.LENGTH_SHORT).show();
                                                removeView(position);
                                            }
                                        }
                                    });
                        }
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            /*
            thrash_modelList = tinyDB.getListObject("thrash_modelList_key", CardModel.class);

            if (thrash_modelList.size() == 0) {
                   /* thrash_programNames.add(modelList.get(position).getProgramname());
                    thrash_Deadlines.add(modelList.get(position).getDeadline());
                thrash_modelList.add(model);
                tinyDB.putListObject("thrash_modelList_key", thrash_modelList);

                Toast.makeText(context, "Sending " + modelList.get(position)
                        .getProgramname() + " to not interested.", Toast.LENGTH_LONG).show();
                removeView(position);

            } else {
                if (!thrash_modelList.contains(model)) {
                    thrash_modelList.add(model);
                    tinyDB.putListObject("thrash_modelList_key", thrash_modelList);

                        /*thrash_programNames.add(modelList.get(position).getProgramname());
                        thrash_Deadlines.add(modelList.get(position).getDeadline());
                    Toast.makeText(context, "Sending " + modelList.get(position)
                            .getProgramname() + "to not interested.", Toast.LENGTH_LONG).show();
                    removeView(position);
                } else {
                    Toast.makeText(context, "Item already sent to  not interested", Toast.LENGTH_SHORT).show();
                }
            }
        }
        System.out.println("before isConfirmed return " + isConfirmed); */

        }

    }
}
