package be.lifeisbananas;

public class GroentenRecipe extends Recipe {

    // properties
    private ReceptType receptType;
    private String Groentesoort;
    private Double Vezelgehalte;
    private boolean RijkAanEiwitten;
    private Integer Kruidigheid;
    private boolean GeschiktAlsTopping;


    // constructor


    public GroentenRecipe(Integer recipeID, String name, String version) {
        super(recipeID,name, version);
        receptType = ReceptType.GROENTEN;
    }


    //setters
    public void setGroentesoort(String groentensoort) {
        this.Groentesoort = groentensoort;
    }
    public void setVezelgehalte(Double vezelhehalte) {
        this.Vezelgehalte = vezelhehalte;
    }
    public void setRijkAanEiwitten(boolean rijkAanEiwitten) {
        this.RijkAanEiwitten = rijkAanEiwitten;
    }
    public void setKruidigheid(Integer kruidigheid) {
        this.Kruidigheid = kruidigheid;
    }
    public void setGeschiktAlsTopping(boolean geschiktAlsTopping) {
        this.GeschiktAlsTopping = geschiktAlsTopping;
    }



    // getters
    public ReceptType getReceptType() { return receptType; }
    public String getGroentesoort() { return Groentesoort; }
    public Double getVezelgehalte() { return Vezelgehalte; }
    public boolean getRijkAanEiwitten() { return RijkAanEiwitten; }
    public Integer getKruidigheid() { return Kruidigheid; }
    public boolean getGeschiktAlsTopping() {return GeschiktAlsTopping; }
}