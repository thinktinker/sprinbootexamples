public class Circle implements Shape{

    float value;

    public Circle(float value) {
        this.value = value;
    }

    @Override
    public float area() {
        return (float) (Math.PI * this.value * this.value);
    }

    @Override
    public float perimeter() {
        return (float) (2 * Math.PI * this.value);
    }
}
