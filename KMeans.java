import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.LinkedList;
import java.util.Random;


public class KMeans
{
	BufferedReader br;
	FileReader fr;
	
	LinkedList<double[]> inputData;
	LinkedList<double[]> centroids;
	LinkedList<double[]> prevCents; //previous centroids
	LinkedList [] classifiedPts;
	
	String[] tempStrArray;
	double[] tempDoubleArray;

	int dimension,k;
	boolean firstLine;
	Random r;

	public KMeans(String input)
	{
		

		/*read input*/
		
		inputData = new LinkedList<double[]>();
		centroids = new LinkedList<double[]>();
		prevCents = new LinkedList<double[]>();
		firstLine = true;
		
		try
		{

			fr = new FileReader(input);
			br = new BufferedReader(fr);

			String currline;

			br = new BufferedReader(new FileReader(input));
			

			while ((currline = br.readLine()) != null)
			{
				//split into string array
				
				if(firstLine)
				{
					k = Integer.parseInt(currline);
					firstLine = false;
				}
				
				else
				{
					tempStrArray = currline.split(" ");
					tempDoubleArray = new double[tempStrArray.length];
				
					//parse one at a time to double
					for(int i=0;i<tempStrArray.length;i++)
					{
						tempDoubleArray[i] = Double.parseDouble(tempStrArray[i]);
					}
					//take note of input dimension, this will be the dimension for euclidean distance
					dimension = tempDoubleArray.length;
				
					inputData.add(tempDoubleArray);
				}
			}

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			}
			
			catch (IOException ex)
			{

				ex.printStackTrace();

			}

		}
		
		//end file read
		
		//init classifiedPts array of lists
		//that way, index would take note of classification and points are already separated by class
		classifiedPts = new LinkedList[k];
		for(int i = 0;i<k;i++)
		{
			classifiedPts[i] = new LinkedList<double[]>();
		}

		classify();
	}
	
	private void classify()
	{
		/*1. initialize centroids*/
		boolean repeat;
		double [] distCent = new double[k]; //distance from centroids
		double min;

		//if no prev centroids, randomize within points
		if(prevCents.isEmpty())
		{
			r = new Random();



			for(int i = 0;i<k;i++)
			{
				//add random centroids one by one	
				do
				{

					int n = r.nextInt(inputData.size());
					
					//making sure that centroid is not in list
					if(!centroids.contains(inputData.get(n)))
					{
						centroids.add(inputData.get(n));
						repeat = false;
					}
					
					//if it is, repeat
					else
					{
						repeat = true;
					}
				}while(repeat);
			}
		}
		
		/*2. classify centroids until centroids == prev centroids*/
		
		do
		{
			//a. compute distances for all points then classify
			//repeat for every data point
			min = Integer.MAX_VALUE;
			for(int i=0;i<inputData.size();i++)
			{
				//i. compute distances
			
				//repeat computation for centroid distance
				for(int j = 0;j<distCent.length;j++) //i know it's just k, but it's a little readable this way.
				{
					distCent[j] = distance(inputData.get(i), centroids.get(j));
				}
				
				
				//ii. find minimum
				//repeat search for minimum for each point
				for(int j=0; j<distCent.length; j++)
				{
					//get minimum value between current minimum and distCent[j]. Repeat until end. Voila, minimum value!
					min = Math.min(min,distCent[j]);
				}
				
				// iii. classification
				//repeat classification per point
				
				//find where minimum is, given by j when loop breaks. then add input point to class
				for(int j = 0;j<distCent.length;j++)
				{
					if(distCent.length == min)
					{
						classifiedPts[j].add(inputData.get(i));
						break;
					}
				}
			}		
			
			//test
			for(int i =0;i<centroids.size();i++)
			{
				for(int j = 0;j<dimension;j++)
					System.out.print(centroids.get(i)[j]);
				System.out.println();
			}
			
			for(int i=0;i<classifiedPts.length;i++)//per classification
			{
				System.out.println(i + ":");
				for(int j = 0; j< classifiedPts[i].size(); j++)//per point
				{
					
					for(int l = 0; l<classifiedPts[i].get(j).length; l++)//per point element
					{
						System.out.print(classifiedPts[i].get(j)[l] + " "); //IS THIS EVEN RIGHT
					}
					System.out.println();
				}
				System.out.println("----------------");
			}
		}while(false);

	}
	
	//euclidean distance for two double arrays of same dimension
	private double distance(double[] pointA, double[] pointB)
	{
		double tempDist = 0;

		for(int i=0;i<pointA.length;i++)
		{
			tempDist += Math.pow((pointA[i]-pointB[i]),2) ;
		}
		
		return Math.sqrt(tempDist);
	}
}
