

package factoryManager;

import java.util.List;
import java.util.Set;

public interface StateLabeling {

    public Set<Set<String>> computeLabels(String formula);
    public ModelCheckingResults computeLabels2(String formula);
}
