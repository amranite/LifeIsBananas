package be.lifeisbananas;
import be.lifeisbananas.ReceptType;
import be.lifeisbananas.Recipe;

public class FruitRecipe extends Recipe {

    // properties
    private ReceptType receptType;
    private String FruitSoort;
    private Double SuikerGehalte;
    private boolean BevatVitamineC;
    private Integer ZuurGehalte;
    private SnackMoment SnackMoment;

    // constructor
    public FruitRecipe(Integer recipeID, String name, String version) {
        super(recipeID, name, version);
        this.receptType = ReceptType.FRUIT;
    }


    //setters
    public void setFruitSoort(String FruitSoort) {
        this.FruitSoort = FruitSoort;
    }
    public void setSuikerGehalte(Double SuikerGehalte) {
        this.SuikerGehalte = SuikerGehalte;
    }
    public void setBevatVitamineC(boolean BevatVitamineC) {
        this.BevatVitamineC = BevatVitamineC;
    }
    public void setZuurGehalte(Integer ZuurGehalte) {
        this.ZuurGehalte = ZuurGehalte;
    }
    public void setSnackMoment(SnackMoment SnackMoment) {
        this.SnackMoment = SnackMoment;
    }



    // getters
    public ReceptType getReceptType() { return receptType; }
    public String getFruitSoort() { return FruitSoort; }
    public Double getSuikerGehalte() { return SuikerGehalte; }
    public boolean getBevatVitamineC() { return BevatVitamineC; }
    public Integer getZuurGehalte() { return ZuurGehalte; }
    public SnackMoment getSnackMoment() { return SnackMoment; }
}