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
public class Layer {
    
    //An array of the neurons in this layer
    private Neuron[] neurons;
    
    //Constructor for layer given the number of neurons in that layer
    //and the number of output needed for each neuron
    public Layer(int numberOfNeurons, int numberOfOutputs){
        neurons = new Neuron[numberOfNeurons];
        for(int x = 0; x < numberOfNeurons; x++)
            neurons[x] = new Neuron(numberOfOutputs, x);
    }

    //Returns neurons from this layer
    public Neuron[] getNeurons() {
        return neurons;
    }
}
