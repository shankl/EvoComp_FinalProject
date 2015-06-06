package evolver;

import java.util.ArrayList;
import java.util.List;

public class Tree<T> {
    private Node<T> root;

    public Tree(T rootData) {
        root = new Node<T>();
        root.data = rootData;
        root.children = new ArrayList<Node<T>>();
    }

    public static class Node<T> {
        private T data;
        private Node<T> parent;
        private List<Node<T>> children;
    }
    
    public List<Node<T>> getChildren(Node<T> parent) {
    	return parent.children;
    }
    public void addChild(Node<T> parent, Node<T> child) {
    	parent.children.add(child);
    }

}
