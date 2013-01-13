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


/**
* @class Node srgcwscs.Node
*
* @brief This class represents a node in the VRP. A node could be a customer (delivery point)
* or a central point for distribution (Depot - Node 0)
* 
* @authors Angel A. Juan,Juan Carlos Cruz, Marcos Fernandez
*
* @package srgcwscs;
* 
* @date 020113
*
* @copyright GNU Public License, version 2.
**/
public class Node
{

	/**
     * @brief Node Identification
     */
    private int id;
    
    /**
     * @brief Node x coordinate
     */
    private double x; 
    
    /**
     * @brief Node y coordinate
     */
    private double y; 
    
    /**
     * @brief Node demand
     */
    private int demand;   
    
    /**
     * @brief id of route containing the node
     */
    private int inRoute; 
    
    /**
     * @brief Node interior -> no direct connection with depot
     */
    private boolean isInterior;
    
    /**
     * @brief Route references associated to the node
     */
    private Route route;

    /**
  	* @brief Node Constructor
  	*
  	* @param id  - int node identification
  	* 
  	* @param nodeX - double x node coordinate
  	* 
  	* @param nodeY - double Y node coordinate
  	* 
  	* @param nodeDemand - int demand associated to the node
  	*
  	*/
    public Node(int nodeId, double nodeX, double nodeY, int nodeDemand) 
    {
        id = nodeId;
        x = nodeX;
        y = nodeY;
        demand = nodeDemand;
        inRoute = -1; // node is not assigned to any route yet
        isInterior = false; // initially, all nodes are exterior ones
        route = null; // initially, there is no route associated to the node
    }
   
    /**
  	* @brief Updates node identification
  	*
  	* @param nodeId  - int new node identification
  	*/
    public void setId(int nodeId) 
    {
        id = nodeId;
    }

    /**
  	* @brief Updates node x coordinate
  	*
  	* @param nodeX  - float new node X coordinate
  	*/
    public void setX(float nodeX) 
    {
        x = nodeX;
    }

    /**
  	* @brief Updates node y coordinate
  	*
  	* @param nodeY  - float new node Y coordinate
  	*/
    public void setY(float nodeY) 
    {
        y = nodeY;
    }

    /**
  	* @brief Updates node demand
  	*
  	* @param nodeDemand  - int new node demand
  	*/
    public void setDemand(int nodeDemand) 
    {
        demand = nodeDemand;
    }

    /**
  	* @brief Updates route identification associated to the node 
  	*
  	* @param routeId  - int new route identification for the node
  	*/
    public void setInRoute(int routeId) 
    {
        inRoute = routeId;
    }

    /**
  	* @brief Updates the nodes status in the route
  	* 
  	* @remarks isInterior true when the node is not directly connected to the central depot (node 0) 
  	*
  	* @param value  - boolean new status state
  	*/
    public void setIsInterior(boolean value) 
    {
        isInterior = value;
    }
    
    /**
  	* @brief Updates the route instance associated to the node
  	* 
  	* @param newRoute  - instance to the route which the node is associated
  	*/
    public void setRoute(Route newRoute) 
    {
        route = newRoute;
    }

    /**
  	* @brief Gets the nodes identification
  	* 
  	* @returns int  - node identification
  	*/
    public int getId() 
    {
        return id;
    }

    /**
  	* @brief Gets x coordinate
  	* 
  	* @returns double  - x coordinate
  	*/
    public double getX() 
    {
        return x;
    }

    /**
  	* @brief Gets y coordinate
  	* 
  	* @returns double  - y coordinate
  	*/
    public double getY() 
    {
        return y;
    }

    /**
  	* @brief Gets node demand
  	* 
  	* @returns int  - node demand
  	*/
    public int getDemand() 
    {
        return demand;
    }

    /**
  	* @brief Gets route identification associated to the node
  	* 
  	* @returns int  - route identification
  	*/
    public int getInRoute() 
    {
        return inRoute;
    }

    /**
  	* @brief Gets route status in the routes
  	* 
  	* @returns int  - false if the route is directly connected to the central depot otherwise true
  	*/
    public boolean isInterior() 
    {
        return isInterior;
    }
    
    /**
  	* @brief Gets the route instance which the node is associated 
  	* 
  	* @remarks null if the node is not associated to any route
  	* 
  	* @return Route  - instance to the route which the node is associated
  	*/
    public Route getRoute() 
    {
        return route;
    }

    /**
  	* @brief Gets a string formatted information related to the node
  	* 
  	* @returns String  - node characteristics
  	*/
    public String toString() 
    {
        String s = "";
        s = s.concat(this.getId() + " ");
        s = s.concat(this.getX() + " ");
        s = s.concat(this.getY() + " ");
        s = s.concat(this.getDemand() + "");
        return s;
    }
    
    
}
