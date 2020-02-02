import java.util.LinkedList;

public class Population {
    private final int POPULATION;

    private LinkedList<Brain> brains;
    private float[][] input;
    private float[][] output;
    private float mutationRate;

    public Population(int size, float[][] input, float[][] output, float mutationRate) {
        this.POPULATION = size;
        this.brains = new LinkedList<>();
        this.input = input;
        this.output = output;
        this.mutationRate = mutationRate;

        for (int i = 0; i < POPULATION; i++)
            brains.add(new Brain(new NeuralNetwork(2, new int[] {2}, 1, mutationRate)));
    }

    public void test() {
        for (Brain brain : brains) {
            brain.test(input, output);
        }
    }

    public void nextGeneration() {
        for (int i = 0; i < POPULATION; i++) {
            if (POPULATION % 3 == 0)
                brains.get(i).reset();
            else {
                brains.set(i, new Brain(brains.get(i / 3 * 3)));
                brains.get(i).mutate();
            }
        }
//        brains.getFirst().reset();
//        brains.getLast().reset();
//        for (int i = 1; i < POPULATION / 2; i++) {
//            brains.set(i, new Brain(brains.getFirst()));
//            brains.get(i).mutate();
//        }
//        for (int i = POPULATION / 2; i < POPULATION - 1; i++) {
//            brains.set(i, new Brain(brains.getLast()));
//            brains.get(i).mutate();
//        }
    }

    public void sort() {
        brains.sort((b1, b2) -> {
            if (b1.getFitness() < b2.getFitness())
                return -1;
            return 1;
        });
    }

    public void print() {
        for (Brain brain : brains)
            System.out.println(brain.getFitness());
    }

    public Brain getLeader() {
        return brains.getFirst();
    }
}