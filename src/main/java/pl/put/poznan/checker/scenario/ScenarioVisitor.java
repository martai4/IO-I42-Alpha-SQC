package pl.put.poznan.checker.scenario;

public class ScenarioVisitor implements Visitor{

    private Scenario toConvert;
    private int maxLevel;
    private String newName;

    public ScenarioVisitor(Scenario toConvert, int lvl, String name){
        this.toConvert=toConvert;
        this.maxLevel = lvl;
        this.newName = name;
    }

    @Override
    public Visitor visit(Scenario toConvert) {
        SubScenario mainSubscenario = new SubScenario(1);
        Scenario converted = new Scenario(newName, toConvert.getActors(), toConvert.getSystemActor(),mainSubscenario);

        SubScenarioVisitor mainVisitor = new SubScenarioVisitor();




        return this;
    }

    @Override
    public Visitor visit(SubScenario subScenario) {
        return null;
    }

    @Override
    public Visitor visit(Step step) {
        return null;
    }
}
