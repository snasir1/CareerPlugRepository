package com.mycareerplug.iamokah.mycareerplug;

public class CardModel {

    public String instname;
    public String programname;
    public String deadline;
    public String cost;
    public String financialaid;
    public String pay;
    public String urlLink;
    public String open_date;
    public String type_of_event;
    public String video_link;
    public String get_tense;
    public String category;

    public int array_pos;



    public boolean is_new;

    public CardModel() {

    }

    public CardModel(String instname, String programname, String deadline, String cost,
                     String financialaid, String pay, String urlLink,
                     String open_date, String type_of_event, String video_link, int array_pos,
                     String get_tense, String category, boolean is_new) {
        this.instname = instname;
        this.programname = programname;
        this.deadline = deadline;
        this.cost = cost;
        this.financialaid = financialaid;
        this.pay = pay;
        this.urlLink = urlLink;
        this.open_date = open_date;
        this.type_of_event = type_of_event;
        this.video_link = video_link;
        this.array_pos = array_pos;
        this.get_tense = get_tense;
        this.category = category;
        this.is_new = is_new;
    }
/* THIS EQUALS OVERRIDE CODE DOES NOT WORK!!!!
    @Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof CardModel)) {
            return false;
        }

        CardModel model = (CardModel) o;

        return model.instname.equals(instname) &&
                model.programname == programname &&
                model.deadline.equals(deadline) &&
                model.cost.equals(cost) &&
                model.financialaid.equals(financialaid) &&
                model.pay.equals(pay) &&
                model.urlLink.equals(urlLink) &&
                model.open_date.equals(open_date) &&
                model.type_of_event.equals(type_of_event) &&
                model.video_link.equals(video_link) &&
                model.array_pos == (array_pos);
    } */

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!CardModel.class.isAssignableFrom(obj.getClass())) {
            return false;
        }

        final CardModel other = (CardModel) obj;
        if ((this.instname == null) ? (other.instname != null) : !this.instname.equals(other.instname)) {
            return false;
        }

        if ((this.programname == null) ? (other.programname != null) : !this.programname.equals(other.programname)) {
            return false;
        }

        if ((this.deadline == null) ? (other.deadline != null) : !this.deadline.equals(other.deadline)) {
            return false;
        }

        if ((this.cost == null) ? (other.cost != null) : !this.cost.equals(other.cost)) {
            return false;
        }

        if ((this.financialaid == null) ? (other.financialaid != null) : !this.financialaid.equals(other.financialaid)) {
            return false;
        }

        if ((this.pay == null) ? (other.pay != null) : !this.pay.equals(other.pay)) {
            return false;
        }

        if ((this.urlLink == null) ? (other.urlLink != null) : !this.urlLink.equals(other.urlLink)) {
            return false;
        }

        if ((this.open_date == null) ? (other.open_date != null) : !this.open_date.equals(other.open_date)) {
            return false;
        }

        if ((this.type_of_event == null) ? (other.type_of_event != null) : !this.type_of_event.equals(other.type_of_event)) {
            return false;
        }

        if ((this.video_link == null) ? (other.video_link != null) : !this.video_link.equals(other.video_link)) {
            return false;
        }

        if (this.array_pos != other.array_pos) {
            return false;
        }

        if ((this.get_tense == null) ? (other.get_tense != null) : !this.get_tense.equals(other.get_tense)) {
            return false;
        }

        if ((this.category == null) ? (other.category != null) : !this.category.equals(other.category)) {
            return false;
        }
        /*
        if (this.is_new != other.is_new) {
            return false;
        }*/

        return true;
    }

    public boolean get_is_new() {
        return is_new;
    }

    public void set_is_new(boolean is_new) {
        this.is_new = is_new;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String get_tense() {
        return get_tense;
    }

    public void setGet_tense(String get_tense) {
        this.get_tense = get_tense;
    }

    public int getArray_pos() {
        return array_pos;
    }

    public void setArray_pos(int array_pos) {
        this.array_pos = array_pos;
    }

    public String getInstname() {
        return instname;
    }

    public void setInstname(String instname) {
        this.instname = instname;
    }

    public String getProgramname() {
        return programname;
    }

    public void setProgramname(String programname) {
        this.programname = programname;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getFinancialaid() {
        return financialaid;
    }

    public void setFinancialaid(String financialaid) {
        this.financialaid = financialaid;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getUrlLink() {
        return urlLink;
    }

    public void setUrlLink(String urlLink) {
        this.urlLink = urlLink;
    }

    public String getOpenDate() {
        return open_date;
    }

    public void setOpen_date(String open_date) {
        this.open_date = open_date;
    }

    public String getType_of_event() {
        return type_of_event;
    }

    public void setType_of_event(String type_of_event) {
        this.type_of_event = type_of_event;
    }

    public String getVideo_link() {
        return video_link;
    }

    public void setVideo_link(String video_link) {
        this.video_link = video_link;
    }


}
