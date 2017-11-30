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
public class Neuron {
    
    //This neurons output value
    private double outputVal;
    //An array of links that contain the weight between this neuron and the neuron(s) int the next layer
    private Link[] outputWeights;
    //This neurons index in its layer
    //used for finding its connection weight to neurons in previous layer
    private int myIndex;
    //Used for calculating weight changes during back propagation
    private double gradient;
    
    //Constructor for neuron
    public Neuron(int numOutputs, int myIndex){
        outputWeights = new Link[numOutputs];
        
        for(int x = 0; x < numOutputs; x++){
            outputWeights[x] = new Link();
        }
        this.myIndex = myIndex;
    }

    //Sets a new output value based on output values of previous layer and their weights
    public void feedForward(Layer previousLayer){
        double sumOfInputs = 0;
        Neuron[] previousLayersNeurons = previousLayer.getNeurons();
        for(int x = 0; x < previousLayersNeurons.length; x++){
            double outVal = previousLayersNeurons[x].getOutputVal();
            double prevW = previousLayersNeurons[x].getOutputWeights()[myIndex].getWeight();
            sumOfInputs += outVal * prevW;
        }
        
        setOutputVal(transferFunction(sumOfInputs));
    }
    
    //Used for calculating the output value
    private static double transferFunction(double input){
        return Math.tanh(input);
    }
    
    //Approximates the derivitave of the normal transfer function
    //used for calculating gradients
    private static double transferFunctionDerivative(double input){
        return 1.0 - (input * input);
    }
    
    //Calculates gradient for output layer neurons
    public void calcOutputGradients(double targetVal){
        double delta = targetVal - outputVal;
        gradient = delta * transferFunctionDerivative(outputVal);
    }
    
    //Calculates gradient for hidden layer neurons
    public void calcHiddenGradients(Layer nextLayer){
        double dow = sumDow(nextLayer);
        double temp = transferFunctionDerivative(outputVal);
        gradient = dow * temp;
    }
    
    //Sums the derivatives of the weights of the next layer
    double sumDow(Layer nextLayer){
        double sum = 0;
        Neuron[] nextLayersNeurons = nextLayer.getNeurons();
        
        for(int x = 0; x < nextLayersNeurons.length - 1; x++){
            sum+= outputWeights[x].getWeight() * nextLayersNeurons[x].getGradient();
        }
        
        return sum;
    }
    
    //Updates the weights in the links from previous layer's neurons to this neuron
    //used during back propagation
    public void updateInputWeights(Layer previousLayer){
        Neuron[] previousLayersNeurons = previousLayer.getNeurons();
        
        for(int x = 0; x < previousLayersNeurons.length; x++){
            Neuron neuron = previousLayersNeurons[x];
            Link[] cons = neuron.getOutputWeights();
            double oldDeltaWeight = cons[myIndex].getDeltaWeight();
            
            double newDeltaWeight = 
                    NeuralNet.LEARNING_RATE *
                    neuron.getOutputVal() *
                    gradient +
                    NeuralNet.ALPHA *
                    oldDeltaWeight;
            
            neuron.getOutputWeights()[myIndex].setDeltaWeight(newDeltaWeight);
            neuron.getOutputWeights()[myIndex].setWeight(neuron.getOutputWeights()[myIndex].getWeight() + newDeltaWeight);
        }
    }

    public double getGradient() {
        return gradient;
    }
    
    public double getOutputVal() {
        return outputVal;
    }

    public void setOutputVal(double outputVal) {
        this.outputVal = outputVal;
    }

    public Link[] getOutputWeights() {
        return outputWeights;
    }

    public void setOutputWeights(Link[] outputWeights) {
        this.outputWeights = outputWeights;
    }
}
