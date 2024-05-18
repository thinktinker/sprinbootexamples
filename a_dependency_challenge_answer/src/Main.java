public class Main {
    public static void main(String[] args) {

        // TODO: Modify the following
        // create a utility function to ask for the user's input
        // on the type of shape to calculate and given length or radius

        Circle circle = new Circle(5f);
        Square square = new Square(5f);

        CalculateShape calculateCircle = new CalculateShape(circle);
        CalculateShape calculateSquare = new CalculateShape(square);

        // Calculate Area
        calculateCircle.PrintArea();
        calculateSquare.PrintArea();

        // Calculate Perimeter
        calculateCircle.PrintPerimeter();
        calculateSquare.PrintPerimeter();

    }
}