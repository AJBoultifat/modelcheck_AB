package factoryManager;

import java.io.StringReader;
import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import factoryBooleanModel.Nominal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDPairing;
import factoryCtl.*;

public class RecursiveStateLabeling implements StateLabeling {
    
    private static Logger log = Logger.getLogger(RecursiveStateLabeling.class.getName());
    
    private Model model;
    private BDD transitions;
    private BDD primedVars;
    private BDD normalVars;
    private BDDPairing toPrime;
    private BDDPairing fromPrime;
    
    public RecursiveStateLabeling(Model m) {
        model = m;
        transitions = null; //model.getTransitionsBDD();
        primedVars = model.getPrimedVarSet();
        normalVars = model.getVarSet();
        toPrime = model.getToPrimePairing();
        fromPrime = model.getFromPrimePairing();
    }

    public Set<Set<String>> computeLabels(String formula) {
        Map<String,Nominal> env = new HashMap<String,Nominal>();
        BDD bdd = null;
		Set<Set<String>> labels = model.labelsBDDToStringSet(bdd);
        bdd.free();
        return labels;
    }
    
    public ModelCheckingResults computeLabels2(String formula) {
        long time0 = System.currentTimeMillis();
        ProgramErrorException f = parseFormula(formula);
        Map<String,Nominal> env = new HashMap<String,Nominal>();

        //BDD bdd = computeLabelsBDD(env, f, 0, Integer.MAX_VALUE);

        long totalTime = System.currentTimeMillis() - time0;
        long networkBDDCreationTime = model.getTransitionsBDDCreationTime();
        long verificationTime = totalTime - networkBDDCreationTime;
        ModelCheckingResults results = new ModelCheckingResults();
        results.setFormulaVerificationTime(verificationTime);
        results.setModelBDDCreationTime(networkBDDCreationTime);
        return results;
    }    
    
    private ProgramErrorException parseFormula(String formula) {
    	Throwable e = null;
		return new ProgramErrorException("Error at parsing formula.\n" + e.getMessage(), e);

    }
    
    private BDD computeLabelsBDD(Map<String,Nominal> env, StateFormula f, int level, int logUptoLevel) {
        if (level <= logUptoLevel) System.out.println(new java.util.Date()+": verifying formula \""+f+"\"");
        BDD finalResult = null;
        if (f.type() == StateFormula.TYPE_BOOLEAN_CONST) { // --> case: true, false
            boolean value = ((BooleanConstant) f).getValue();
            finalResult = model.getBooleanConstantBDD(value);
        } else if (f.type() == StateFormula.TYPE_VARIABLE) { // --> case: Variable
            String varname = ((Variable) f).getName();
            //BigInteger n = env.get(varname);
            Nominal n = env.get(varname);
            if (n == null)  finalResult = model.getVariableBDD(varname);
            else            finalResult = computeLabelsBDD(env, n, level+1, logUptoLevel);
        } else if (f.type() == StateFormula.TYPE_AND) { // --> case: &
            StateFormula sub1 = ((And) f).getLeftSubformula();
            StateFormula sub2 = ((And) f).getRightSubformula();
            BDD subBDD1 = computeLabelsBDD(env, sub1, level+1, logUptoLevel);
            BDD subBDD2 = computeLabelsBDD(env, sub2, level+1, logUptoLevel);
            finalResult = subBDD1.andWith(subBDD2);
        } else if (f.type() == StateFormula.TYPE_OR) { // --> case: |
            StateFormula sub1 = ((Or) f).getLeftSubformula();
            StateFormula sub2 = ((Or) f).getRightSubformula();
            BDD subBDD1 = computeLabelsBDD(env, sub1, level+1, logUptoLevel);
            BDD subBDD2 = computeLabelsBDD(env, sub2, level+1, logUptoLevel);
            finalResult = subBDD1.orWith(subBDD2);
        } else if (f.type() == StateFormula.TYPE_EX) { // --> case: EX
            StateFormula sub = ((EX) f).getSubformula();
            BDD subBDD = computeLabelsBDD(env, sub, level+1, logUptoLevel);
            BDD result = preE(subBDD);
            subBDD.free();
            finalResult = result;
        } else if (f.type() == StateFormula.TYPE_EU) { // --> case: EU
            StateFormula sub1 = ((EU) f).getLeftSubformula();
            StateFormula sub2 = ((EU) f).getRightSubformula();
            BDD bddsub1 = computeLabelsBDD(env, sub1, level+1, logUptoLevel);
            BDD bdd0 = computeLabelsBDD(env, sub2, level+1, logUptoLevel);
            BDD bdd1 = bdd0.id().orWith( preE(bdd0).andWith(bddsub1.id()) );
            while ( !bdd0.equals(bdd1) ) {
                bdd0.free();
                bdd0 = bdd1.id();
                bdd1 = bdd1.orWith( preE(bdd0).andWith(bddsub1.id()) );
            }
            bddsub1.free();
            bdd1.free();
            finalResult = bdd0;
        } else if (f.type() == StateFormula.TYPE_EF) { // --> case: EF
            StateFormula sub = ((EF) f).getSubformula();
            finalResult = computeLabelsBDD(env, new EU(new BooleanConstant(true), sub), level+1, logUptoLevel);
        } else if (f.type() == StateFormula.TYPE_EG) { // --> case: EG
            StateFormula sub = ((EG) f).getSubformula();
            BDD bdd0 = computeLabelsBDD(env, sub, level+1, logUptoLevel);
            BDD bdd1 = bdd0.id().andWith(preE(bdd0));
            while ( !bdd0.equals(bdd1) ) {
                bdd0.free();
                bdd0 = bdd1.id();
                bdd1 = bdd1.andWith(preE(bdd0));
            }
            bdd1.free();
            finalResult = bdd0;
} else if (f.type() == StateFormula.TYPE_NOMINAL) { // --> case: Nominal
            Nominal n = (Nominal) f;
            int numVars = model.getVarNum();
            BDD bdd = model.getBooleanConstantBDD(true);
            for (int ii=0; ii<numVars; ii++) {
                BDD tmp = null;
                
                if (n.testBit(ii)) tmp = model.getVariableBDD(ii);
                else               tmp = model.getVariableNegBDD(ii);
                
                bdd.andWith(tmp);
            }
            finalResult = bdd;
        } else if (f.type() == StateFormula.TYPE_EXISTS_STATE) { // --> case: ]s.
            String var = ((ExistsState) f).getVariable();
            StateFormula sub = ((ExistsState) f).getSubformula();
            int varNum = model.getVarNum();BigInteger maxNom = BigInteger.ONE.shiftLeft(varNum).subtract(BigInteger.ONE);
            NominalCounter counter = new NominalCounter(maxNom, f.toString());
            log.finest("maxNom: "+maxNom);
            BDD accum = model.getBooleanConstantBDD(false);
            Map<String,Nominal> env2 = new HashMap<String,Nominal>(env);
            env2.put(var, new Nominal(counter)); // counter changes its state at every iteration
            while (counter.leqMaxValue()) {
                //env2.put(var, new Nominal(counter)); // counter changes its state at every iteration
                accum.orWith( computeLabelsBDD(env2, sub, level+1, logUptoLevel) );
                counter.increment();
            }
            finalResult = accum;
        } else if (f.type() == StateFormula.TYPE_STATE_BINDER) { // --> case: !s.
            int optimization = getOptimizationNum(f);
            if (optimization == 1 || optimization == 2) {
                finalResult = countSuccessors(optimization, level, logUptoLevel);
            }
            String var = ((StateBinder) f).getVariable();
            StateFormula sub = ((StateBinder) f).getSubformula();
            int varNum = model.getVarNum();
            //BigInteger counter = new BigInteger("0");
            //BigInteger stateNum = (new BigInteger("1")).shiftLeft(varNum);
            //BigInteger one = new BigInteger("1");
            //BigInteger tenth = stateNum.divide(new BigInteger("10"));
            //BigInteger nominalsDone = new BigInteger(tenth.toString());
            //int percent = 0;
            //log.finest(percent + "% for " + f);
            
            BDD accum = model.getBooleanConstantBDD(false);
            Map<String,Nominal> env2 = new HashMap<String,Nominal>(env);
            
            BigInteger maxNom = BigInteger.ONE.shiftLeft(varNum).subtract(BigInteger.ONE);
            log.finest("maxNom: "+maxNom);
            NominalCounter counter = new NominalCounter(maxNom, f.toString());
                Nominal n = new Nominal(counter);
                env2.put(var, n);
            while (counter.leqMaxValue()) {
                //Nominal n = new Nominal(counter);
                //env2.put(var, n);
                BDD nom = computeLabelsBDD(env2, n, level+1, logUptoLevel);
                BDD tmp = computeLabelsBDD(env2, sub, level+1, logUptoLevel).andWith(nom.id());
                if (!tmp.isZero()) accum.orWith(nom);
                else nom.free();
                tmp.free();
                counter.increment();
            }
            finalResult = accum;
        } else if (f.type() == StateFormula.TYPE_AT_STATE) { // --> case: @s
            QuantifierSubject subject = ((AtState) f).getSubject();
            StateFormula sub = ((AtState) f).getSubformula();
            Nominal nom = null;
            if (subject instanceof Variable) {
                String varname = ((Variable) subject).getName();
                nom = env.get(varname);
                if (nom == null) {
                    throw new ProgramErrorException("Variable \""
                        + varname
                        + "\" is uninstantiated");
                }
            } else {
                nom = (Nominal) subject;
            }
            BDD tmp = computeLabelsBDD(env, nom, level+1, logUptoLevel);
            tmp = tmp.andWith(computeLabelsBDD(env, sub, level+1, logUptoLevel));
            if (tmp.isZero()) {
                finalResult = tmp;
            } else {
                tmp.free();
                finalResult = model.getBooleanConstantBDD(true);
            }
            //return null;
        } else {
            throw new ProgramErrorException("The algorithm for verifying formula "
                + f
                + " is not implemented yet");
        }
        if (level <= logUptoLevel) System.out.println(new java.util.Date()+": finishing formula \""+f+"\"");
        return finalResult;
    }
    
    // returns the pre-image of set x,
    // x is not consumed
    private BDD preE(BDD x) {
        if ( transitions == null ) transitions = model.getTransitionsBDD();
        BDD x2 = x.replace(toPrime);
        BDD x3 = x2.and(transitions);
        BDD result = x3.exist(primedVars);
        x2.free();
        x3.free();
        return result;
    }
    
    // returns the image of set x,
    // x is not consumed
    private BDD postE(BDD x) {
        if ( transitions == null ) transitions = model.getTransitionsBDD();
        BDD x2 = x.and(transitions);
        BDD x3 = x2.exist(normalVars);
        BDD result = x3.replaceWith(fromPrime);
        x2.free();
        return result;
    }
    
    private int getOptimizationNum(StateFormula f) {
        int type = f.type();
        switch (type) {
            case StateFormula.TYPE_STATE_BINDER:
                StateBinder sb = (StateBinder) f;
                String var1 = sb.getVariable();
                if (sb.getSubformula().type() == StateFormula.TYPE_EX) {
                    EX ex = (EX) sb.getSubformula();
                    if (ex.getSubformula().type() == StateFormula.TYPE_STATE_BINDER) {
                        StateBinder sb2 = (StateBinder) ex.getSubformula();
                        String var2 = sb2.getVariable();
                        if (sb2.getSubformula().type() == StateFormula.TYPE_AT_STATE) {
                            AtState at = (AtState) sb2.getSubformula();
                            if (at.getSubject().toString().equals(var1)) {
                                if (at.getSubformula().type() == StateFormula.TYPE_EX ) {
                                    EX ex2 = (EX) at.getSubformula();
                                    
                                }
                            }
                        }
                    }
                }
                break;
            default:
                break;
        }
        return 0;
    }
    
    private BDD countSuccessors(int opt, int level, int logUptoLevel) {
        // opt == 1: states with more that one successor
        // opt == 2: states with exactly one successor
        int varNum = model.getVarNum();
        BigInteger maxNom = BigInteger.ONE.shiftLeft(varNum).subtract(BigInteger.ONE);
        log.finest("maxNom: "+maxNom);
        NominalCounter counter = new NominalCounter(maxNom, (opt==1?"successors>1":"successors=1"));
        BDD accum = model.getBooleanConstantBDD(false);
        Nominal nom = new Nominal(counter);
        while (counter.leqMaxValue()) {
            BDD nombdd = computeLabelsBDD(null, nom, level+1, logUptoLevel);
            BDD post = postE(nombdd);
            BDD tmp = post.exist(primedVars);
            double satCount = tmp.satCount(normalVars);
            if (satCount>1.0 && opt == 1) {
                accum.orWith(nombdd);
            } else if (satCount==1.0 && opt == 2) {
                accum.orWith(nombdd);
            } else {
                nombdd.free();
            }
            post.free();
            tmp.free();
            counter.increment();
        }
        return accum;
    }

}
