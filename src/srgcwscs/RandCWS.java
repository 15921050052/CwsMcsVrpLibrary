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
import java.util.Collections;

/**
* @class RandCWS srgcwscs.RandCWS 
*
* @brief This class encapsulates the Randomized Generalized Clarke & Wright Savings
* heuristic for the extended CVRP. The RGCWS algorithm introduces randomness in
* the edge-selection process of the Generalized CWS heuristic.
* 
* @authors Angel A. Juan, Juan Carlos Cruz, Marcos Fernandez Callejo
*
* @package srgcwscs;
* 
* @date 050113
*
* @copyright GNU Public License, version 2.
**/
public class RandCWS
{
	/**
     * @brief Inputs a representation of the different components of the problem, nodes, edges and vehicles
     */
    private Inputs inputs;
    
    /**
     * @brief Set of characteristics for the test to be run. InstanceName, Constraints, Maximiun Computing time,...
     */
    private Test aTest;
    
    /**
     * @brief Number of edges
     */
    private int nEdges;
    
    /**
     * @brief Randomness instance
     */
    private Randomness random;
    
    /**
     * @brief Array for randomize savings list
     */
    private int[] positions; // array of randomly selected positions 
    
    /**
     * @brief Array of nodes
     */
    private Node[] nodes;
    
    /**
     * @brief Vehicle Capacity
     */
    private int vCap;
    
    /**
	* @brief RandCWS Constructor
	*
	* @param test - Test set of characteristics for the test to be run. InstanceName, Constraints, Maximiun Computing time,...
	* 
	* @param varInputs - Inputs a representation of the different components of the problem, nodes, edges and vehicles
	* 
	* @param nodeDemand - float value for the associated node's demand
	* 
	*/
    public RandCWS(Test test, Inputs varInputs)
    {
        inputs = varInputs;
        aTest = test;
        int n = inputs.getNodeList().length;
        vCap = inputs.getVehicle(0).getVcap();

        nEdges = (n - 1) * (n - 2) / 2; // The depot is not considered
        random = new Randomness(aTest, inputs);
        positions = new int[nEdges];
        nodes = inputs.getNodeList();  
    }

    /**
	* @brief Core method to solve the VRP according to a given characteristics, with a set of elements and returning a collection of routes
	*
	* @param effList - Edge[] array of edges connecting nodes
	* 
	* @param useRandomSelection - use Random selection for solving VRP
	* 
	* @return Solution - Set of nodes that resolves the VRP problem
	* 
	*/
    public Solution solve(Edge[] effList, boolean useRandomSelection)
    {  
        // Depot is always node 0 with (x,y) = (0,0) and demand = 0
        Node depot = nodes[0];

        // 1. CONSTRUCT THE CWS INITIAL (DUMMY) SOLUTION
        Solution currentSol = getDummySolution(nodes);

        // 2. PERFORM THE EDGE-SELECTION & ROUTING-MERGING ITERATIVE PROCESS
        // This process is a randomization of the corresponding CWS process
        // Calculate the array of randomly selected positions of edges in effList
        if (useRandomSelection == false) // classical Clarke & Wright solution
        {
            for (int i = 0; i < nEdges; i++)
            {
            	positions[i] = i; 
            }
        }
        else
        {
        	positions = random.calcPositionsArrayFast();// Randomized Clarke & Wright solution
        }
        
        this.edgeSelectionRoutingMerging(depot,positions, effList,aTest, currentSol);

        /***************************************************************************
         * 4. SET THE SOLUTION COSTS AND RETURN IT
         ***************************************************************************/
        // 4.1. Reset the solution costs
        currentSol.setCosts(0);

        // 4.2. Calculate solutions costs
        for (int i = 0; i < currentSol.getRoutes().size(); i++) {
            currentSol.addCosts(currentSol.getRoutes().get(i));
        }

        return currentSol;
    }
    
    /**
	* @brief Constructs the CWS initial dummy Solution 
	*
	* @param aNode - Node [] array of nodes involved in the problem
	* 
	* @param Solution - Set of routes constructed, one route for each node depot combination
	*
	* @see Solution solve(Edge[] effList, boolean useRandomSelection)
	*/
    public Solution getDummySolution(Node [] nodeSet) 
    {
    	Solution dummySolution = new Solution();

    	Node depot = nodeSet[0];
    	
    	for( int i = 1; i < nodeSet.length; i++ ) // don't consider i = 0 (depot)
    	{
    		// 1.1. Create diEdge and idEdge (and set corresponding costs)
    		Node iNode = nodeSet[i];
    		Edge diEdge = new Edge(depot, iNode);
    		Edge idEdge = new Edge(iNode, depot);

    		// 1.2. Create didRoute (and set corresponding total costs and demand)
    		Route didRoute = new Route();
    		didRoute.addEdge(diEdge);
    		didRoute.addDemand(diEdge);
    		didRoute.addCosts(diEdge);
    		didRoute.addEdge(idEdge);
    		didRoute.addCosts(idEdge);
        
    		// 1.3. Update iNode properties
    		iNode.setInRoute(didRoute.getId()); // save route to which node belongs
    		iNode.setIsInterior(false); // node is directly connected to depot
    		iNode.setRoute(didRoute);
    		
    		// 1.4. Add didRoute to current solution
    		dummySolution.addRoute(didRoute);
    		dummySolution.addCosts(didRoute);
    		
    	}
    	return dummySolution;
    }
    
    /**
	* @brief Performs the edge selection and the route merging 
	*
	* @param depot - Node Reference to the depot node
	* 
	* @param positions - int [] array of edge position, guides the order of edge selection
	*
	* @param effList - Edge [] array of edges 
	*
	* @param test - Test set of characteristics for the test to be run. InstanceName, Constraints, Maximiun Computing time,...
	* 
	* @param aSolution - Solution set of routes resulting from the performing
	* 
	* @see Solution solve(Edge[] effList, boolean useRandomSelection)
	*/
    public void edgeSelectionRoutingMerging(Node depot,int [] positions, Edge[] effList,Test test, Solution aSolution) 
    {
    	for( int i = positions.length - 1; i > 0; i-- )
    	{
    		//System.out.println(effList[positions[i]].getSavings());

    		// 3.1. Select the next edge from the list (either at random or not)
    		Edge ijEdge = effList[positions[i]];

    		// 3.2. Determine the nodes i < j that define the edge
    		Node iNode = ijEdge.getOrigin();
    		Node jNode = ijEdge.getEnd();
 
    		// 3.3. Determine the routes associated to each node
    		//Route iR = getRoute(iNode, aSolution);
    		//Route jR = getRoute(jNode, aSolution);
    		Route iR = iNode.getRoute();
    		Route jR = jNode.getRoute();

    		// 3.4. If all necessary conditions are satisfied, apply merging process
    		// boolean isMergingPossible = checkMergingConditions(iR, jR, ijEdge, aTest);
    		// 3.4. If all necessary conditions are satisfied, apply merging process with diferents vehicles
    		if (checkMergingConditions(iR, jR, ijEdge, test)) 
    		{
    			aSolution.substractCosts(iR);
    			aSolution.substractCosts(jR);

    			// 3.4.1. Get an edge iE in iR containing nodes i and 0
    			Edge iE = getEdgeDepotNode(iR, iNode);// iE is either (0,i) or (i,0)
    			// 3.4.2. Get an edge jE in jR containing nodes j and 0
    			Edge jE = getEdgeDepotNode(jR, jNode); // jE is either (0,j) or (j,0)

    			// 3.4.3. Remove edge iE from iR route 
    			iR.getEdges().remove(iE);
    			iR.substractCosts(iE);
    			
    			// 3.4.4. Remove edge jE from jR route
    			jR.getEdges().remove(jE);
    			jR.substractCosts(jE);

    			// 3.4.5. If there are more than one edge then i will be interior
    			if (iR.getEdges().size() > 1) 
    			{
    				iNode.setIsInterior(true);
    			}
    			
    			// 3.4.6. If there are more than one edge then j will be interior
    			if (jR.getEdges().size() > 1) 
    			{
    				jNode.setIsInterior(true);
    			}
    			
    			//3.4.7 Control routes and edges in order to merge it properly
    			//mergingManager(iR,jR,ijEdge,aSolution);
    			if(iR.getEdges().size() > jR.getEdges().size())
    			{
    				mergingManager(iR,jR,ijEdge,aSolution);
    			}
    			else
    			{
    				
    				Edge ijNewEdge = new Edge (ijEdge);
    				ijNewEdge.reverse();
    				mergingManager(jR,iR,ijNewEdge,aSolution);
    			}
    			
    			//System.out.println(currentSol.getCosts());

    		}

    	}
    	
    	
    }
    
    /**
	* @brief Gets the edge directly connected to the depot
	*
	* @remarks Return null in no edges was found for the given nodes
	* 
	* @param aRoute - Route Representation of a route formed by a set of edges starting and and ending at the depot
	* 
	* @param externalNode - Node connected to the depot
	*
	* @return Edge - Edge connecting the two nodes. Null if any edge was found for the two nodes.
	*  
	* @see void edgeSelectionRoutingMerging(Node depot,int [] positions, Edge[] effList,Test test, Solution aSolution)
	*/
    public Edge getEdgeDepotNode(Route aRoute, Node externalNode) 
    {
        Edge noEdge = null;
        
        //1. Try to get first edge
        Edge depotStart = aRoute.getEdges().get(0);
        int idEnd = depotStart.getEnd().getId(); 
        
        if(idEnd == externalNode.getId())
        {
        	return depotStart;
        }
        
        //2. Try to get last edge
        Edge endDepot = aRoute.getEdges().get(aRoute.getEdges().size() -1);
        int idStart = endDepot.getOrigin().getId(); 
        
        if(idStart == externalNode.getId())
        {
        	return endDepot;
        }

        // If no edge has been found, send a warning message
        System.out.println("Error in getEdgeDepotNode() method: no edge was found");
        return noEdge;
    }

    
    /**
	* @brief Reverse the route direction, reversing all edges in route
	* 
	* @remarks Given aRoute (e.g. 0 -> 2 -> 6 -> 0), reverses the order of its nodes (e.g. 0 -> 6 -> 2 -> 0)
	* 
	* @param aRoute - Route Representation of a route formed by a set of edges starting and and ending at the depot
	* 
	* @see void edgeSelectionRoutingMerging(Node depot,int [] positions, Edge[] effList,Test test, Solution aSolution)
	*/
    public void reverseRoute(Route aRoute) 
    {
    	Route auxRoute = new Route();
    	
    	// For each ijEdge in aRoute, reverse the edge
    	for (int i = aRoute.getEdges().size() - 1; i >= 0; i--) 
    	{
    		Edge newEdge = new Edge (aRoute.getEdges().get(i));
    		newEdge.reverse();
    		auxRoute.addEdge(newEdge);
        }
        
        aRoute.setEdges(auxRoute.getEdges(), auxRoute.getNodes());
    }

    /**
   	* @brief Coordinates the merging process, reduces the timing used for coordinates routes and new edge direction
   	*  
   	* @remarks the edge and small route will have the same direction as the biggest route. 
   	* 
   	* @param bigRoute - Route big one route among two routes
   	* 
   	* @param smallRoute - Small one route among two routes
   	*
   	* @param newEdge - Edge connecting the new routes to be merged
   	* 
   	* @param aSolution - Solution new set of routes to be updated
   	* 
   	* @see void edgeSelectionRoutingMerging(Node depot,int [] positions, Edge[] effList,Test test, Solution aSolution) 
   	*/
    public void mergingManager(Route bigRoute,Route smallRoute,Edge newEdge,Solution aSolution)
    {
    	// 1. If new big route does not start at 0
		if (bigRoute.getEdges().get(0).getOrigin().getId() != 0) 
		{
			//1.1.1 newEdge is reversed in order to have the same direction of the big route
			Edge reverseNewEdge = new Edge (newEdge);
			reverseNewEdge.reverse();
			
			//1.1.2 If new small route does start at 0 it must be reversed to have the same direction like the big route
			if (smallRoute.getEdges().get(0).getOrigin().getId() != 0) 
			{
				reverseRoute(smallRoute);
			}
			
			//1.1.3 Merge Routes
			mergeRoutes(smallRoute, bigRoute, reverseNewEdge,  aSolution);
	    }
		else
		{
			// 1.2.1 If new small route does start at 0 it must be reversed to have the same direction like the big one
			if (smallRoute.getEdges().get(0).getOrigin().getId() == 0) 
			{
				reverseRoute(smallRoute);
			}
			
			//1.2.2 Merge Routes
			mergeRoutes(bigRoute, smallRoute, newEdge,  aSolution);
		}
    }
    
    
    /**
	* @brief Merge to given routes in a solution through an edge
	*  
	* @param finalRoute - Final route where auxRoute will be added
	* 
	* @param auxRoute - Auxiliary route to be merged in final route
	* 
	* @param edge - Edge Bridge to connect the two routes
	*
	* @param aSolution - Solution where the routes are assigned
	* 
	* @see void edgeSelectionRoutingMerging(Node depot,int [] positions, Edge[] effList,Test test, Solution aSolution) 
	*/
    public void mergeRoutes(Route finalRoute, Route auxRoute, Edge edge, Solution aSolution)
    {
    	//1. Add edge
    	finalRoute.addEdge(edge);
    	finalRoute.addDemand(edge);
    	finalRoute.addCosts(edge);
    	edge.getOrigin().setInRoute(finalRoute.getId());
    	edge.getOrigin().setRoute(finalRoute);
    	
    	//2. Merge Routes
    	for (int k = 0; k < auxRoute.getEdges().size(); k++) 
    	{
    		Edge kEdge = auxRoute.getEdges().get(k);
    		finalRoute.addEdge(kEdge);
    		finalRoute.addDemand(kEdge);
    		finalRoute.addCosts(kEdge);
    		kEdge.getEnd().setInRoute(finalRoute.getId());
    		//Marcos added
    		kEdge.getOrigin().setInRoute(finalRoute.getId());
    		
    		kEdge.getEnd().setRoute(finalRoute);
    		kEdge.getOrigin().setRoute(finalRoute);
    	}
    
    	//3. Delete route jR from iterSolution
    	aSolution.getRoutes().remove(auxRoute);
    	aSolution.addCosts(finalRoute);
    	
    }
	

    /**
	* @brief Check if it is feasible to merge two given routes
	* 
	* @remarks Controls that any constraint is violated 
	* (Routes are not the same, Both nodes are exterior in their respective nodes, demand after merging can be covered by a single node)
	* 
	* @param iR - Route First route to be merged
	* 
	* @param jR - Route second route to be merged
	* 
	* @param ijEdge - Edge which connects two nodes
	*
	* @param aTest - Test Characteristics of the test being run
	* 
	* @return boolean - True if the two routes can be merged without breaking any condition
	*
	* @see void edgeSelectionRoutingMerging(Node depot,int [] positions, Edge[] effList,Test test, Solution aSolution) 
	*/
    public boolean checkMergingConditions(Route iR, Route jR, Edge ijEdge,Test atest) 
    {
    	// Condition 1: iR and jR are not the same route
        if( iR.getId() == jR.getId() )
        {
        	return false;
        }
    	
        // Condition 2: both nodes are exterior nodes in their respective routes
        if( ijEdge.getOrigin().isInterior() == true || ijEdge.getEnd().isInterior() == true )
        {
        	return false;
        }
        
        // Condition 3: demand after merging can be covered by a single vehicle
        if (iR.getDemand() + jR.getDemand() > inputs.getVehicle(0).getVcap())
        {
        	return false;
        }
    	
        // Condition 4: total costs (distance) after merging are feasible
        int nodesInIR = iR.getNodes().size();
        int nodesInJR = jR.getNodes().size();
        if ( (iR.getCosts() + jR.getCosts() - ijEdge.getSavings()) > (atest.getMaxRouteCost() - atest.getServiceCosts() * (nodesInIR + nodesInJR - 2)))
        {
        	return false;
        }

        return true;
    }

    
}
