public class CalculateShape {
    Shape shape;
    public CalculateShape(Shape shape){
        this.shape = shape;
    }
    public void PrintArea(){
        System.out.println(String.format("%.2f", this.shape.area()));
    }
    public void PrintPerimeter(){
        System.out.println(String.format("%.2f", this.shape.perimeter()));
    }
}

