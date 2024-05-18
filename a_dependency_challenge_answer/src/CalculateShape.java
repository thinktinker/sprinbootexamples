public class CalculateShape {

    Shape shape;

    public CalculateShape(Shape shape){
        this.shape = shape;
    }

    public void PrintArea(){
        System.out.println(this.shape.area());
    }

    public void PrintPerimeter(){
        System.out.println(this.shape.perimeter());
    }
}
