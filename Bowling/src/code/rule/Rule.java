package code.rule;

import code.Roll;
import code.Frame;

import java.util.List;

public interface Rule {
    int getMaxIter();

    Frame determineFrame(int struck, List<Roll> results);

    int grantChance(Frame frame);
}
