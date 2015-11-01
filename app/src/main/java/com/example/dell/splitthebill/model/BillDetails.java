package com.example.dell.splitthebill.model;

/**
 * Created by dell on 10/26/2015.
 */
public class BillDetails {
    private int id;
    private String date;
    private double bill;
    private int tipPercent;
    private int people;
    private double tipAmount;
    private double billAmount;
    private double amtPerPerson;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getBill() {
        return bill;
    }

    public void setBill(double bill) {
        this.bill = bill;
    }

    public int getTipPercent() {
        return tipPercent;
    }

    public void setTipPercent(int tipPercent) {
        this.tipPercent = tipPercent;
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    public double getTipAmount() {
        return tipAmount;
    }

    public void setTipAmount(double tipAmount) {
        this.tipAmount = tipAmount;
    }

    public double getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(double billAmount) {
        this.billAmount = billAmount;
    }

    public double getAmtPerPerson() {
        return amtPerPerson;
    }

    public void setAmtPerPerson(double amtPerPerson) {
        this.amtPerPerson = amtPerPerson;
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
