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
* @class Inputs srgcwscs.Inputs
*
* @brief This class represents the basic inputs for a VRP instance, a set of nodes, edges and vehicles.
* 
* @authors Angel A. Juan, Miquel Gilibert, Marcos Fernandez 
*
* @package srgcwscs;
* 
* @date 050113
*
* @copyright GNU Public License, version 2.
**/
public class Inputs 
{

	/**
     * @brief NodeList Array of nodes
     */
    static private Node[] NodeList;
    
    /**
     * @brief EdgeList ArrayList of edges
     */
    static private Edge[] EdgeList;
   
    /**
     * @brief EdgeList ArrayList of vehicles
     */
    private ArrayList<Vehicle> vehiclesList;
    
    /**
   	* @brief Inputs Constructor
   	*
   	* @param n - int Total number of nodes   
   	*/
    public Inputs(int n) 
    {
        NodeList = new Node[n];
        EdgeList = new Edge[(n - 1) * (n - 2) / 2]; // The depot is not considered
        vehiclesList=new ArrayList<Vehicle>();
    }

    /**
   	* @brief Gets an array of nodes corresponding to the customers of the problem
   	*
   	* @return Node[] - Array of nodes to supply   
   	*/
    public Node[] getNodeList() 
    {
        return NodeList;
    }

    /**
   	* @brief Sets an array of nodes corresponding to the customers of the problem
   	*
   	* @param NodeList - Node[] array of nodes to supply   
   	*/
    public void setNodeList(Node[] NodeList) 
    {
        this.NodeList = NodeList;
    }

    public static void setEdgeList(Edge[] EdgeList) 
    {
        Inputs.EdgeList = EdgeList;
    }

    /**
   	* @brief Returns an array list of edges
   	*    	
   	* @return ArrayList<Edge> - array list of edges
   	*/
    public Edge[] getEdgeList()
    {
        return EdgeList;
    }

    /**
   	* @brief Sets a given node in a given position of the array of nodes
   	*
   	* @param i position in the array to insert the node
   	*
   	* @param NodeList - Node[] array of nodes to supply   
   	*/
    public void setNode(int i, Node node) 
    {
        NodeList[i] = node;
    }

    /**
   	* @brief Sets a given edge in a given position of the array list of edges
   	*
   	* @param i position in the array list to insert the node
   	*
   	* @param NodeList - Node[] array of nodes to supply   
   	*/
    public void sedEdge(int i, Edge edge) 
    {
        EdgeList[i] = edge;
    }

    /**
   	* @brief Gets an array list of available vehicles
   	*
   	* @returns ArrayList<Vehicle> - Array List of vehicles   
   	*/
    public ArrayList<Vehicle> getVehiclesList() 
    {
        return vehiclesList;
    }

    /**
   	* @brief Adds a new vehicle to the array list of available vehicles
   	*
   	* @param v - Vehicle vehicle instance to be added  
   	*/
    public void setVehicle(Vehicle v)
    {
        vehiclesList.add(v);
    }

    /**
   	* @brief Gets a vehicle stored in a given position of the array list of available vehicles
   	*
   	* @param indes - int indes in the array list for the vehicle to recover
   	* 
   	* @return Vehicle - instance to the vehicle
   	*/
    public Vehicle getVehicle(int index)
    {
        return vehiclesList.get(index);
    }

    /**
   	* @brief Creates an array list of edges connecting the problem nodes
   	*/
    public void fillEdgeList() 
    {
    	int n = 0;
        for (int i = 1; i < NodeList.length - 1; i++) // node 0 is the depot
        {
            for (int j = i + 1; j < NodeList.length; j++) 
            {
                Node iNode = NodeList[i];
                Node jNode = NodeList[j];
                Edge ijEdge = new Edge(iNode, jNode);
                EdgeList[n] = ijEdge;
                n++;
            }
        }
    }
    
}
