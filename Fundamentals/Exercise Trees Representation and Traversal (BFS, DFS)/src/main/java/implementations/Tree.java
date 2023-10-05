package implementations;

import interfaces.AbstractTree;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Tree<E> implements AbstractTree<E> {
    private E key;
    private Tree<E> parent;
    private List<Tree<E>> children;

    public Tree(E key) {
        this.key = key;
        this.children = new ArrayList<>();
    }

    @Override
    public void setParent(Tree<E> parent) {
        this.parent = parent;
    }

    @Override
    public void addChild(Tree<E> child) {
        children.add(child);
    }

    @Override
    public Tree<E> getParent() {
        return this.parent;
    }

    @Override
    public E getKey() {
        return this.key;
    }

    @Override
    public String getAsString() {
        StringBuilder sb = new StringBuilder();
        traverseWithRecurrence(sb, 0, this);

        return sb.toString().trim();
    }

    private void traverseWithRecurrence(StringBuilder sb, int indent, Tree<E> tree) {
        sb.
                append(addPadding(indent)).
                append(tree.key).
                append(System.lineSeparator());
        for (Tree<E> child : tree.children) {
            traverseWithRecurrence(sb, indent + 2, child);
        }
    }

    private String addPadding(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }

    @Override
    public List<E> getLeafKeys() {
        List<E> result = new ArrayList<>();
        this.FindsLeafDFS(this, result);
        return result;
    }

    private void FindsLeafDFS(Tree<E> tree, List<E> result) {
        for (Tree<E> child : tree.children) {
            this.FindsLeafDFS(child, result);
            if (child.children.isEmpty()) {
                result.add(child.key);
            }
        }
    }


    @Override
    public List<E> getMiddleKeys() {

        List<E> result = new ArrayList<>();
        this.findsMiddleDFS(this, result);
        return result;
    }

    private void findsMiddleDFS(Tree<E> tree, List<E> result) {
        for (Tree<E> child : tree.children) {

            if (!child.children.isEmpty()) {
                result.add(child.key);
            }
            this.findsMiddleDFS(child, result);
        }

    }

    @Override
    public Tree<E> getDeepestLeftmostNode() {
        return getDeepestLeftmostNodeDFS(this, 0).deepestLeftmost;
    }

    private Result<E> getDeepestLeftmostNodeDFS(Tree<E> node, int depth) {
        if (node == null) {
            return new Result<>(null, depth - 1);
        }

        if (node.children.isEmpty()) {
            return new Result<>(node, depth);
        }

        Result<E> deepest = new Result<>(null, depth - 1);

        for (Tree<E> child : node.children) {
            Result<E> childResult = getDeepestLeftmostNodeDFS(child, depth + 1);
            if (childResult.depth > deepest.depth) {
                deepest = childResult;
            }
        }

        return deepest;
    }

    private static class Result<E> {
        Tree<E> deepestLeftmost;
        int depth;

        Result(Tree<E> deepestLeftmost, int depth) {
            this.deepestLeftmost = deepestLeftmost;
            this.depth = depth;
        }
    }


    @Override
    public List<E> getLongestPath() {
        
        Tree<E> deepestForMost = getDeepestLeftmostNode();
        return deepestForMost.getPath();

    }

    private List<E> getPath() {
        ArrayDeque<E> stack = new ArrayDeque<>();
        Tree<E> currentTree = this;

        while (currentTree != null) {
            stack.push(currentTree.key);
            currentTree = currentTree.parent;
        }

        return new ArrayList<>(stack);
    }

    private boolean hasParentAndAnyChildren() {
        return this.parent != null && !this.children.isEmpty();
    }


    @Override
    public List<List<E>> pathsWithGivenSum(int sum) {
        List<List<E>> result = new ArrayList<>();
        List<E> currentPath = new ArrayList<>();
        findPathsWithSum(this, sum, currentPath, result);

        int maxLength = 0;
        List<List<E>> longestPaths = new ArrayList<>();

        for (List<E> path : result) {
            if (path.size() > maxLength) {
                maxLength = path.size();
                longestPaths.clear();
                longestPaths.add(path);
            } else if (path.size() == maxLength) {
                longestPaths.add(path);
            }
        }

        return longestPaths;
    }

    @Override
    public List<List<E>> pathsWithGivenSum(int sum) {
        List<List<E>> result = new ArrayList<>();
        List<E> currentPath = new ArrayList<>();
        findPathsWithSum(this, sum, currentPath, result);

        int maxLength = 0;
        List<List<E>> longestPaths = new ArrayList<>();

        for (List<E> path : result) {
            if (path.size() > maxLength) {
                maxLength = path.size();
                longestPaths.clear();
                longestPaths.add(path);
            } else if (path.size() == maxLength) {
                longestPaths.add(path);
            }
        }

        return longestPaths;
    }

    private void findPathsWithSum(Tree<E> currentNode, int targetSum, List<E> currentPath, List<List<E>> result) {
        if (currentNode == null) {
            return;
        }

        currentPath.add(currentNode.key);

        // Check if the current path sum equals the target sum.
        int currentPathSum = sumOfPath(currentPath);
        if (currentPathSum == targetSum) {
            result.add(new ArrayList<>(currentPath));
        }

        for (Tree<E> child : currentNode.children) {
            findPathsWithSum(child, targetSum, currentPath, result);
        }

        currentPath.remove(currentPath.size() - 1);
    }

    private int sumOfPath(List<E> path) {
        int sum = 0;
        for (E key : path) {
            if (key instanceof Integer) {
                sum += (Integer) key;
            }
        }
        return sum;
    }
    @Override
    public List<Tree<E>> subTreesWithGivenSum(int sum) {
        return null;
    }
}



