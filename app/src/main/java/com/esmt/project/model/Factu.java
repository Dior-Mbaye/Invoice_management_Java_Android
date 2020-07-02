package com.esmt.project.model;

public class Factu {
    private String userLast, bid, dateLimit,montant,ref,category;

    public Factu(String userLast, String bid, String dateLimit, String montant, String ref,String category) {
        this.userLast = userLast;
        this.bid = bid;
        this.dateLimit = dateLimit;
        this.montant = montant;
        this.ref = ref;
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDateLimit() {
        return dateLimit;
    }

    public void setDateLimit(String dateLimit) {
        this.dateLimit = dateLimit;
    }

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public Factu() {
    }

    public String getUserLast() {
        return userLast;
    }

    public void setUserLast(String userLast) {
        this.userLast = userLast;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }
}
