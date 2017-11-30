/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnet;

/**
 *
 * @author leoed
 */
public class Net {
    
    //Array of the neural net's layers
    private Layer[] layers;
    //Double to represent error used in back propagation
    private double error;
    
    //Neural net's constructor
    //(initialises it with random link weights)
    public Net(int[] topology){
        int numberOfLayers = topology.length;
        layers = new Layer[numberOfLayers];
        int numberOfOutputs;
        for(int x = 0; x < numberOfLayers; x++){
            if(x == (numberOfLayers - 1))
                numberOfOutputs = 0;
            else
                numberOfOutputs = topology[x + 1];
            layers[x] = new Layer(topology[x] + 1, numberOfOutputs);
            Neuron[] thisLayersNeurons = layers[x].getNeurons();
            thisLayersNeurons[thisLayersNeurons.length - 1].setOutputVal(1.0);
        }
    }
    
    //Feeds given inputs through the neural net to produce vaules in the output layer
    public void feedForward(double[] inputVals){
        Neuron[] inputNeurons = layers[0].getNeurons();
        
        if(inputVals.length == inputNeurons.length - 1){
            for(int x = 0; x < inputVals.length; x++){
                inputNeurons[x].setOutputVal(inputVals[x]);
            }
            
            for(int x = 1; x < layers.length; x++){
                Neuron[] currentLayersNeurons = layers[x].getNeurons();
                Layer previousLayer = layers[x - 1];
                for(int y = 0; y < currentLayersNeurons.length - 1; y++){
                    currentLayersNeurons[y].feedForward(previousLayer);
                }
            }
        }
        else
            System.out.println("Error: Number of input values is different to number of input neurons");
    }
    
    //Uses target values to calculate error and backpropagate it to change weights
    public void backProp(double[] targetVals){
        Neuron[] outputLayerNeurons = layers[layers.length - 1].getNeurons();
        error = 0;
        
        for(int x = 0; x < outputLayerNeurons.length - 1; x++){
            double delta = targetVals[x] - outputLayerNeurons[x].getOutputVal();
            error += delta * delta;
        }
        
        error /= outputLayerNeurons.length - 1;
        error = Math.sqrt(error);
        
        for(int x = 0; x < outputLayerNeurons.length - 1; x++){
            outputLayerNeurons[x].calcOutputGradients(targetVals[x]);
        }
        
        for(int x = layers.length - 2; x > 0; x--){
            Layer hiddenLayer = layers[x];
            Layer nextLayer = layers[x + 1];
            
            Neuron[] hiddenLayersNeurons = hiddenLayer.getNeurons();
            for(int y = 0; y < hiddenLayersNeurons.length; y++)
                hiddenLayersNeurons[y].calcHiddenGradients(nextLayer);
        }
        
        for(int x = layers.length - 1; x > 0; x--){
            Neuron[] thisLayersNeurons = layers[x].getNeurons();
            Layer previousLayer = layers[x - 1];
            
            for(int y = 0; y < thisLayersNeurons.length - 1; y++){
                thisLayersNeurons[y].updateInputWeights(previousLayer);
            }
        }
    }
    
    //Assigns given pointer with array of outputs from the neurons in the output layer
    public void getResults(double[] resultVals){
        Neuron[] outputLayersNeurons = layers[layers.length - 1].getNeurons();
        
        for(int x = 0; x < outputLayersNeurons.length - 1; x++){
            resultVals[x] = outputLayersNeurons[x].getOutputVal();
        }
    }
}
