package com.navimee.foursquareCategories;

import com.google.firebase.database.utilities.Pair;
import com.navimee.models.dto.categories.FsCategoriesDto;
import com.navimee.staticData.NavimeeData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class CategoryTree {

    private CategoryNode root;
    private Map<String, CategoryNode> nodes;
    private List<String> forbiddenCategories;

    public CategoryTree() {
        this.root = new CategoryNode("ROOT", "ROOT");
        this.nodes = new HashMap<>();
        this.forbiddenCategories = new NavimeeData().getCategories();
    }

    public CategoryTree build(List<FsCategoriesDto> categoriesDtos) {
        FsCategoriesDto dto = new FsCategoriesDto();
        dto.setCategories(categoriesDtos);
        build(dto, root);
        return this;
    }

    public Pair<CategoryNode, Boolean> isForbidden(List<String> categories) {
        List<Pair<CategoryNode, Boolean>> available = new ArrayList<>();
        categories = categories.stream().map(String::toUpperCase).collect(toList());
        categories.forEach(category -> {
            Pair<CategoryNode, Boolean> forbidden = isForbidden(category);
            if (!forbidden.getSecond())
                available.add(forbidden);
        });

        return available.size() > 0 ? available.get(0) : new Pair<>(null, true);
    }

    private Pair<CategoryNode, Boolean> isForbidden(String category) {
        category = category.toUpperCase();
        if (!this.nodes.containsKey(category))
            return new Pair<>(null, true);

        CategoryNode node = this.nodes.get(category);
        while (node.getParent() != null) {
            if (this.forbiddenCategories.contains(node.getCategoryName()))
                return new Pair<>(node, true);

            node = node.getParent();

            // The last node which is not the root.
            if (node.getParent().getParent() == null)
                break;
        }

        return new Pair<>(node, false);
    }

    private void build(FsCategoriesDto dto, CategoryNode node) {

        dto.getCategories().forEach(category -> {
            CategoryNode leaf = new CategoryNode();
            nodes.put(category.getName(), leaf);

            leaf.setCategoryId(category.getId());
            leaf.setCategoryName(category.getName());
            leaf.setParent(node);

            node.getLeafs().add(leaf);

            build(category, leaf);
        });
    }
}
