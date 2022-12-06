package com.mycareerplug.iamokah.mycareerplug;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.NOTIFICATION_SERVICE;

public class CustomListAdapter extends ArrayAdapter {
    //to reference the Activity
    private final Activity context;
    private final ArrayList<CardModel> modelList;
    //to store the list of countries
 //   private final ArrayList<String> nameArray;

    //to store the list of countries
//    private final ArrayList<String> infoArray;

//    private final ArrayList<String> instNameArray;

    public CustomListAdapter(Activity context, ArrayList<CardModel> modelList){

        super(context, R.layout.listview_row , modelList);

        this.context = context;
        this.modelList = modelList;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.listview_row, null,true);
        createNotificationChannel();
        //this code gets references to objects in the listview_row.xml file
        TextView nameTextField = (TextView) rowView.findViewById(R.id.textprogramname);
        TextView infoTextField = (TextView) rowView.findViewById(R.id.textdeadline);
        TextView instName = (TextView) rowView.findViewById(R.id.instname);
        TextView deadlineTextView = (TextView) rowView.findViewById(R.id.textView15);
        TextView day_tag = (TextView) rowView.findViewById(R.id.day_tag);
        TextView day_tag_helper_text = (TextView) rowView.findViewById(R.id.textView21);

        //this code sets the values of the objects to values from the arrays
        instName.setText(modelList.get(position).getInstname());
        if (modelList.get(position).getDeadline().isEmpty() &&
                !modelList.get(position).getOpenDate().isEmpty()) {
            deadlineTextView.setText("Application opens on " + modelList.get(position).getOpenDate());
        } else if (!modelList.get(position).getDeadline().isEmpty()) {
            deadlineTextView.setText("Application deadline: " + modelList.get(position).getDeadline());
        } else {
            deadlineTextView.setVisibility(View.GONE);
        }
        infoTextField.setText(modelList.get(position).getDeadline());
        nameTextField.setText(modelList.get(position).getProgramname());

        DateFormat dateFormat = new SimpleDateFormat("MM/dd");
        Date date = new Date();
        Date c_date = stringToDate(dateFormat.format(date));
        Date o_date = stringToDate(modelList.get(position).getOpenDate());
        Date d_date = stringToDate(modelList.get(position).getDeadline());
        System.out.println("Print c_date " + c_date);
        System.out.println("Print o_date " + o_date);
        System.out.println("Print deadline " + d_date);
        System.out.println("Print daysdiff " + Math.abs(getDaysDifference(c_date, o_date)));
        if (modelList.get(position).getOpenDate() != null) {
            if (Math.abs(getDaysDifference(c_date, o_date)) <= 14 && Math.abs(getDaysDifference(c_date, o_date)) > 0) {
                //  shownotification;
                Activity homeActivity = (HomeActivity) context;
                // Create an explicit intent for an Activity in your app
                Intent intent = new Intent(homeActivity, InterestsFragment.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(homeActivity, 0, intent, 0);
                PendingIntent urlPendingIntent = null;
                if (modelList.get(position).getUrlLink() != null) {
                    if (!modelList.get(position).getUrlLink().isEmpty()) {
                        String url = modelList.get(position).getVideo_link();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                       /* try {
                           // context.startActivity(i);
                        } catch (ActivityNotFoundException e) {
                            e.printStackTrace();
                            Toast.makeText(context,"URL is currently not working", Toast.LENGTH_LONG).show();
                        } */
                         urlPendingIntent =
                                PendingIntent.getBroadcast(homeActivity, 0, i, 0);
                    }
                }


                String textTitle = "Application opens in " + Math.abs(getDaysDifference(c_date, o_date)) + " days.";
                String textContent = modelList.get(position).getInstname() + " 's application will open in " + Math.abs(getDaysDifference(c_date, o_date));
                NotificationCompat.Builder builder;
                if (urlPendingIntent != null) {
                    builder = new NotificationCompat.Builder(context, "notification_channel_id")
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle(textTitle)
                            .setContentText(textContent)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setContentIntent(pendingIntent)
                            .addAction(R.drawable.ic_launcher, "View Details", urlPendingIntent)
                            .setAutoCancel(true);
                    System.out.println("test");
                } else {
                    builder = new NotificationCompat.Builder(context, "notification_channel_id")
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle(textTitle)
                            .setContentText(textContent)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true);
                    System.out.println("test1");
                }
                System.out.println("test");

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(homeActivity);
                int  notificationId = position;
                // notificationId is a unique int for each notification that you must define
                notificationManager.notify(position, builder.build());

                int daysleft = Math.abs(getDaysDifference(c_date, o_date));
                day_tag_helper_text.setVisibility(View.VISIBLE);
                day_tag.setVisibility(View.VISIBLE);
                day_tag.setText(Integer.toString(daysleft) + " days");

            } else {
                day_tag_helper_text.setVisibility(View.GONE);
                day_tag.setVisibility(View.GONE);
            }
        }

        if (modelList.get(position).getDeadline() != null) {
            if (!modelList.get(position).getDeadline().isEmpty()) {
                Date deadline = stringToDate(modelList.get(position).getDeadline());
                Date new_o_date = subtractMonths(deadline, 3);
                System.out.println("inside else if");
                if (Math.abs(getDaysDifference(c_date, new_o_date)) <= 14 && Math.abs(getDaysDifference(c_date, new_o_date)) > 0) {
                    Activity homeActivity = (HomeActivity) context;
                    // Create an explicit intent for an Activity in your app
                    Intent intent = new Intent(homeActivity, InterestsFragment.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PendingIntent pendingIntent = PendingIntent.getActivity(homeActivity, 0, intent, 0);
                    PendingIntent urlPendingIntent = null;
                    if (modelList.get(position).getUrlLink() != null) {
                        if (!modelList.get(position).getUrlLink().isEmpty()) {
                            String url = modelList.get(position).getVideo_link();
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                       /* try {
                           // context.startActivity(i);
                        } catch (ActivityNotFoundException e) {
                            e.printStackTrace();
                            Toast.makeText(context,"URL is currently not working", Toast.LENGTH_LONG).show();
                        } */
                            urlPendingIntent =
                                    PendingIntent.getBroadcast(homeActivity, 0, i, 0);
                        }
                    }


                    String textTitle = "Application opens in " + Math.abs(getDaysDifference(c_date, new_o_date)) + " days.";
                    String textContent = modelList.get(position).getInstname() + " 's application will open in " + Math.abs(getDaysDifference(c_date, new_o_date));
                    NotificationCompat.Builder builder;
                    if (urlPendingIntent != null) {
                        builder = new NotificationCompat.Builder(context, "notification_channel_id")
                                .setSmallIcon(R.drawable.ic_launcher)
                                .setContentTitle(textTitle)
                                .setContentText(textContent)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setContentIntent(pendingIntent)
                                .addAction(R.drawable.ic_launcher, "View Details", urlPendingIntent)
                                .setAutoCancel(true);
                        System.out.println("test second if");
                    } else {
                        builder = new NotificationCompat.Builder(context, "notification_channel_id")
                                .setSmallIcon(R.drawable.ic_launcher)
                                .setContentTitle(textTitle)
                                .setContentText(textContent)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setContentIntent(pendingIntent)
                                .setAutoCancel(true);
                        System.out.println("test1 second if");
                    }
                    System.out.println("test second if");

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(homeActivity);
                    int  notificationId = position;
                    // notificationId is a unique int for each notification that you must define
                    notificationManager.notify(position, builder.build());

                    day_tag_helper_text.setVisibility(View.VISIBLE);
                    day_tag.setVisibility(View.VISIBLE);
                    int daysleft = Math.abs(getDaysDifference(c_date, new_o_date));
                    day_tag.setText(Integer.toString(daysleft) + " days");

                } else {
                    day_tag_helper_text.setVisibility(View.GONE);
                    day_tag.setVisibility(View.GONE);
                }
            }
        }
        /*
        if (!modelList.get(position).getOpenDate().isEmpty() && c_date.before(o_date)) {
            if (Math.abs(getDaysDifference(c_date, o_date)) <= 14) {
              //  shownotification;
                int daysleft = Math.abs(getDaysDifference(c_date, o_date));
                day_tag_helper_text.setVisibility(View.VISIBLE);
                day_tag.setVisibility(View.VISIBLE);
                day_tag.setText(Integer.toString(daysleft));

            } else {
                day_tag_helper_text.setVisibility(View.GONE);
                day_tag.setVisibility(View.GONE);
            }
        } else if (!modelList.get(position).getDeadline().isEmpty()){
            Date deadline = stringToDate(modelList.get(position).getDeadline());
            Date new_o_date = subtractMonths(deadline, 3);
            System.out.println("inside else if");
            if (Math.abs(getDaysDifference(c_date, new_o_date)) <= 14) {
                day_tag_helper_text.setVisibility(View.VISIBLE);
                day_tag.setVisibility(View.VISIBLE);
                int daysleft = Math.abs(getDaysDifference(c_date, new_o_date));
                day_tag.setText(Integer.toString(daysleft));

            } else {
                day_tag_helper_text.setVisibility(View.GONE);
                day_tag.setVisibility(View.GONE);
            }
        }*/


        return rowView;

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel for CareerPlug";
            String description = "Channel created for notifications for CareerPlug";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notification_channel_id", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }


    public static int getDaysDifference(Date fromDate, Date toDate) {
        if(fromDate==null||toDate==null)
            return 0;

        return (int)( (toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24));
    }

    private static Date subtractMonths(Date start, int numMonths) {
        // System.out.println("add2months date input = " + start);

        if (start != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(start);
            int month = cal.get(Calendar.MONTH);
            month++;
            System.out.println("printing month " + month + " printing numMonths " + numMonths);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            month -= numMonths;
            System.out.println("printing mont-numMonth " + month);
            //10 + 3 = 13.  12 = december.
            if (month < 1) {
                month = 12 - Math.abs(month); //13 - 12 = 1
                System.out.println("12 - month " + month);
            }

            return stringToDate(month + "/" + String.format("%02d", day));
        } else {
            return null;
        }

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

}
