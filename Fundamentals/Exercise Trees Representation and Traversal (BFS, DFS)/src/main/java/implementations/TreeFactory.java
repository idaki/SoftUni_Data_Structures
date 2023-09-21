package implementations;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;


public class TreeFactory {
    private Map<Integer, Tree<Integer>> nodesByKeys;


    public TreeFactory() {
        this.nodesByKeys = new LinkedHashMap<>();
    }

    public Tree<Integer> createTreeFromStrings(String[] input) {
        for (String entry : input) {
            int[] keys = Arrays.stream(entry.split("\\s+"))
                    .mapToInt(Integer::parseInt)
                    .toArray();
            int parentKey = keys[0];
            int childKey = keys[1];

            this.addEdge(parentKey, childKey);
        }
        return this.getRoot();
    }

    public Tree<Integer> getRoot(){
        for (Tree<Integer> current: nodesByKeys.values()
             ) {
            if (current.getParent() == null){
                return  current;
            }
            
        }
       
        return null;
    }

    public Tree<Integer> createNodeByKey(int key) {
        this.nodesByKeys.putIfAbsent(key, new Tree<>(key));
        return this.nodesByKeys.get(key);
    }

    public void addEdge(int parent, int child) {
        Tree<Integer> parentByKey = this.createNodeByKey(parent);
        Tree<Integer> childByKey = this.createNodeByKey(child);
        childByKey.setParent(parentByKey);
        parentByKey.addChild(childByKey);

    }
}


