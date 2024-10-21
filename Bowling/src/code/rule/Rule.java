package code.rule;

import code.model.Frame;
import code.model.Roll;

import java.util.List;

public interface Rule {
    int MAX = 10;
    int NONE = 0;

    int getMaxIter();
    Frame determineFrame(int struck, List<Roll> results);
    int grantChance(Frame frame, int iter);
}
