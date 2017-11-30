package neuralnet;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NeuralNet {
    
    //Filename of the first data set
    public static final String TYPE1_FILENAME = "data1.txt";
    //Filename of the second data set
    public static final String TYPE2_FILENAME = "data2.txt";
    //Filename of the third data set
    public static final String TYPE3_FILENAME = "data3.txt";
    //Filename of file to write test results to
    public static final String RESULTS_FILENAME = "results.txt";
    
    //Learning rate used to calculate link weight changes
    public static double LEARNING_RATE = 0.1;
    //Alpha used to calculate link weight changes
    public static double ALPHA = 0.19;
    
    //Topology of the neural net
    //(Number of elements = number of layers)
    //(Value of element = number of neurons in corresponding layer)
    public static int[] NET_TOPOLOGY = {5,5,5,1};
    
    //Number of elements from the total data set to use in training
    public static int TRAINING_SET_SIZE = 31;
    
    //Number of epocs for training
    public static int PASSES = 20000;
    //Number of times the network is trained (for averaging)
    public static final int META_PASSES = 100;
    //Number of times the whole proccess is run (for testing changes in parameters)
    public static final int META_META_PASSES = 1;
    
    //If true, the output from all inputs tried during training are printed to console
    public static final boolean PRINT_TRAINING = false;
    //If true, the output from all inputs tried during testing are printed to console
    public static final boolean PRINT_TESTING = false;
    //If true, the final results of each run of the neural net are printed to console
    public static final boolean PRINT_RESULTS = false;
    
    //If true, the training set is always taken from the start of the full data set
    //If false, the training set is selected randomly from the full data set
    public static boolean ALWAYS_SAME_TRAINING_SET = false;
    
    //Which data set to use (either 1, 2, or 3)
    public static final int TYPE = 1;
    
    //Used to generate random values throughout the nerual network
    public static Random rand;
    
    public static void main(String[] args) {
        rand = new Random(System.currentTimeMillis());
        
        //Creates file to write results to
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(RESULTS_FILENAME, "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(NeuralNet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Outermost loop for changing parameters
        for(int outerOuterCounter = 0; outerOuterCounter < META_META_PASSES; outerOuterCounter++){
            
            //Lists for recording results for later averaging
            ArrayList<Double> error = new ArrayList<>();
            ArrayList<Double> correctPercentTotal = new ArrayList<>();
            long startTime = System.currentTimeMillis();
            
            //Middle loop for averaging many different runs
            for(int outerCounter = 0; outerCounter < META_PASSES; outerCounter++){

                //Lists for storing the data items
                ArrayList<Type> allData = new ArrayList<>();
                ArrayList<Type> trainingSet = new ArrayList<>();
                ArrayList<Type> testSet;
                
                //Get items from file
                try{
                    if(TYPE == 1)
                        allData = FileParser.type1Parser(TYPE1_FILENAME);
                    else if(TYPE == 2)
                        allData = FileParser.type2Parser(TYPE2_FILENAME);
                    else
                        allData = FileParser.type3Parser(TYPE3_FILENAME);
                }
                catch(Exception E){
                    System.out.println("Error: Couldn't find file");
                }
                
                //Splits data into training set and testing set
                for(int x = 0; x < TRAINING_SET_SIZE; x++)
                    if(ALWAYS_SAME_TRAINING_SET)
                        trainingSet.add(allData.remove(0));
                    else
                        trainingSet.add(allData.remove(rand.nextInt(allData.size())));
                testSet = allData;

                //Initialises neural net
                Net neuralNet = new Net(NET_TOPOLOGY);
                
                //For keeping track of epoc
                int pass = 1;

                //Training loop
                while(pass <= PASSES){
                    if(PRINT_TRAINING) System.out.println("Pass " + pass);
                    
                    //Chooses random data item from the training set
                    Type next = trainingSet.get(rand.nextInt(trainingSet.size()));

                    double[] inputValues = next.getInputs();

                    if(PRINT_TRAINING){
                        System.out.print("Inputs: ");
                        for(double d : inputValues)
                            System.out.printf("%f  ", d);
                        System.out.printf("\n");
                    }
                    
                    //Feeds inputs from data item into the neural net
                    neuralNet.feedForward(inputValues);
                    
                    //Gets results from the neural net
                    double[] results = new double[NET_TOPOLOGY[NET_TOPOLOGY.length - 1]];
                    neuralNet.getResults(results);
                    if(PRINT_TRAINING){
                        System.out.print("Outputs: ");
                        for(double d : results)
                            System.out.printf("%f  ", d);
                        System.out.printf("\n");
                    }

                    double[] targets = next.getOutputs();
                    if(PRINT_TRAINING){
                        System.out.print("Target: ");
                        for(double d : targets)
                            System.out.printf("%f  ", d);
                        System.out.printf("\n------------------------\n");
                    }
                    
                    //Uses the real output of the data item to backpropogate into the nerual net and change weights
                    neuralNet.backProp(targets);

                    pass++;
                }

                if(PRINT_RESULTS) System.out.printf("\n\n\n--------------------------------------------RESULTS %d----------------------------------------------------------\n\n\n", outerCounter);

                double errorSum = 0;
                double correctCount = 0;
                
                //Testing loop
                for(Type t : testSet){

                    double[] inputValues = t.getInputs();

                    if(PRINT_TESTING){
                        System.out.print("Inputs: ");
                        for(double d : inputValues)
                            System.out.printf("%f  ", d);
                        System.out.printf("\n");
                    }
                    
                    //Feeds a test data item into the trained neural net
                    neuralNet.feedForward(inputValues);

                    double[] results = new double[NET_TOPOLOGY[NET_TOPOLOGY.length - 1]];
                    //Gets the trained neural net's results
                    neuralNet.getResults(results);

                    if(PRINT_TESTING){
                        System.out.print("Outputs: ");
                        for(double d : results)
                            System.out.printf("%.10f  ", d);
                        System.out.printf("\n");
                    }

                    double[] targets = t.getOutputs();

                    if(PRINT_TESTING){
                        System.out.print("Target: ");
                        for(double d : targets)
                            System.out.printf("%f  ", d);
                        System.out.printf("\n------------------------\n");
                    }
                    
                    //Tests the results against the targets and calculates fitness
                    for(int x = 0; x < results.length; x++){
                        errorSum += Math.abs(results[x] - targets[x]);
                        int crispValue = 1;
                        if(Math.abs(results[x] - 1) > Math.abs(results[x] - 0))
                            crispValue = 0;
                        if(crispValue == (int)targets[x])
                            correctCount++;
                    }
                }
                
                //Averages fitness of all test set results
                errorSum /= (NET_TOPOLOGY[NET_TOPOLOGY.length - 1] * testSet.size());
                double correctPercent = (double)correctCount / (double)(NET_TOPOLOGY[NET_TOPOLOGY.length - 1] * testSet.size());
                if(PRINT_RESULTS){
                    System.out.printf("Average error: %f\n", errorSum);
                    System.out.printf("Correct count: %d\n", (int)correctCount);
                    System.out.printf("Percentage correct: %%%f", correctPercent * 100);
                }
                error.add(errorSum);
                correctPercentTotal.add(correctPercent);
            }
            //Averages results of all neural net runs
            long endTime = System.currentTimeMillis();
            long timeTaken = endTime - startTime;
            double totalError = 0;
            for(double d : error)
                totalError += d;
            totalError /= error.size();
            System.out.printf("\n\n\n--------------------------------------------FINAL RESULTS--------------------------------------------------------\n\n\n");
            System.out.printf("Total average error: %f\n", totalError);
            double totalPercent = 0;
            for(double d : correctPercentTotal)
                totalPercent += d;
            totalPercent /= correctPercentTotal.size();
            System.out.printf("Total average percent correct: %%%f\n", totalPercent * 100);
            System.out.printf("Milliseconds taken: %d\n", timeTaken);
            System.out.printf("%s", extraToPrint());
            //Writes results to file
            if(writer != null) writer.printf("%f\t%f\t%d\n", totalError, totalPercent, timeTaken);
            
            
            
        }
        if(writer != null) writer.close();
    }
    
    //Extra information to print for testing certain parameter changes
    private static String extraToPrint(){
        String extra = "";
        extra += "TRAINING_SET_SIZE: " + String.format("%d", TRAINING_SET_SIZE) + "\n";
        return extra;
    }
}
