package com.jtissdev.api.model;

import org.json.simple.JSONObject;

public class Consumable {
    private final int id;
    private final String unite;
    private final String designation;
    private final double pu;


    private final int quantite;

    public Consumable(int id, String designation, String unite, int quantite, double pu) {
        this.id = id;
        this.designation = designation;
        this.unite = unite;
        this.pu = pu;
        this.quantite = quantite;
        
    }

    public Object toJSON() {
        JSONObject jsonconsumable = new JSONObject();
        jsonconsumable.put("id",this.getId());
        jsonconsumable.put("designation",getDesignation());
        jsonconsumable.put("unite",getUnite());
        jsonconsumable.put("pu",getPu());
        jsonconsumable.put("quantite",getQuantite());
        jsonconsumable.put("prix",getPrix());
        return jsonconsumable;
    }

    public int getId() {
        return this.id;
    }

    public String getDesignation() {
        return this.designation;
    }
    public String getUnite() {
        return this.unite;
    }

    public double getPu() {
        return this.pu;
    }

    public int getQuantite() {
        return this.quantite;
    }

    public double getPrix(){
        return this.getQuantite()*this.getPu();
    }
}
