package mainpack;

import static ctlFormula.Atom.atom;
import static ctlOperator.EF.EF;

import ctlChecker.CTLModelChecker;
import ctlFormula.Formula;
import model.KripkeStructure;
import model.ModelBoolVariable;
import model.State;

public class ExampleMain {
    public static void main(String[] args) {

       
        
        ModelBoolVariable m = new ModelBoolVariable();
        m.CreateVariable("a","b","c","d", "f");
        m.condition("a",true,m.OP(m.AND("b",true, m.OR("c", false, "d", true))));
        
        // condition(string, model)
        // and("a",true,"b",true);
        // and("b", op);
        // and("","","","","");
        
        // a = true if b = flase and c = true; 
        //        if ---> transation
        // {a = false, b = false, c = true } -> {a = true, b = false, c= true}
        
        
        
    }
    
}
















/*
 * 
 * 
 * 
 * 
 * 
        
        */
