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
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
* @class TestsPlanner  srgcwscs.TestsPlanner
*
* @brief This class creates a list of tests to be run.
* 
* @authors Angel A. Juan, Marcos Fernandez
* 
* @package srgcwscs;
* 
* @date 020113
*
* @copyright GNU Public License, version 2.
**/
public class TestsPlanner 
{

	/**
     * @brief File path for tests
     */
    private String testsFilePath;
    
    /**
     * @brief Array list of test
     */
    private ArrayList<Test> list;

    /**
     * @brief Test Constructor
     * 
     * @param Path to the file of tests to implement
     */
    public TestsPlanner(String path) 
    {
        testsFilePath = path;
        list = new ArrayList<Test>();
    }

    /**
     * @brief Gets a list of test to be run
     * 
     * @return ArrayList<Test> - Array of tets to be run
     */
    public ArrayList<Test> getTestsList() 
    {
        try 
        {
            FileReader reader = new FileReader(testsFilePath);
            Scanner in = new Scanner(reader);
            // The two first lines (lines 0 and 1) of this file are like this:
            //# instance | vCap | maxRouteCosts | serviceCosts | maxTime(sec) | nIterRandCWS | nSols | distribution | betaMin | betaMax | randomGenerator 
            //A-n32-k5     100      10000000          0              120           1000          1          t            0.1      0.2         java          
            while (in.hasNextLine()) 
            {    
             	String s = in.next();
                
                 if (s.charAt(0) != '#') // this is a comment line
                 {                    
                	 String instance = s;  
                     int maxRouteCost = in.nextInt();
                     int serviceCosts = in.nextInt();
                     int maxTime = in.nextInt();
                     int nIterRandCWS = in.nextInt();
                     int nSols = in.nextInt();
                     String distribution = in.next();
                     float min = in.nextFloat();
                     float max = in.nextFloat();
                     //For PCs that haven't put American lenguage as Regional lenguage
//                     float min=Float.parseFloat(in.next());
//                     float max=Float.parseFloat(in.next());
                     String randomGenerator = in.next();
                     boolean useLecuyer = false;
                     if (randomGenerator.equalsIgnoreCase("lecuyer")) 
                     {
                         useLecuyer = true;
                     }                   
                     int seed = in.nextInt();
                     //If seed is 0 , get seed value of MAC address
                     if(seed==0){
                         MACSeed rand= new MACSeed();
                         seed=rand.getSeedOfMAC();                       
                     }
                     Test aTest = new Test(instance, maxRouteCost, serviceCosts, maxTime, nIterRandCWS, nSols, distribution, min, max, useLecuyer, seed);
                     list.add(aTest);
                     in.nextLine();
                 }
                 else
                 {
                 	in.nextLine();
                 }
             }
             in.close();
         } catch (IOException exception) {
             System.out.println("Error processing tests file: " + exception);
         }

        return list;
    }
}
