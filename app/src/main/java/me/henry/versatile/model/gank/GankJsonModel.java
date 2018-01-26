package me.henry.versatile.model.gank;

import java.util.List;


   
public class GankJsonModel {

   private boolean error;
   private List<GankData> results;

    public GankJsonModel() {
    }

    public void setError(boolean error) {
        this.error = error;
    }
    public boolean getError() {
        return error;
    }
    

    public void setResults(List<GankData> results) {
        this.results = results;
    }
    public List<GankData> getResults() {
        return results;
    }
    
}

   

