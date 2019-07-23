package utils;

public class RuleAdapterItem {
    String name;
    //more things later

    public RuleAdapterItem(Rule rule) {
        this.name = rule.getName();
    }

    public String getName() {
        return name;
    }
}
