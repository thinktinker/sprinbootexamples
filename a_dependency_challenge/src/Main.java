public class Main {
    public static void main(String[] args) {

        // TODO: Modify the following
        // Create a utility function that asks for the user's inputs:
        //   1) Ask for the type of shape: <S> for Square or <C> for Circle
        //   2) Ask for the shape length (or radius, if its a circle)
        //   3) Or, press "Q" to end
        // Finally, depending on the selected shape, print out the area and parameter
        // Hint: The use of a loop is required

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