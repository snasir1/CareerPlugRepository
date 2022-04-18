package com.mycareerplug.iamokah.mycareerplug;

import android.util.Log;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import static com.mycareerplug.iamokah.mycareerplug.CreateProfile.TAG;


public class test {

    private static final String[] other_grade_level_array = {"6th Grade", "7th Grade", "8th Grade",
            "HS Freshman", "HS Sophomore", "HS Junior", "HS Senior",
            "College Freshman", "College Sophomore", "College Junior", "College Senior", "Graduate Student"};

    public static void main (String[] args ) {
        /*String string = "January 2, 2010";
        DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        Date date = format.parse(string);
        System.out.println(date);
        System.out.println(date);
        // your block here*/





        String sDate1="31/12/1998";
      //  Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
    //    System.out.println(sDate1+"\t"+date1);

        System.out.println("test");

        try {
            Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
            System.out.println("Converting str: "+ sDate1+" to date \t "+date1);
            sDate1 = "10/31";
            Date date2=new SimpleDateFormat("MM/dd").parse(sDate1);
            System.out.println("Converting str: "+ sDate1+" to date \t "+date2);

            sDate1 = "1-Dec";
            Date date3=new SimpleDateFormat("dd-MMM").parse(sDate1);
            System.out.println("Converting str: "+ sDate1+" to date \t "+date3);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date3);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            System.out.println("year: " + year +" month "+month + " day: " + day);

            //case 1
            String current_date, open_app_date, deadline;
            current_date = "12/1";
            open_app_date = "1/5";
            deadline = "1-April";

            boolean test4 = false == true;
            System.out.println("Chekck " + test4);

            Date c_date = stringToDate(current_date);
            Date open_date = stringToDate(open_app_date);
            Date deadline_date = stringToDate(deadline);

      //      System.out.println("compare open case" + compareDate(open_date, addMonths(open_date, 3), c_date));
            System.out.println("checkEmptyRecords = " + checkEmptyRecords(open_app_date, deadline, c_date));

            System.out.println( "main: c_date" + c_date);
            System.out.println( "comparedate2 " + compareDate2(c_date, addMonths(c_date, 2), open_date));
        //    System.out.println("Days " + getDaysDifference(open_date, c_date));
 //           System.out.println( "main: open_Date " + open_date);
  //          System.out.println( "main: deadline_date " + deadline_date);
     //       System.out.println("yearstart " + YEAR_START);
   //         System.out.println("yearend " + YEAR_END);

      //      System.out.println("compare open > deadline " + open_date.after(deadline_date));
     //       System.out.println("compare " + compareDate(open_date, deadline_date, c_date));

           // System.out.println("add2m0nths to c_date " + addTwoMonths(c_date));

           // System.out.println("addMonths (2 months) to c_date " + addMonths(c_date, 2));
         //   System.out.println("subtractMonths (3 months) to c_date " + subtractMonths(c_date, 3));
           // System.out.println("addMonths (3 months) to c_date " + addMonths(c_date, 3));

        //    System.out.println("case 2 upcoming " + compareDate(c_date, addTwoMonths(c_date), open_date));

       //     System.out.println("case 2 upcoming comparedate2 " + compareDate2(c_date, addTwoMonths(c_date), open_date));

            DateFormat dateFormat = new SimpleDateFormat("MM/dd");
            Date date = new Date();
            Date cur_date = stringToDate(dateFormat.format(date));
           // System.out.println("Current date " + stringToDate(dateFormat.format(date)));

            String glevel = "College Senior";
            String[] test = getPlanning(glevel);
      //      System.out.println("gradelevel " + test[0]);


        } catch (ParseException e) {              // Insert this block.
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static int getDaysDifference(Date fromDate,Date toDate)
    {
        if(fromDate==null||toDate==null)
            return 0;

        return (int)( (toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24));
    }

    private static boolean checkEmptyRecords(String open_date, String deadline_date, Date c_date) {

        Date o_date = stringToDate(open_date);
        Date d_date = stringToDate(deadline_date);

        if (open_date != null && deadline_date != null) {
            if (open_date.isEmpty() && !deadline_date.isEmpty()) {
                if (compareDate(subtractMonths(d_date, 3), d_date, c_date)) {
                    // System.out.println("deadline empty " + i);
                    return true;
                }

            } else if (!open_date.isEmpty() && deadline_date.isEmpty()) {
                if (compareDate(o_date, addMonths(o_date, 3), c_date)) {
                    // System.out.println("deadline empty " + i);
                    return true;
                }
            } else if (!open_date.isEmpty() && !deadline_date.isEmpty()) {
                if (compareDate(o_date, d_date, c_date)) {
                    return true;
                }
            }  else if (open_date.isEmpty() && deadline_date.isEmpty()) {
                //   System.out.println("both empty " + i);
                return true;
            }
        }

        return false;
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

    private static boolean compareDate(Date date_start, Date date_end, Date current_date) {
        Date YEAR_START = stringToDate("1-Jan"),
                YEAR_END = stringToDate("31-Dec");

        System.out.println("start " + date_start);
        System.out.println("end " + date_end);
        System.out.println("current " + current_date);

        if (date_start != null && date_end != null) {
            if (date_start.before(date_end) && !date_start.after(current_date) && !date_end.before(current_date)) {
                return true;
            } else if (date_start.after(date_end)) {
                if (!date_start.after(current_date) && !YEAR_END.before(current_date)) {
                    return true;
                } else if (!YEAR_START.after(current_date) && !date_end.before(current_date)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static Date addTwoMonths(Date start) {
       // System.out.println("add2months date input = " + start);
        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        int month = cal.get(Calendar.MONTH);
        month++;
        System.out.println("printing month " + month);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        if (month == 11) {
            month = 1;
        } else if (month == 12) {
            month = 2;
        } else {
            month += 2;
        }

        return stringToDate(month + "/" + String.format("%02d", day));
    }

    private static Date addMonths(Date start, int numMonths) {
        // System.out.println("add2months date input = " + start);
        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        int month = cal.get(Calendar.MONTH);
        month++;
    //    System.out.println("printing month " + month + " printing numMonths " + numMonths);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        month += numMonths;
    //    System.out.println("printing mont+numMonth " + month);
        //10 + 3 = 13.  12 = december.
        if (month > 12) {
            month = month - 12; //13 - 12 = 1
     //       System.out.println("month - 12 " + month);
        }

        return stringToDate(month + "/" + String.format("%02d", day));
    }

    private static Date subtractMonths(Date start, int numMonths) {
        // System.out.println("add2months date input = " + start);
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
    }




    private static boolean compareDate2(Date c_date, Date c_date_plus_twomonths, Date open_date) {
        Date YEAR_START = stringToDate("1-Jan"),
                YEAR_END = stringToDate("31-Dec");

        if (c_date != null && c_date_plus_twomonths != null) {
            System.out.println("c_date(before) " + c_date.before(c_date_plus_twomonths));
            System.out.println("c_date (after) open " + c_date.after(open_date));
            System.out.println("c_date_plus_twomonths " + !c_date_plus_twomonths.before(open_date));
            if ((open_date.compareTo(c_date_plus_twomonths) <= 0 && open_date.compareTo(c_date) > 0) && !c_date_plus_twomonths.before(open_date)) {
                return true;
            } else if (c_date.after(c_date_plus_twomonths)) {
                System.out.println("compare2 else if");
                //Here c_date > open_date. Different from above case.
                if (open_date.after(c_date) && !YEAR_END.before(open_date)) {
                    return true;
                } else if (!YEAR_START.after(open_date) && !c_date_plus_twomonths.before(open_date)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static String[] getPlanning(String gradelevel) {
        String[] answer = new String[2];
        for (int i = 0; i < other_grade_level_array.length; i++) {
            if (gradelevel.contains(other_grade_level_array[i])) {
                if (i == 10) {
                    answer[0] = other_grade_level_array[i+1];
                    answer[1] = "College Graduate";
                    return answer;
                } else if (i == 11) {
                    return null;
                } else {
                    answer[0] = other_grade_level_array[i+1];
                    answer[1] = other_grade_level_array[i+2];
                    return answer;
                }
            }
        }

        return null;
    }


}
