package pl.put.poznan.checker.scenario;

import java.util.List;

public class Scenario {
    private String name;
    private List<String> actors;
    private SubScenario main;

    public Scenario(){}

    public void HowManyDecisions(){
        System.out.println(main.HowManyDecisions());
    }

    public void ShowActorsErrors(){
        List<String> odpowiedz=new ArrayList<>();
        odpowiedz=main.ListNoActorsErrors(actors);
        for (String s : odpowiedz) {
            System.out.println(s);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getActors() {
        return actors;
    }

    public void setActors(List<String> actors) {
        this.actors = actors;
    }

    public SubScenario getMain() {
        return main;
    }

    public void setMain(SubScenario main) {
        this.main = main;
    }
}
