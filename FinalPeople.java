import javafx.beans.property.SimpleStringProperty;

public class FinalPeople
{
    private SimpleStringProperty f1;
    private SimpleStringProperty f2;
    private double f3;

    FinalPeople(String f1, String f2, double f3)
    {
        this.f1 = new SimpleStringProperty(f1);
        this.f2 = new SimpleStringProperty(f2);
        this.f3 = f3;
    }
    public String getF1()
    {
        return f1.get();
    }
    public String getF2()
    {
        return f2.get();
    }
    public double getF3()
    {
        return f3;
    }
}