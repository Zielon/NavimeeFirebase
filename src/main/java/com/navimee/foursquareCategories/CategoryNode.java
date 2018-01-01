package com.navimee.foursquareCategories;

import java.util.ArrayList;
import java.util.List;

public class CategoryNode {

    private CategoryNode parent;
    private List<CategoryNode> leafs;
    private String categoryName;
    private String categoryId;

    public CategoryNode() {
        leafs = new ArrayList<>();
    }

    public CategoryNode(String categoryName, String categoryId) {
        this();
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public CategoryNode getParent() {
        return parent;
    }

    public void setParent(CategoryNode parent) {
        this.parent = parent;
    }

    public List<CategoryNode> getLeafs() {
        return leafs;
    }

    public void setLeafs(List<CategoryNode> leafs) {
        this.leafs = leafs;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
