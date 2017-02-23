package com.cooksys.ftd.assignments.collections;

import com.cooksys.ftd.assignments.collections.hierarchy.Hierarchy;
import com.cooksys.ftd.assignments.collections.model.Capitalist;
import com.cooksys.ftd.assignments.collections.model.FatCat;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

public class MegaCorp implements Hierarchy<Capitalist, FatCat> {
	// public static Set<Set<Capitalist>>

	public HashSet<Capitalist> cats = new HashSet<>();

	/**
	 * Adds a given element to the hierarchy.
	 * <p>
	 * If the given element is already present in the hierarchy, do not add it
	 * and return false
	 * <p>
	 * If the given element has a parent and the parent is not part of the
	 * hierarchy, add the parent and then add the given element
	 * <p>
	 * If the given element has no parent but is a Parent itself, add it to the
	 * hierarchy
	 * <p>
	 * If the given element has no parent and is not a Parent itself, do not add
	 * it and return false
	 *
	 * @param capitalist
	 *            the element to add to the hierarchy
	 * @return true if the element was added successfully, false otherwise
	 */

	@Override
	public boolean add(Capitalist capitalist) {
		if (capitalist == null) {
			System.out.println("53");
			return false;
		} else if (cats.contains(capitalist)) {
			System.out.println("58");
			return false;
		}

		else if (capitalist.hasParent()) {
			if (!cats.contains(capitalist.getParent())) {
				if (add(capitalist.getParent())) {
					cats.add(capitalist);
					return true;

				} else {
					// cats.add(capitalist);
					return false;
				}
			} else {
				cats.add(capitalist);

				System.out.println("HasParent, add of Parent returned false");
				// if (capitalist instanceof FatCat)
				// {
				// cats.add(capitalist);
				// return true;
				// }
				return true;
			}

		} else if (!capitalist.hasParent()) {
			if (capitalist instanceof FatCat) {
				System.out.println("75");
				cats.add(capitalist);
				return true;
			}
			System.out.println("Parentless, Childless FatCat!");
			return false;

		}
		System.out.println("default return used");
		return false;

	}
	/*
	*/

	/**
	 * @param capitalist
	 *            the element to search for
	 * @return true if the element has been added to the hierarchy, false
	 *         otherwise
	 */
	@Override
	public boolean has(Capitalist capitalist) {

		// return getElements().contains(capitalist);
		if (capitalist == null)
			return false;

		if (cats.contains(capitalist)) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * @return all elements in the hierarchy, or an empty set if no elements
	 *         have been added to the hierarchy
	 */
	@Override
	public Set<Capitalist> getElements() {

		return new HashSet<>(cats);

	}

	/**
	 * @return all parent elements in the hierarchy, or an empty set if no
	 *         parents have been added to the hierarchy
	 */


	@Override
	public Set<FatCat> getParents() {

		Set<FatCat> fatCats = new HashSet<>();

		for (Capitalist c : cats) {
			if (c instanceof FatCat) {
				fatCats.add((FatCat) c);
			}
		}

		//return fatCats;
		Set<FatCat> parents = new HashSet<>(fatCats);

		return parents;
	}

	/**
     * @param fatCat the parent whose children need to be returned
     * @return all elements in the hierarchy that have the given parent as a direct parent,
     * or an empty set if the parent is not present in the hierarchy or if there are no children
     * for the given parent
     */
    @Override
    public Set<Capitalist> getChildren(FatCat fatCat) {
        
    	Set<Capitalist> children = new HashSet<>();
    	
    	if (!cats.contains(fatCat) || (fatCat == null)) 
    		return new HashSet<>(children); // empty
    	
    	for (Capitalist c : cats)
    	{
    		if (c.getParent() != null && c.getParent() ==  (fatCat))
    		{
    			children.add(c);	
    		}
    		
    	}
    	return new HashSet<>(children);
    }

	/**
	 * @return a map in which the keys represent the parent elements in the
	 *         hierarchy, and the each value is a set of the direct children of
	 *         the associate parent, or an empty map if the hierarchy is empty.
	 */
	@Override
	public Map<FatCat, Set<Capitalist>> getHierarchy() {

		// if (cats.keySet().isEmpty()) return new;

		// HashMap<FatCat, Set<Capitalist>> h = new HashMap<FatCat,
		// Set<Capitalist>>(cats);
		// return h;

		// System.out.println("test");
		Map<FatCat, Set<Capitalist>> hmap = new HashMap<>();

		// Set<Capitalist> all_children = new HashSet<>();

		if (cats.isEmpty())
			return hmap;

		for (Capitalist c : getParents()) {
			hmap.put((FatCat) c, new HashSet<>());
		}

		for (Capitalist c : cats) {
			if (c.hasParent()) {
				hmap.get(c.getParent()).add(c);
			}

		}

		// Capitalist[] c_array;
		// c_array = (Capitalist[]) capitalists.toArray();

		// Set<FatCat> hs = getChildren();

		return hmap;
	}

	/**
	 * @param capitalist
	 * @return the parent chain of the given element, starting with its direct
	 *         parent, then its parent's parent, etc, or an empty list if the
	 *         given element has no parent or if its parent is not in the
	 *         hierarchy
	 */
	@Override
	public List<FatCat> getParentChain(Capitalist capitalist) {
		List<FatCat> fatCats = new ArrayList<>();

		if (!cats.contains(capitalist) || !capitalist.hasParent() || capitalist == null)
			return fatCats;

		Capitalist c = capitalist;

		while (c.hasParent()) {
			fatCats.add((FatCat) c.getParent());

			c = c.getParent();
		}

		return fatCats;

	}
}
