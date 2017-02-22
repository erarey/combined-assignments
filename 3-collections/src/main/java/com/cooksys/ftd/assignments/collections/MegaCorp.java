package com.cooksys.ftd.assignments.collections;

import com.cooksys.ftd.assignments.collections.hierarchy.Hierarchy;
import com.cooksys.ftd.assignments.collections.model.Capitalist;
import com.cooksys.ftd.assignments.collections.model.FatCat;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

public class MegaCorp implements Hierarchy<Capitalist, FatCat> {
	//public static Set<Set<Capitalist>>
	public static HashSet<Capitalist> capitalists = new HashSet<>();
	//public static Hierarchy<Capitalist,FatCat> capitalists;
    
	/*public MegaCorp(Hierarchy<Capitalist, FatCat> h)
	{
		//capitalists = new HashMap<Capitalist, HashSet<FatCat>>();
		capitalists = h;
	}
	*/
	
	/**
     * Adds a given element to the hierarchy.
     * <p>
     * If the given element is already present in the hierarchy,
     * do not add it and return false
     * <p>
     * If the given element has a parent and the parent is not part of the hierarchy,
     * add the parent and then add the given element
     * <p>
     * If the given element has no parent but is a Parent itself,
     * add it to the hierarchy
     * <p>
     * If the given element has no parent and is not a Parent itself,
     * do not add it and return false
     *
     * @param capitalist the element to add to the hierarchy
     * @return true if the element was added successfully, false otherwise
     */
	
    @Override
    public boolean add(Capitalist capitalist) {
    	if (capitalist == null) return false;
    	
        if (has(capitalist))
        {
        	return false;
        }
        else if (capitalist.hasParent() == true)
        {
        	Capitalist iter = capitalist.getParent();
        	
        	while (iter.hasParent() == true)
        	{
        		System.out.println("THIS ONE");
        		capitalists.add(iter);
        		add(iter);
        		iter = iter.getParent();
        	}
        	return true;
        } 
        else if (capitalist.hasParent() == false)
        {
        	if (!(capitalist instanceof FatCat)) return false;
        			
        	Set<Capitalist> children = getChildren((FatCat)capitalist);
        	
        	if (children.isEmpty()) return false;
        	
        	//if (capitalist instanceof FatCat)
        	//{
        		
        	add(capitalist);
        		
        	return true;
        	//}
        	//else 
        	//{
        		//a wage slave with no owner cannot be connected to the network
        		//return false;
        	//}
        }
        
        //what condition is left?
        return false;
    }

    /**
     * @param capitalist the element to search for
     * @return true if the element has been added to the hierarchy, false otherwise
     */
    @Override
    public boolean has(Capitalist capitalist) {
        Set<Capitalist> all_elements = getElements();
        
        if (all_elements.contains(capitalist))
        {
        	return true;
        }
        else
        {
        	return false;
        }
    }

    /**
     * @return all elements in the hierarchy,
     * or an empty set if no elements have been added to the hierarchy
     */
    @Override
    public Set<Capitalist> getElements() {
        
    	//if (capitalists.isEmpty()) return Collections.EMPTY_SET;
    	
    	return capitalists;
    	
    	
    	//Collections.copy(hs.toArray(), capitalists.toArray());
    }

    /**
     * @return all parent elements in the hierarchy,
     * or an empty set if no parents have been added to the hierarchy
     */
    @Override
    public Set<FatCat> getParents() {
    	
    	Set<FatCat> parents = new HashSet<>();
    	
        for (Capitalist c : capitalists)
        {
        	if (c.hasParent())
        	{
        		parents.add((FatCat) c);
        	}
        	
        }
        
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
    	
    	if (!capitalists.contains(fatCat)) return children; // empty
    	
    	for (Capitalist c : capitalists)
    	{
    		if (c.hasParent())
    		{
    			if (c.getParent() == (fatCat))
    			{
    				children.add(c);
    			}
    		}
    		
    	}
    	
    	return children;
    }

    /**
     * @return a map in which the keys represent the parent elements in the hierarchy,
     * and the each value is a set of the direct children of the associate parent, or an
     * empty map if the hierarchy is empty.
     */
    @Override
    public Map<FatCat, Set<Capitalist>> getHierarchy() {
        
    	//System.out.println("test");
    	Map<FatCat, Set<Capitalist>> hmap = new HashMap<>();
    	
    	for (Capitalist c : capitalists)
    	//Iterator<Capitalist> iter = capitalists.iterator();
    	//while(iter.hasNext())	
    	{
    		System.out.println("test");
    		if (c instanceof FatCat)
    		{
    			Set<Capitalist> s = getChildren((FatCat) c);
    			
    			hmap.put((FatCat)c, s);
    		}
    		
    	}
    	
    	return hmap;
    }

    /**
     * @param capitalist
     * @return the parent chain of the given element, starting with its direct parent,
     * then its parent's parent, etc, or an empty list if the given element has no parent
     * or if its parent is not in the hierarchy
     */
    @Override
    public List<FatCat> getParentChain(Capitalist capitalist) {
    	List<FatCat> fatCats = new ArrayList<>();
        
    	if (!capitalists.contains(capitalist)) return fatCats;
    	
    	if (!capitalist.hasParent()) return fatCats;
    	
    	Capitalist c = capitalist.getParent();
    	
    	while (c.hasParent())
    	{
    		fatCats.add((FatCat) c);
    		
    		c = c.getParent();
    	}
    	
    	return fatCats;

        
    }
}
