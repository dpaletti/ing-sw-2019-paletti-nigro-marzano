package it.polimi.se2019.model;

import java.util.Set;

public class TreeNode<T> {
    private Set<TreeNode<T>> sons;

    public void addChild(TreeNode<T> child){};
    public Set<TreeNode<T>> getSons() {
        return sons;
    };


}
