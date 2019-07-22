package utils;


//This class is only for UI work. Not to be kept.
public class DisplayRule implements Rule {

    String name;

    public DisplayRule(String name) {
        this.name = name;
    }

    @Override
    public void disable() {

    }

    @Override
    public void enable() {

    }

    @Override
    public String getName() {
        return name;
    }
}
