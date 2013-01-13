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
import java.util.LinkedList;
import java.util.Random;
import umontreal.iro.lecuyer.rng.LFSR113;
import umontreal.iro.lecuyer.rng.RandomStream;

/**
* @class Randomness srgcwscs.Randomness
*
* @brief This class manages the random behavior (Monte Carlo Simulation) of the library.  
* 
* @author Angel A. Juan, Marcos Fernandez
*
* @package srgcwscs;
* 
* @date 020113
*
* @copyright GNU Public License, version 2.
**/
public class Randomness 
{

	/**
     * @brief Test set of characteristics for the test to be run. InstanceName, Constraints, Maximiun Computing time,...
     */
    private Test aTest;
    
    /**
     * @brief Inputs a representation of the different components of the problem, nodes, edges and vehicles
     */
    private Inputs inputs;
    
    /**
     * @brief beta Coefficient value used for geometric distribution
     */
    private double beta;
    
    /**
     * @brief nEdges total number of edges
     */
    private int nEdges;
    
    /**
     * @brief rng RandomStream object manager from lecuyer random api
     */
    private RandomStream rng;
    
    /**
     * @brief rng Random object manager from java util api
     */
    private Random rngJava;

    /**
	* @brief Randomness Constructor
	*
	* @param test - Test set of characteristics for the test to be run. InstanceName, Constraints, Maximum Computing time,...
	* 
	* @param inputData - Inputs a representation of the different components of the problem, nodes, edges and vehicles 
	*/
    public Randomness(Test test, Inputs inputData) 
    {
        aTest = test;
    	inputs = inputData;
        beta = aTest.getBetaMin();
        int n = inputData.getNodeList().length;
        nEdges = (n - 1) * (n - 2) / 2; // The depot is not considered
        rng = initializeRNG();
        
        rngJava = initializeRngJava();
    }

    /**
	* @brief Calculates a new savings edge order according to a randomization bias. It uses Lecuyer library for number generation. 
	* 
	* @param distribuntion - Type of geometric distribution for generating numbers T (Triangular) G (Geometric) U (Uniform)
	* 
	* @return int[] - new array of positions for selecting edges
	*
	* @see calcPositionsArrayFast()
	*/
    public int[] calcPositionsArray(String distribution) 
    {
        int[] posArray = new int[nEdges];
       
        // Select a new beta value (only for Geometric distribution)
        beta = rng.nextDouble() * (aTest.getBetaMax() - aTest.getBetaMin())
                + aTest.getBetaMin();

        // array of "pointers" to jobs in effList
        LinkedList<Integer> auxArray = new LinkedList<Integer>();
        
        for (int i = 0; i < nEdges; i++) 
        {
            auxArray.add(i);
        }
        
        int pos = 0;
        // Assign new random positions
        for (int i = 0; i < nEdges; i++) 
        {
            pos = getRandomPosition(nEdges - i, distribution);
            posArray[i] = auxArray.get(pos);
            auxArray.remove(pos);
        }

        return posArray;
    }

    /**
	* @brief Calculates a new savings edge order according to a randomization bias.It uses Lecuyer library for number generation. 
	* 
	* @param distribuntion - Type of geometric distribution for generating numbers T (Triangular) G (Geometric) U (Uniform)
	* 
	* @param noJob - number of jobs to run the randomization process
	* 
	* @return int[] - new array of positions for selecting edges
	*
	* @see calcPositionsArrayFast()
	*/
    public int[] calcPositionsArray(String distribution, int noJob) 
    {
        int[] posArray = new int[noJob];
        
        // Select a new beta value (only for Geometric distribution)
        beta = rng.nextDouble() * (aTest.getBetaMax() - aTest.getBetaMin())
                + aTest.getBetaMin();

        // array of "pointers" to jobs in effList
        LinkedList<Integer> auxArray = new LinkedList<Integer>();
        
        for (int i = 0; i < noJob; i++) 
        {
            auxArray.add(i);
        }
        
        int pos = 0;
        // Assign new random positions
        for (int i = 0; i < noJob; i++) 
        {
            pos = getRandomPosition(noJob - i, distribution);
            posArray[i] = auxArray.get(pos);
            auxArray.remove(pos);
        }

        return posArray;
    }

    /**
	* @brief Calculates a new savings edge order according to a randomization bias.It uses Lecuyer library for number generation. 
	*
	* @remarks It is strongly recommended to use calcPositionsArrayFast() instead of calcPositionsArray(). It is extremely more faster method
	* 
	* @return int[] - new array of positions for selecting edges
	*
	* @deprecated The performance of this method is really low in comparison to calcPositionsArrayFast() based on LinkedList which is extremely faster.
	*
	* @see calcPositionsArrayFast()
	*/
    public int[] calcPositionsArray() 
    {
        String distribution = aTest.getDistribution();
        int[] posArray = new int[nEdges];
        int[] auxArray = new int[nEdges]; // array of "pointers" to edges in effList

        // Select a new beta value (only for Geometric distribution)
        beta = rng.nextDouble() * (aTest.getBetaMax() - aTest.getBetaMin())
                + aTest.getBetaMin();

        // Reset auxArray
        for (int i = 0; i < nEdges; i++) 
        {
            auxArray[i] = i;
        }
        
        // Assign new random positions
        for (int i = 0; i < nEdges; i++) 
        {
            int pos = getRandomPosition(nEdges - i, distribution);
            posArray[i] = auxArray[pos];
            for (int j = pos; j < nEdges - i - 1; j++) 
            {
                auxArray[j] = auxArray[j + 1];
            }
        }
        return posArray;
    }
    
    /**
   	* @brief Calculates a new savings edge order according to a randomization bias.It uses Lecuyer library for number generation. 
   	*
   	* @remarks This method is faster than calcPositionsArray() it uses remove LinkedList method to speed up shifting process
   	* 
   	* @return int[] - new array of positions for selecting edges
   	*/
   public int[] calcPositionsArrayFast() 
   {
       String distribution = aTest.getDistribution();
       int[] posArray = new int[nEdges];
      

       // Select a new beta value (only for Geometric distribution)
       beta = rng.nextDouble() * (aTest.getBetaMax() - aTest.getBetaMin())
               + aTest.getBetaMin();

       // Reset auxArray
       LinkedList<Integer> auxArray = new LinkedList<Integer>();
       
       for (int i = 0; i < nEdges; i++) 
       {
           auxArray.add(i);
       }
       
       int pos = 0;
       // Assign new random positions
       for (int i = 0; i < nEdges; i++) 
       {
           pos = getRandomPosition(nEdges - i, distribution);
           posArray[i] = auxArray.get(pos);
           auxArray.remove(pos);
       }
       
       return posArray;
   }
   
   /**
  	* @brief Calculates a new savings edge order according to a randomization bias 
  	*
  	* @brief It uses Java Random library instead of Lecuyer library to generate new random values
  	* 
  	* @return int[] - new array of positions for selecting edges
  	*
  	* @see calcPositionsArrayFast()
	*/
   public int[] calcPositionsArrayFastJava() 
   {
      String distribution = aTest.getDistribution();
      int[] posArray = new int[nEdges];
     

      // Select a new beta value (only for Geometric distribution)
      beta = rngJava.nextDouble() * (aTest.getBetaMax() - aTest.getBetaMin())
              + aTest.getBetaMin();

      // Reset auxArray
      LinkedList<Integer> auxArray = new LinkedList<Integer>();
      
      for (int i = 0; i < nEdges; i++) 
      {
          auxArray.add(i);
      }
      
      int pos = 0;
      // Assign new random positions
      for (int i = 0; i < nEdges; i++) 
      {
          pos = getRandomPositionJava(nEdges - i, distribution);
          posArray[i] = auxArray.get(pos);
          auxArray.remove(pos);
      }
      
      return posArray;
   }

    /**
 	* @brief Gets a new number from lecuyer library
 	*
 	* @param a - int minimum range value to return
 	* 
 	* @param b - int maximum range value to return
 	*
 	* @return return new number
 	* 
 	* @see int[] calcPositionsArray() 
 	*/
    public int nextInt(int a, int b) 
    {
        return rng.nextInt(a, b);
    }

    /**
   	* @brief Gets a random position according to a given type of distribution
   	*
   	* @param n - int edge index to find a new position
   	* 
   	* @param dist - Distribution (t or T for Triangular, g or G for Geometric otherwise uniform
   	*
   	* @return random between 0 and n -1
   	* 
   	* @see int[] calcPositionsArray() 
   	*/
    private int getRandomPosition(int n, String dist) // random between 0 and n-1
    {
        int pos = 0;
        char distribution = dist.charAt(0);
        if (distribution == 't' || distribution == 'T') // Triangular
        {
            pos = (int) (n * (1 - Math.sqrt(rng.nextDouble())));
        } 
        else if (distribution == 'g' || distribution == 'G') // Geometric
        {
            pos = (int) (Math.log(rng.nextDouble()) / Math.log(1 - beta));
            pos = pos % n;
        } 
        else // Uniform
        {
            pos = rng.nextInt(0, n - 1);
            //pos = (int) (n * rng.nextDouble());
        }
        return pos;
    }
    
    /**
   	* @brief Gets a random position according to a given type of distribution
   	*
   	* @remarks Get a new random values based on a geometric distribution using Java Library
   	* 
   	* @param n - int edge index to find a new position
   	* 
   	* @param dist - Distribution (t or T for Triangular, g or G for Geometric otherwise uniform)
   	*
   	* @return random between 0 and n -1
   	* 
   	* @see int[] calcPositionsArray() 
   	*/
    private int getRandomPositionJava(int n, String dist) // random between 0 and n-1
    {
        int pos = 0;
        char distribution = dist.charAt(0);
        if (distribution == 't' || distribution == 'T') // Triangular
        {
            pos = (int) (n * (1 - Math.sqrt(rngJava.nextDouble())));
        } 
        else if (distribution == 'g' || distribution == 'G') // Geometric
        {
            pos = (int) (Math.log(rngJava.nextDouble()) / Math.log(1 - beta));
            pos = pos % n;
        } 
        else // Uniform
        {
            pos = rngJava.nextInt(n - 1);
            //pos = (int) (n * rng.nextDouble());
        }
        return pos;
    }

    /**
	* @brief Initialize a RNG based on LFSR113 Lecuyer's SSJ (period 2^113-1)
	*
	* @return  RandomStream - random manager instance 
	*/
    private RandomStream initializeRNG() 
    {
        int seed = Math.max(aTest.getSeed(), 128);
        int seedArray[] = {seed, seed, seed, seed};
        LFSR113.setPackageSeed(seedArray);
        RandomStream stream = new LFSR113();
        return stream;
    }
    
    /**
	* @brief Initialize a random java manager
	*
	* @return  Random - random java manager instance 
	*/
    private Random initializeRngJava()
    {
    	int seed = Math.max(aTest.getSeed(), 128);
    	return new Random(seed); 
    }
}
