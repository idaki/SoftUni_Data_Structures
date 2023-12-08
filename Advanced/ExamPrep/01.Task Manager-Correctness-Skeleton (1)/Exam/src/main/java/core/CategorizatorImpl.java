package core;

import models.Category;

import java.util.*;


public class CategorizatorImpl implements Categorizator {

    private Map<String, Category> categories;
    private Map<String, Set<String>> parentChildMapping;

    public CategorizatorImpl() {
        this.categories = new HashMap<>();
        this.parentChildMapping = new HashMap<>();
    }

    @Override
    public void addCategory(Category category) {
        if (categories.containsKey(category.getId())) {
            throw new IllegalArgumentException("Category already exists with id: " + category.getId());
        }
        categories.put(category.getId(), category);
    }

    @Override
    public void assignParent(String childCategoryId, String parentCategoryId) {
        Category child = categories.get(childCategoryId);
        Category parent = categories.get(parentCategoryId);

        if (child == null || parent == null) {
            throw new IllegalArgumentException("Invalid category id provided.");
        }

        Set<String> children = parentChildMapping.computeIfAbsent(parent.getId(), k -> new HashSet<>());
        if (children.contains(child.getId())) {
            throw new IllegalArgumentException("Child category is already assigned to the parent category.");
        }

        children.add(child.getId());
    }

    @Override
    public void removeCategory(String categoryId) {
        Category category = categories.get(categoryId);
        if (category == null) {
            throw new IllegalArgumentException("Category not found with id: " + categoryId);
        }

        // Remove the category and its children
        Set<String> children = parentChildMapping.getOrDefault(category.getId(), Collections.emptySet());
        for (String childId : children) {
            removeCategory(childId);
        }

        categories.remove(category.getId());
        parentChildMapping.values().forEach(childIds -> childIds.remove(category.getId()));
    }

    @Override
    public boolean contains(Category category) {
        return categories.containsValue(category);
    }

    @Override
    public int size() {
        return categories.size();
    }

    @Override
    public Iterable<Category> getChildren(String categoryId) {
        Set<String> childIds = parentChildMapping.getOrDefault(categoryId, Collections.emptySet());
        List<Category> children = new ArrayList<>();
        for (String childId : childIds) {
            children.add(categories.get(childId));
        }
        return children;
    }

    @Override
    public Iterable<Category> getHierarchy(String categoryId) {
        if (!categories.containsKey(categoryId)) {
            throw new IllegalArgumentException("Category not found with id: " + categoryId);
        }

        List<Category> hierarchy = new ArrayList<>();
        buildHierarchy(categoryId, hierarchy);
        return hierarchy;
    }

    @Override
    public Iterable<Category> getTop3CategoriesOrderedByDepthOfChildrenThenByName() {
        List<Category> topCategories = new ArrayList<>(categories.values());
        topCategories.sort(Comparator.comparingInt(this::calculateDepthByCategory).thenComparing(Category::getName));

        // Return top 3 or all if less than 3
        return topCategories.subList(0, Math.min(3, topCategories.size()));
    }

    private void buildHierarchy(String categoryId, List<Category> hierarchy) {
        Category category = categories.get(categoryId);
        if (category == null) {
            throw new IllegalArgumentException("Category not found with id: " + categoryId);
        }

        hierarchy.add(category);
        Set<String> parentIds = parentChildMapping.getOrDefault(categoryId, Collections.emptySet());
        for (String parentId : parentIds) {
            buildHierarchy(parentId, hierarchy);
        }
    }

    private int calculateDepthByCategory(Category category) {
        return calculateDepth(category.getId());
    }

    private int calculateDepth(String categoryId) {
        Category category = categories.get(categoryId);
        if (category == null) {
            throw new IllegalArgumentException("Category not found with id: " + categoryId);
        }

        Set<String> childIds = parentChildMapping.getOrDefault(categoryId, Collections.emptySet());
        if (childIds.isEmpty()) {
            return 0;
        }

        int maxDepth = 0;
        for (String childId : childIds) {
            maxDepth = Math.max(maxDepth, calculateDepth(childId));
        }

        return maxDepth + 1;
    }
}
