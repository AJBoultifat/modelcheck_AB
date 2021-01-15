package factoryManager;

import java.io.FileReader;
import java.io.Reader;
import java.util.Set;
import java.util.SortedSet;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Access {

    private static Logger log = Logger.getLogger(Access.class.getName());
    
    private Model model;
    
    public void loadModel(String filename) {
        log.log(Level.FINE, "Loading model: " + filename);
        try {
            FileReader reader = new FileReader(filename);
            if (filename.toLowerCase().endsWith(".eqn")) {
                loadEquationsModel(reader);
            } else {
                loadTableModel(reader);
            }
        } catch (Exception e) {
            throw new ProgramErrorException(e);
        }
        log.log(Level.FINE, "Loaded model: " + filename);
    }

    public void loadTableModel(Reader reader) {
        log.log(Level.FINE, "Loading table model in reader: " + reader);
        //loadEquationsModel(reader);
        log.log(Level.FINE, "Loaded table model in reader: " + reader);
        log.log(Level.FINE, "Initializing BDD engine ...");
        model.initBDDEngine();
        log.log(Level.FINE, "Initializing BDD engine ...");
    }

    public void loadEquationsModel(Reader reader) {

        log.log(Level.FINE, "Loading equations model in reader: " + reader);
        log.log(Level.FINE, "Initializing BDD engine ...");
        model.initBDDEngine();
        log.log(Level.FINE, "BDD engine initialized");
    }

   
    public String getEFPFormula(boolean isConjunctive) {
        return model.getEFPFormula(isConjunctive);
    }
    
    public SortedSet<String> getModelVariables() {
        return model.getVariables();
    }
    
    public Set<Set<String>> getLabelsSet(String formula) {
        StateLabeling sl = new RecursiveStateLabeling(model);
        Set<Set<String>> labels = sl.computeLabels(formula);
        return labels;
    }
    
    public ModelCheckingResults getLabelsArray(String formula) {
        log.log(Level.FINE, "computing SAT(" + formula + ") ...");
        StateLabeling sl = new RecursiveStateLabeling(model);
        ModelCheckingResults results = sl.computeLabels2(formula);
        log.log(Level.FINE, "Finished computing SAT(" + formula + ")");
        return results;
    }
    
    public boolean checkCTLFormulaSyntax(String formula) {
        return true;
    }
}
