import java.io.File;
import java.util.Arrays;
import java.util.Random;

class NeuralNetwork {
    private Matrix[] weights;
    private Matrix[] biases;
    private int layers;
    private float mutationRate;
    private Map[] activationFunctions;
    private Random random;

    static Map SIGMOID = v -> 1 / (float)(1 + Math.exp(-v));

    NeuralNetwork(int inputNodes, int[] hiddenLayersNodes, int outputNodes, float mutationRate) {
        this.mutationRate = mutationRate;
        this.layers = hiddenLayersNodes.length;

        this.weights = new Matrix[layers + 1];
        this.biases = new Matrix[layers + 1];

        this.random = new Random();

        weights[0] = new Matrix(hiddenLayersNodes[0], inputNodes);
        weights[layers] = new Matrix(outputNodes, hiddenLayersNodes[layers - 1]);
        weights[0].randomize();
        weights[layers].randomize();

        biases[0] = new Matrix(hiddenLayersNodes[0], 1);
        biases[layers] = new Matrix(outputNodes, 1);
        biases[0].randomize();
        biases[layers].randomize();

        for (int i = 1; i < layers; i++) {
            weights[i] = new Matrix(hiddenLayersNodes[i], hiddenLayersNodes[i - 1]);
            biases[i] = new Matrix(hiddenLayersNodes[i], 1);

            weights[i].randomize();
            biases[i].randomize();
        }

        activationFunctions = new Map[this.layers + 1];

        Arrays.fill(activationFunctions, SIGMOID);
    }

    NeuralNetwork(int inputNodes, int[] hiddenLayersNodes, int outputNodes, Map[] activationFunctions, Map[] derivativeActivationFunctions, float mutationRate) {
        if (activationFunctions.length != hiddenLayersNodes.length + 1 || activationFunctions.length != derivativeActivationFunctions.length) {
            System.out.println("ERROR, cannot create neural network");
            return;
        }

        this.mutationRate = mutationRate;
        this.layers = hiddenLayersNodes.length;

        this.weights = new Matrix[layers + 1];
        this.biases = new Matrix[layers + 1];

        weights[0] = new Matrix(hiddenLayersNodes[0], inputNodes);
        weights[layers] = new Matrix(outputNodes, hiddenLayersNodes[layers - 1]);
        weights[0].randomize();
        weights[layers].randomize();

        biases[0] = new Matrix(hiddenLayersNodes[0], 1);
        biases[layers] = new Matrix(outputNodes, 1);
        biases[0].randomize();
        biases[layers].randomize();

        for (int i = 1; i < layers; i++) {
            weights[i] = new Matrix(hiddenLayersNodes[i], hiddenLayersNodes[i - 1]);
            biases[i] = new Matrix(hiddenLayersNodes[i], 1);

            weights[i].randomize();
            biases[i].randomize();
        }

        this.activationFunctions = new Map[this.layers + 1];

        System.arraycopy(activationFunctions, 0, this.activationFunctions, 0, activationFunctions.length);
    }

    NeuralNetwork(String directory, float mutationRate) {
        this.mutationRate = mutationRate;

        File dir = new File(directory);
        File[] list = dir.listFiles();

        if (list != null) {
            int length = list.length;

            this.weights = new Matrix[length / 2];
            this.biases = new Matrix[length / 2];
            this.layers = list.length / 2 - 1;

            for (int i = 0; i < length / 2; i++) {
                biases[i] = new Matrix(dir + "/biases" + i + ".txt");
            }

            for (int i = 0; i < length / 2; i++) {
                weights[i] = new Matrix(dir + "/weights" + i + ".txt");
            }
        }

        activationFunctions = new Map[this.layers + 1];

        Arrays.fill(activationFunctions, SIGMOID);
    }

    NeuralNetwork(NeuralNetwork neuralNetwork) {
        this.biases = new Matrix[neuralNetwork.biases.length];
        for (int i = 0; i < biases.length; i++) {
            biases[i] = new Matrix(neuralNetwork.biases[i]);
        }
        this.weights = new Matrix[neuralNetwork.biases.length];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = new Matrix(neuralNetwork.weights[i]);
        }
        this.layers = neuralNetwork.layers;
        this.mutationRate = neuralNetwork.mutationRate;
        this.activationFunctions = neuralNetwork.activationFunctions;
        this.random = new Random();
    }

    public void mutate() {
        for (Matrix weight : weights) {
            float[][] data = weight.getData();
            for (int r = 0; r < data.length; r++) {
                for (int c = 0; c < data[r].length; c++) {
                    if (random.nextFloat() < mutationRate)
                        data[r][c] += random.nextFloat() - 0.5;
                }
            }
            weight = new Matrix(data);
        }
        for (Matrix bias : biases) {
            float[][] data = bias.getData();
            for (int r = 0; r < data.length; r++) {
                for (int c = 0; c < data[r].length; c++) {
                    if (random.nextFloat() < mutationRate)
                        data[r][c] += random.nextFloat() - 0.5;
                }
            }
            bias = new Matrix(data);
        }
    }

    float[] feedForward(float[] input) {
        Matrix inputs = Matrix.fromArray(input);
        Matrix[] hiddenLayers = new Matrix[weights.length - 1];

        hiddenLayers[0] = Matrix.multiply(weights[0], inputs);
        hiddenLayers[0].add(biases[0]);
        hiddenLayers[0].map(activationFunctions[0]);

        for (int i = 1; i < layers; i++) {
            hiddenLayers[i] = Matrix.multiply(weights[i], hiddenLayers[i - 1]);
            hiddenLayers[i].add(biases[i]);
            hiddenLayers[i].map(activationFunctions[i]);
        }

        Matrix output = Matrix.multiply(weights[layers], hiddenLayers[layers - 1]);
        output.add(biases[layers]);
        output.map(activationFunctions[layers]);

        return output.toArray();
    }

    void printResults(float[] input) {
        System.out.println(Arrays.toString(input) + " : " + Arrays.toString(feedForward(input)));
    }

    void export(String directory) {
        new File(directory).mkdir();
        for (int i = 0; i < weights.length; i++) {
            weights[i].exportToFile(directory + "/weights" + i + ".txt");
        }
        for (int i = 0; i < biases.length; i++) {
            biases[i].exportToFile(directory + "/biases" + i + ".txt");
        }
    }
}