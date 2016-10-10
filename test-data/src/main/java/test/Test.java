package test;

public class Test {

    private String aField = "I'm a field";
    private final String aFinalField = "I'm a final field. Don't touch me.";

    public Test() {}
    public Test(int parameter) {}

    public int testMethod(int argument) {
        return argument + 5;
    }

}
