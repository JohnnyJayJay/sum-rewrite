package bettersum.tools;

import java.util.Random;

public class Calculator {

    private final Random random;

    public Calculator() {
        this.random = new Random();
    }

    public boolean randomBoolean() {
        return random.nextBoolean();
    }

    public int randomInt() {
        return random.nextInt();
    }

    public double randomDouble() {
        return random.nextDouble();
    }

    public int randomInt(int min, int max) {
        return random.nextInt(max - min) + min;
    }

    public int modulus(int i) {
        return Math.abs(i);
    }

    public double modulus(double d) {
        return Math.abs(d);
    }

    public double square(double d) {
        return d * d;
    }

    public int square(int i) {
        return i * i;
    }

    public double radical(double d) {
        return Math.sqrt(d);
    }

    public double sin(double angle) {
        return Math.sin(Math.toRadians(angle));
    }

    public double cos(double angle) {
        return Math.cos(Math.toRadians(angle));
    }

    public double tan(double angle) {
        return Math.tan(Math.toRadians(angle));
    }

    public double asin(double angle) {
        return Math.toDegrees(Math.asin(angle));
    }

    public double acos(double angle) {
        return Math.toDegrees(Math.acos(angle));
    }

    public double atan(double angle) {
        return Math.toDegrees(Math.atan(angle));
    }

    public double exp(double d) {
        return Math.exp(d);
    }

    public double ln(double d) {
        return Math.log(d);
    }

    public double potency(double base, double exponent) {
        return Math.pow(base, exponent);
    }

    public int potency(int base, int exponent) {
        return (int) Math.pow(base, exponent);
    }

    public long round(double d) {
        return Math.round(d);
    }

    public int withoutDecimal(double d) {
        return (int) d;
    }

}
