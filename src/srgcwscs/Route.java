/* 
 	File is part of CwsMcsLib.

    CwsMcsLib is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, GPLv2.

    CwsMcsLib is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with CwsMcsLib.  If not, see <http://www.gnu.org/licenses/>.
 */

package srgcwscs;

import java.util.ArrayList;

/**
* @class Route srgcwscs.Route
*
* @brief This class represents a route (array of edges) of a VRP instance.
* 
* @authors Angel A. Juan, Miquel Gilibert, Marcos Fernández Callejo
* 
* @package srgcwscs;
* 
* @date 050113
*
* @copyright GNU Public License, version 2.
**/
public class Route 
{

	/**
     * @brief Number of route instances
     */
    private static int nInstances = 0; 
    
    /**
     * @brief Route Identification
     */
    private int id; 
    
    /**
     * @brief Total route costs
     */
    private double costs; 
    
    /**
     * @brief Total Route Demand
     */
    private int demand; 
    
    /**
     * @brief List of edges
     */
    private ArrayList<Edge> edges; 
    
    /**
     * @brief Nodes in the route
     */
    private ArrayList<Node> nodes; 
    
    /**
     * @brief x-coordinate of the route center
     */
    private float xRouteCenter;  
    
    /**
     * @brief y-coordinate of the route center
     */
    private float yRouteCenter; 
    
    /**
     * @brief Vehicle associated to the route
     */
    private Vehicle vehicle;

    /**
  	* @brief Route Constructor
  	*/
    public Route() 
    {
        nInstances++;
        id = nInstances;
        costs = 0;
        demand = 0;
        edges = new ArrayList<Edge>();
        nodes = new ArrayList<Node>();
        xRouteCenter = (float) 0.0;
        yRouteCenter = (float) 0.0;
    }
    
    /**
  	* @brief Copy Route Constructor
  	* 
  	* @param Original reference route for the copy
  	*/
    public Route(Route referenceRoute) 
    {
        nInstances = referenceRoute.nInstances;
        id = referenceRoute.id;
        costs = referenceRoute.costs;
        demand = referenceRoute.demand;
        edges = referenceRoute.edges;
        nodes = referenceRoute.nodes;
        xRouteCenter = referenceRoute.xRouteCenter;
        yRouteCenter = referenceRoute.yRouteCenter;
    }

    /**
  	* @brief Adds a new edge to he route
  	*
  	* @remarks Edges nodes are also added to the route
  	*/
    public void addEdge(Edge anEdge) 
    {
        edges.add(anEdge);

        if (nodes.contains(anEdge.getOrigin()) == false) 
        {
            nodes.add(anEdge.getOrigin());
        }

        if (nodes.contains(anEdge.getEnd()) == false) 
        {
            nodes.add(anEdge.getEnd());
        }
    }
    
    /**
  	* @brief Resets Edges and nodes from the route
  	*/
    public void resetEdges()
    {
    	edges.clear();
    	nodes.clear();
    }
    
    /**
  	* @brief Sets a new list of edges and nodes
  	* 
  	* @param newEdges - ArrayList<Edge> new Edges
  	* 
  	* @param newNodes - ArrayList<Node> newNodes
  	*/
    public void setEdges(ArrayList<Edge> newEdges, ArrayList<Node> newNodes)
    {
    	edges = newEdges;
    	nodes = newNodes;
    }

    /**
  	* @brief Removes a given edge
  	* 
  	* @param anEdge - Edge to be removed
  	*/
    public void removeEdge(Edge anEdge) 
    {
        edges.remove(anEdge);
    }

    /**
  	* @brief Updates route cost add an edge cost
  	* 
  	* @param anEdge - Edge to add costs
  	*/
    public void addCosts(Edge anEdge) 
    {
        costs += anEdge.getCosts();
    }

    /**
  	* @brief Remove edge cost from total route cost
  	* 
  	* @param anEdge - Edge to remove costs
  	*/
    public void substractCosts(Edge anEdge) 
    {
        costs -= anEdge.getCosts();
    }

    /**
  	* @brief Update route costs
  	* 
  	* @param c - int new costs value
  	*/
    public void setCosts(int c) 
    {
        costs = c;
    }

    /**
  	* @brief Update X coordinate route center
  	* 
  	* @param x - float coordinate 
  	*/
    public void setXRouteCenter(float x) 
    {
        xRouteCenter = x;
    }

    /**
  	* @brief Update Y coordinate route center
  	* 
  	* @param Y - float coordinate 
  	*/
    public void setYRouteCenter(float y) 
    {
        yRouteCenter = y;
    }

    /**
  	* @brief add Edge demand to the total route demand
  	* 
  	* @param anEdge - Edge demand to be added to route demand
  	*/
    public void addDemand(Edge anEdge) 
    {
        Node endNode = anEdge.getEnd();
        demand += endNode.getDemand();
    }

    /**
  	* @brief Get route costs
  	* 
  	* @return double - route costs
  	*/
    public double getCosts() 
    {
        return costs;
    }

    /**
  	* @brief Get route demand
  	* 
  	* @return double - route demand
  	*/
    public int getDemand() 
    {
        return demand;
    }

    /**
  	* @brief Get route identification
  	* 
  	* @return int - route identification
  	*/
    public int getId() 
    {
        return id;
    }

    /**
  	* @brief Get route edges
  	* 
  	* @return ArrayList<Edge> - List of edges
  	*/
    public ArrayList<Edge> getEdges() 
    {
        return edges;
    }

    /**
  	* @brief Get route nodes
  	* 
  	* @return ArrayList<Node> - List of nodes
  	*/
    public ArrayList<Node> getNodes() 
    {
        return nodes;
    }

    /**
  	* @brief Get route X Center
  	* 
  	* @return float - x Route Center
  	*/
    public float getXRouteCenter() 
    {
        return xRouteCenter;
    }

    /**
  	* @brief Get route Y Center
  	* 
  	* @return float - y Route Center
  	*/
    public float getYRouteCenter() 
    {
        return yRouteCenter;
    }

    /**
  	* @brief Get the associated vehicle to the route
  	* 
  	* @return Vehicle - Vehicle Associated
  	*/
    public Vehicle getVehicle() 
    {
        return vehicle;
    }

    /**
  	* @brief Updates the associated vehicle to the route
  	* 
  	* @param vehicle - Vehicle Associated
  	*/
    public void setVehicle(Vehicle vehicle) 
    {
        this.vehicle = vehicle;
    }



    /**
  	* @brief Gets a string formatted information related to the route
  	* 
  	* @returns String  - route characteristics
  	*/
    public String toString() {
        String s = "";
        s = s.concat("\nRuta Id: " + this.getId());
        s = s.concat("\nRute costs: " + (this.getCosts()));
        s = s.concat("\nRuta demand:" + this.getDemand());
        s = s.concat("\nRuta edges: " + this.getEdges());
        return s;
    }
}
