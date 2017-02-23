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
	
	private Map<FatCat, Set<Capitalist>> cats = new HashMap<>();
	
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
    	
        //if keyset contains cap , return false
        
        if (capitalist instanceof FatCat && cats.keySet().contains(capitalist))
        //if (cats.keySet().contains(capitalist))
        {
        	System.out.println("57: return false, cap was a fatcat inside keyset");
        	return false;
        }
        
        if (capitalist.hasParent())// && cats.keySet().contains(capitalist))
        {

        	if (cats.keySet().contains(capitalist.getParent()))
        	{
        		cats.get(capitalist.getParent()).add(capitalist);
        		return true;
        		
        	}
        	else //capitalist has parent and parent is not in map keys
        	{
        		System.out.println("capitalist has parent, parent not in map keys");
        		
        		if (!add(capitalist.getParent())) return false;
        		
        		if (cats.keySet().contains(capitalist))
                {
                	return false;
                }
        		
        		if (capitalist instanceof FatCat)
        			cats.put((FatCat)capitalist, new HashSet<>());
        		
        		//if (getChildren(capitalist))
        		cats.get(capitalist.getParent()).add(capitalist);
        		return true;
        	}
        	
        }
        else if (capitalist.hasParent() == false)
        {
        	if (capitalist instanceof FatCat)
        	{
        		//No need to check for children since they are not already in the hierarchy
        		
        		//might be ownerless cat that nonetheless has children we don't know about;
        		
        		if (cats.keySet().contains(capitalist))
                {
                	return false;
                }
        		
        		//lonely_cats.add((FatCat)capitalist); 
        		
        		System.out.println("cap with no parent is a FatCat");

        		cats.put((FatCat) capitalist, new HashSet<>());
        	}
        	else
        	{
        		return false;
        	}
        		if (!(capitalist instanceof FatCat)) 
        		{System.out.println("Searching if WageSlave is child of someone already in hier");}
        		else { System.out.println("Searching if FatCat is child of someone already in hier");}
        	
        		Set<Capitalist> caps = getElements();
        	
        		for (Capitalist c : caps)
        		{
        			if (c.getParent().equals(capitalist) && c.getParent() != null)
        			{
        				//give capitalist their children
        				cats.put((FatCat) capitalist, new HashSet<>());
        				cats.get(capitalist).add(c);
        				return true;
        			}
        	
        		}
        		return false;
        
        			
        	
        	//Set<Capitalist> children = getChildren((FatCat)capitalist);
        	
        	//if (children.isEmpty()) return false; 
        	//They can't have children if they are not already in the hierarchy
        	
        	//if (capitalist instanceof FatCat)
        	//{
        	//capitalists.add(capitalist);
        		
        	
        	//}
        	//else 
        	//{
        		//a wage slave with no owner cannot be connected to the network
        		//return false;
        	//}
        	 
        }
        
        //what condition is left?
        System.out.println("default 'false' fired");
        return false;
    }

    /**
     * @param capitalist the element to search for
     * @return true if the element has been added to the hierarchy, false otherwise
     */
    @Override
    public boolean has(Capitalist capitalist) {
    	
    	return getElements().contains(capitalist);

    	//if (capitalists.contains(capitalist)){ return true;}
    	//else {return false;}
    	//if (cats.containsKey(capitalist))
    	//{
    		//return true;
    	//}
    	//Set<FatCat> fatCats = getParents();
    	
    	//for (FatCat cat : fatCats)
    	//{
    		//Set<Capitalist> c = getChildren(cat);
    		//if (c.contains(capitalist)) return true;
    		
    	//}
    	
    	//handle lonely_cats?
    	
    	//return false;
    	
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
    	
    	parents.clear();
    	
    	System.out.println(parents.isEmpty());
    	
    	for (FatCat cat : cats.keySet())
    	{
    		if (cat.hasParent() == false)
    		{
    			continue;
    		}
    		System.out.println("getParents: Parent found!");
    		parents.add(cat.getParent());
    	}
    	
    	//parents = cats.keySet();
    	
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
    	//if (cats.keySet().isEmpty()) return new;
    	
        HashMap<FatCat, Set<Capitalist>> h = new HashMap<FatCat, Set<Capitalist>>(cats);
        return h;
    	
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
