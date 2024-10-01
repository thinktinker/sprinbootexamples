public class Circle implements Shape {
    
    float radius;
    float pi = (float)Math.PI;
    
    public Circle(float radius) {
        this.radius = radius;
    }

    @Override
    public float area() {
        return ((pi)*(radius*radius));
    }

    @Override
    public float perimeter() {
        return (2*pi*radius);
    }
}
