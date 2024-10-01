import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // Create a utility function to ask for a shape
        // Square <S>, Circle <C> or Quit <Q>
        // loop (do-while loop)

        char option;
        float dimension = 0;
        Scanner scanner = new Scanner(System.in);

        do{
            System.out.println("Press Square <S>, Circle <C> or Quit <Q>:");                    // Ask for input 'S', 'C' or 'Q'
            option = scanner.next().charAt(0);                                                  // Scan for user's input

            if(Character.toUpperCase(option) == 'S' || Character.toUpperCase(option) == 'C'){   // Check if S or C are selected
                option = Character.toUpperCase(option);                                         // convert option to uppercase
                System.out.println("Enter the dimension:");                                     // Ask for the dimension
                dimension = scanner.nextFloat();
                break;
            }
            else if(Character.toUpperCase(option) == 'Q'){                                      // Check if 'Q' is selected
                System.out.println("\nGood Bye.\n");                                            // End the game
                System.exit(0);                                                                 // Exit the program
                break;
            }
            else{
                System.out.println("\nWrong input. Please try again.\n");                       // Inform user wrong input entered
            }

        }while (Character.toUpperCase(option) != 'Q' );

        if(Character.toUpperCase(option) == 'S'){
            Square square = new Square(dimension);                                              // Instance of Square takes in a float too
            CalculateShape calculateSquare = new CalculateShape(square);
            System.out.print("Area of Square: ");
            calculateSquare.PrintArea();                                                        // Calculate square Area
            System.out.print("Perimeter of Square: ");
            calculateSquare.PrintPerimeter();                                                   // Calculate square Perimeter
        }

        if(Character.toUpperCase(option) == 'C'){
            Circle circle = new Circle(dimension);                                              // Instance of Circle takes in a float
            CalculateShape calculateCircle = new CalculateShape(circle);
            System.out.print("Area of Circle: ");
            calculateCircle.PrintArea();                                                        // Calculate circle Area
            System.out.print("Perimeter of Circle: ");
            calculateCircle.PrintPerimeter();                                                   // Calculate circle Perimeter
        }

        scanner.close();                                                                        // Close the scanner

    }
}
