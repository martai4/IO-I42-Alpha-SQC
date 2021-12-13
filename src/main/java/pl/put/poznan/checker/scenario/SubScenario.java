package pl.put.poznan.checker.scenario;

import java.util.List;

public class SubScenario {
    private Integer level;
    private Integer length;
    private List<Step> steps;

    public boolean Is_w2_start_w1(String w1, String w2){
        for (int i=0;i<w1.length();i++){
            if(w1.charAt(i) != w2.charAt(i)){
                return false;
            }
        }
        return true;
    }

    public int HowManyDecisions(){
        int counter=0;
        String k1="IF";
        String k2="ELSE";
        String k3="FOR EACH";
        for (Step s : steps) {
            if(Is_w2_start_w1(k1, String.valueOf(s)) || Is_w2_start_w1(k2,String.valueOf(s)) || Is_w2_start_w1(k3,String.valueOf(s))){
                counter=counter+1;
            }
        }
        return counter;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> stepsList) {
        this.steps = stepsList;
    }
}
