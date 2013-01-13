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
* @class Edge srgcwscs.Edge
*
* @brief This class represents an edge connecting two nodes in the VRP
* 
* @authors Angel A. Juan , Miquel Gilibert, Marcos Fernandez Callejo 
*
* @package srgcwscs;
* 
* @date 020113
*
* @copyright GNU Public License, version 2.
**/
public class Edge implements Comparable<Edge> 
{
	/**
	 * @brief number of instances
	 */
    private static int nInstances = 0; 
    
    /**
	 * @brief edge ID
	 */
    private int id; 
    
    /**
	 * @brief origin node
	 */
    private Node origin; 
    
    /**
	 * @brief end node
	 */
    private Node end;
    
    /**
	 * @brief edge costs 
	 */
    private double costs; 
    
    /**
	 * @brief edge savings (Clarke & Wright)
	 */
    private double savings; 
    
    /**
	 * @brief route containing this edge (0 if no route assigned)
	 */
    private int inRoute = 0; 
    
    /**
	 * @brief is forward direction
	 */
    private boolean isForward = true;
    
    /**
	* @brief Edge Constructor
	*
	* @param originNode - Node starting edge node
	* 
	* @param endNode - Node Ending edge node 
	* 
	*/
    public Edge(Node originNode, Node endNode) 
    {
        nInstances++;
        id = nInstances;
        origin = originNode;
        end = endNode;
        costs = calcCostsEdge(origin, end);
        savings = calcSavingsEdge(origin, end);
        isForward = true;
    }
    
    /**
	* @brief Edge Copy Constructor
	*
	* @param referenceEdge - Edge to copy
	* 
	*/
    public Edge(Edge referenceEdge) 
    {
        id = referenceEdge.id;
        origin = referenceEdge.origin;
        end = referenceEdge.end;
        costs = referenceEdge.costs;
        savings = referenceEdge.savings;
        isForward = referenceEdge.isForward;
    }

    /**
   	* @brief Update edge costs
   	*
   	* @param edgeCosts - double new edge cost
   	*/
    public void setCosts(double edgeCosts) 
    {
        costs = edgeCosts;
    }

    /**
   	* @brief Update savings value
   	*
   	* @param edgeSavings - double new edge savings value
   	*/
    public void setSavings(double edgeSavings) 
    {
        savings = edgeSavings;
    }

    /**
   	* @brief Update the route associated to the edge
   	*
   	* @param routeId - int route id
   	*/
    public void setInRoute(int routeId) 
    {
        inRoute = routeId;
    }

    /**
   	* @brief Get Starting Node
   	*
   	* @return Node - reference to the origin node
   	*/
    public Node getOrigin() 
    {
    	if(isForward)
    	{
    		return origin;
    	}
    	else
    	{
    		return end;
    	}
    }

    /**
   	* @brief Get Ending Node
   	*
   	* @return Node - reference to the end node
   	*/
    public Node getEnd() 
    {
        if(isForward)
        {
        	return end;
        }
        else
        {
        	return origin;
        }
    }

    /**
   	* @brief Get Edge id
   	*
   	* @return int - edge id
   	*/
    public int getId() {
        return id;
    }

    /**
   	* @brief Get Edge costs
   	*
   	* @return double - costs associated to the edge
   	*/
    public double getCosts() 
    {
        return costs;
    }

    /**
   	* @brief Get Edge Savings value
   	*
   	* @return double - cws saving value for the edge
   	*/
    public double getSavings() 
    {
        return savings;
    }

    /**
   	* @brief Get Route id associated to the edge
   	* 
   	* @remarks 0 if no route is associated to the edge
   	*
   	* @return int - associted route id
   	*/
    public int getInRoute() 
    {
        return inRoute;
    }

    
    /**
   	* @brief Reverse the edge, starting and ending nodes are swapped
   	*/
    public void reverse() 
    {
    	if(isForward)
    	{
    		isForward = false;
    	}
    	else
    	{
    		isForward = true;
    	}
    }
    
    /**
  	* @brief Method used to get Edge information in a string mode
  	*
  	* @returns String - string formated information related to the edge
  	*/ 
    public String toString() 
    {
        String s = "";
        s = s.concat("\nEdge Id: " + this.getId());
        s = s.concat("\nEdge origin: " + this.getOrigin());
        s = s.concat("\nEdge end: " + this.getEnd());
        s = s.concat("\nEdge costs: " + (this.getCosts()));
        s = s.concat("\nEdge savings: " + (this.getSavings()));
        return s;
    }

    /**
     * @brief Compare the node id against a given node
     * 
     * @return 1 if the node is the lowest -1 if the given node is the lowest 0 if both nodes have the same id
     */
    public int compareTo(Edge otherEdge) 
    {
        if (this.savings < otherEdge.getSavings()) 
        {
            return -1;
        } 
        else if (this.savings > otherEdge.getSavings()) 
        {
            return 1;
        }
        return -1;
    }

   
    /**
     * @brief Calculates the edges cost, geometric distance between nodes
     * 
     * @return double - edge cost
     */
    public double calcCostsEdge(Node on, Node en) 
    {
        double Xo = on.getX();
        double Yo = on.getY();
        double Xe = en.getX();
        double Ye = en.getY();

        return Math.sqrt((Xe - Xo) * (Xe - Xo) + (Ye - Yo) * (Ye - Yo));
    }

    /**
     * @brief Calculates the edges savings value according to Clarke and Wright definition
     * 
     * @return double - savings value
     */
    public double calcSavingsEdge(Node no, Node ne) 
    {
        double Xo = no.getX();
        double Yo = no.getY();
        double Xe = ne.getX();
        double Ye = ne.getY();

        //Cost originNode to depot
        double cod = Math.sqrt((0 - Xo) * (0 - Xo) + (0 - Yo) * (0 - Yo));

        //cost depot to endNode
        double cde = Math.sqrt((Xe - 0) * (Xe - 0) + (Ye - 0) * (Ye - 0));

        //Return cost depot to savings
        return cod + cde - costs;

    }
}
