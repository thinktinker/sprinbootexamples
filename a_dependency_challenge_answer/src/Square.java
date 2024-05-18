public class Square implements Shape{

    float value;

    public Square(float value) {
        this.value = value;
    }

    @Override
    public float area() {
        return this.value * this.value;
    }

    @Override
    public float perimeter(){
        return (float) 2 * (this.value + this.value);
    }
}
