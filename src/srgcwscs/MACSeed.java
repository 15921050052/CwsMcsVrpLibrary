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

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * @class  MACSeed srgcwscs.MACSeed
 * 
 * @brief This class is being useful for getting the MAC address number as a seed for the
 * random number generation
 * 
 * @authors Juan Carlos, Marcos Fernandez
 * 
 * @package srgcwscs;
 * 
 * @date 020113
 * 
 * @copyright GNU Public License, version 2.
 */
public class MACSeed {

    private int seedOfMAC;

    public MACSeed() {
    }

    public int getSeedOfMAC() {

        String OS = System.getProperty("os.name");
        try {
            if (OS.startsWith("Windows")) {
                String mac = GetMACWin();
                seedOfMAC = (int) GenarateRandSeed(mac);
            } else if (OS.startsWith("Linux")) {
                System.out.println("L");
            } else {
                System.out.println("Sistema operativo desconocido: " + OS);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return seedOfMAC;


    }

    private String GetMACWin() {
        String mac = "";
        try {
            Process p = Runtime.getRuntime().exec("getmac");
            InputStream IS = new BufferedInputStream(p.getInputStream());
            Scanner scan = new Scanner(IS);
            while (scan.hasNext()) {
                if (scan.nextLine().startsWith("=")) {
                    mac = scan.nextLine().substring(0, 17);
                }
            }

        } catch (Exception e) {
        }

        return mac;
    }


    private final static String GetMACLin() {
        String outputText=null;
        try {
              Process p = Runtime.getRuntime().exec("ifconfig");
        InputStream stdoutStream = new BufferedInputStream(p.getInputStream());

        StringBuffer buffer = new StringBuffer();
        for (;;) {
            int c = stdoutStream.read();
            if (c == -1) {
                break;
            }
            buffer.append((char) c);
        }
         outputText = buffer.toString();

        stdoutStream.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
       return outputText;
    }

    private double GenarateRandSeed(String mac) {
        StringTokenizer token = new StringTokenizer(mac, "-");
        // Pass the two   StringTokenizer to firts numbers of MAC because are the builder code
        token.nextToken();
        token.nextToken();
        String Smac1 = token.nextToken();
        double Dmac1 = onlyNumbers(Smac1);
        String Smac2 = token.nextToken();
        double Dmac2 = onlyNumbers(Smac2);
        Random rand = new Random();
        double seed = rand.nextInt(1000) * Dmac1 / Dmac2;
        return seed;
    }

    private double onlyNumbers(String mac) {

        String newMac = "";
        if ((mac.charAt(0) < '0') || (mac.charAt(0) > '9')) {
            newMac = newMac + "1";
        } else {
            newMac = newMac + mac.charAt(0);
        }
        if ((mac.charAt(1) < '0') || (mac.charAt(1) > '9')) {
            newMac = newMac + "1";
        } else {
            newMac = newMac + mac.charAt(1);
        }
        double d = Double.parseDouble(newMac);
        return d;
    }
}
