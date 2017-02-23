package com.cooksys.ftd.assignments.collections;

import com.cooksys.ftd.assignments.collections.hierarchy.Hierarchy;
import com.cooksys.ftd.assignments.collections.model.Capitalist;
import com.cooksys.ftd.assignments.collections.model.FatCat;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

public class MegaCorp implements Hierarchy<Capitalist, FatCat> {
	//public static Set<Set<Capitalist>>
	//public static HashSet<Capitalist> capitalists = new HashSet<>();
	
	//public static HashSet<FatCat> known_fatCats = new HashSet<>();
	
	public Map<FatCat, Set<Capitalist>> cats = new HashMap<>();
	
	public  ArrayList<FatCat> lonely_cats = new ArrayList<>();
	//public static ArrayList<Capitalist> capitalists = new ArrayList<>();
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
    	
        if (cats.keySet().contains(capitalist))
        {
        	return false;
        }
        
        if (!(capitalist instanceof FatCat) && !capitalist.hasParent()) return false;
        
        //experimental
        if (capitalist instanceof FatCat 
        		&& !cats.containsKey(capitalist)) 
        {
        	lonely_cats.add((FatCat) capitalist);
        }
        //end
        	
        if (capitalist.hasParent())// && cats.keySet().contains(capitalist))
        {
        	Capitalist iter = capitalist;
        	
        	while (iter.hasParent())
        	{
        		if (!cats.keySet().contains(iter))
        		{
        			cats.put((FatCat) iter.getParent(), new HashSet<>());
        		}
        		
        		cats.get(iter.getParent()).add(iter);
        		
        		//if (success)
        		//{
        		
        		//}
        		//else
        		//{
        		//could not add iter's parent, were they already in the hierarchy?
        		//if (cats.containsKey(iter.getParent()))
        		//{
        			//cats.get(iter.getParent()).add(iter);
        		//}
        		//was there parent a childless fat cat without an owner (lonely_cat)?
        		if (lonely_cats.contains(iter.getParent()))
        		{
        			if (!cats.keySet().contains(iter.getParent()))
        			{
        				cats.put(iter.getParent(), new HashSet<>());
        				cats.get(iter.getParent()).add(iter);
        				lonely_cats.remove(iter.getParent());
        			}
        			else
        			{
        				//lonely_cats.remove(iter.getParent());
        			}
        		}
        		//}
        		add(iter.getParent());
        		
        		iter = iter.getParent();
        	}
        	
        	//capitalists.add(capitalist);
        	
        	//add(capitalist.getParent());
        	
        	//capitalists.add(capitalist);
        	
        	//add(capitalist.getParent());
        		
        } 
        else if (capitalist.hasParent() == false)
        {
        	if (capitalist instanceof FatCat)
        	{
        		//No need to check for children since they are not already in the hierarchy
        		
        		//might be ownerless cat that nonetheless has children we don't know about;
        		lonely_cats.add((FatCat)capitalist);
        	}
        			
        	
        	//Set<Capitalist> children = getChildren((FatCat)capitalist);
        	
        	//if (children.isEmpty()) return false; 
        	//They can't have children if they are not already in the hierarchy
        	
        	//if (capitalist instanceof FatCat)
        	//{
        	//capitalists.add(capitalist);
        		
        	return false;
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
    	//if (capitalists.contains(capitalist)){ return true;}
    	//else {return false;}
    	if (cats.containsKey(capitalist))
    	{
    		return true;
    	}
    	Set<FatCat> fatCats = getParents();
    	
    	for (FatCat cat : fatCats)
    	{
    		Set<Capitalist> c = getChildren(cat);
    		if (c.contains(capitalist)) return true;
    		
    	}
    	
    	//handle lonely_cats?
    	
    	return false;
    	
        //Set<Capitalist> all_elements = getElements();
        /*
        if (all_elements.contains(capitalist))
        {
        	return true;
        }
        else
        {
        	return false;
        }
        */
    }

    /**
     * @return all elements in the hierarchy,
     * or an empty set if no elements have been added to the hierarchy
     */
    @Override
    public Set<Capitalist> getElements() {
        
    	//if (capitalists.isEmpty()) return Collections.EMPTY_SET;
    	
    	//return capitalists;
    	
    	//if (cats.keySet().isEmpty()) return Collections.EMPTY_SET;
    	
    	Set<Capitalist> elements = new HashSet<>();
    	
    	if (cats.keySet().isEmpty()) return elements;
    	
    	for (Capitalist c : cats.keySet())
    	{
    		//include FatCat key itself?
    		
    		elements.addAll(cats.get(c));
    		elements.add(c);
    	}
    	
    	return elements;
    	//Collections.copy(hs.toArray(), capitalists.toArray());
    }

    /**
     * @return all parent elements in the hierarchy,
     * or an empty set if no parents have been added to the hierarchy
     */
    @Override
    public Set<FatCat> getParents() {
    	
    	Set<FatCat> parents = new HashSet<>();
    	
    	parents = cats.keySet();
    	
    	/*
        for (Capitalist c : capitalists)
        {
        	if (c.hasParent())
        	{
        		parents.add((FatCat) c);
        	}
        	
        }
        */
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
    	
    	//if (!capitalists.contains(fatCat)) return children; // empty
    	if (!cats.containsKey("fatCat")) return children; //empty
    	
    	children = cats.get("fatCat");
    	/*
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
    	*/
    	
    	return children;
    }

    /**
     * @return a map in which the keys represent the parent elements in the hierarchy,
     * and the each value is a set of the direct children of the associate parent, or an
     * empty map if the hierarchy is empty.
     */
    @Override
    public Map<FatCat, Set<Capitalist>> getHierarchy() {
        
    	return cats;
    	
    	/*
    	//System.out.println("test");
    	Map<FatCat, Set<Capitalist>> hmap = new HashMap<>();
    	
    	if (capitalists.isEmpty()) return hmap;
    	
    	//Capitalist[] c_array;
    	//c_array = (Capitalist[]) capitalists.toArray();
    	
    	Set<FatCat> hs = getParents();
    	
    	for (Capitalist c : hs)
    	//for (int i = 0; i < c_array.length; i++)
    	//Iterator<Capitalist> iter = capitalists.iterator();
    	//while(iter.hasNext())	
    	{
    		////System.out.println("test");
    		if (c == null) break;
    		
    		//if (c instanceof FatCat)
    		//{
    			Set<Capitalist> s = getChildren((FatCat) c);
    			//System.out.println("test2");
    			hmap.put((FatCat)c, s);
    			System.out.println("map size "+ hmap.size() + " cap size " + capitalists.size());
    		//}
    		
    	}
    	
    	return hmap;
    	*/
    	
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
        
    	//if (!capitalists.contains(capitalist)) return fatCats;
    	
    	if (!cats.containsKey(capitalist) || !capitalist.hasParent() 
    		|| capitalist == null) return Collections.EMPTY_LIST;
    	
    	//if (!capitalist.hasParent()) return fatCats;
    	
    	Capitalist c = capitalist.getParent();
    	
    	while (c.hasParent())
    	{
    		fatCats.add((FatCat) c);
    		
    		c = c.getParent();
    	}
    	
    	return fatCats;

        
    }
    
}
