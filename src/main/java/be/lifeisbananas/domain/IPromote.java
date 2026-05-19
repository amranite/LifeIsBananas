package be.lifeisbananas.domain;

public interface IPromote {
    public String nextStatus(Product product);
    public String reject (Product product);
}
