public class Square implements Shape{
    float length;
    public Square(float length) {
        this.length = length;
    }

    @Override
    public float area() {
        return length * length;
    }

    @Override
    public float perimeter() {
        return  4 * length;
    }
}
