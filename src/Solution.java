import java.lang.Math;
public class Solution {
    private int numerator;
    private int denominator;

    /**
     * Set numerator.
     * @param numerator is numerator
     */
    public void setNumerator(int numerator) {
        this.numerator = numerator;
    }

    /**
     * Set denominator.
     * @param denominator is denominator
     */
    public void setDenominator(int denominator) {
        if (denominator != 0) {
            this.denominator = denominator;
        }
    }

    /**
     * Get numerator.
     * @return numerator
     */
    public int getNumerator() {
        return numerator;
    }

    /**
     * Get denominator.
     * @return denominator
     */
    public int getDenominator() {
        return denominator;
    }

    /**
     * Constructor 1.
     * @param numerator is numerator
     * @param denominator is denominator
     */
    Solution(int numerator, int denominator) {
        if (denominator != 0) {
            setNumerator(numerator);
            setDenominator(denominator);
        } else {
            setNumerator(numerator);
            setDenominator(1);
        }
    }

    /**
     * Constructor 2.
     * @param other is other Solution
     */
    Solution(Solution other) {
        if (other.getDenominator() != 0) {
            this.numerator = other.numerator;
            this.denominator = other.denominator;
        }
    }

    /**
     * Constructor 3.
     */
    Solution() {
        setDenominator(1);
    }
    /**
     * Tìm gcd của 2 số.
     * @param a là số thứ nhất
     * @param b là số thứ hai
     * @return gcd của 2 số
     */
    private int gcd(int a, int b) {
        while(b!=0) {
            int temp=a%b;
            a=b;
            b=temp;
        }
        return Math.abs(a);
    }
    /**
     * Rút gọn phân số.
     * @return Solution là 1 phân số
     */
    public Solution reduce() {
        int gcd =gcd(Math.abs(numerator),Math.abs(denominator)) ;
        numerator/=gcd;
        denominator/=gcd;
        if (denominator<0) {
            numerator=-numerator;
            denominator=-denominator;
        }
        return this;
    }
    /**
     * Add this with other, save result into this.
     * @param other is other Solution
     * @return this solution
     */
    public Solution add(Solution other) {
        this.numerator=this.numerator*other.denominator + other.numerator*this.denominator;
        this.denominator=this.denominator * other.denominator;
        return this.reduce();
    }

    /**
     * Subtract two Solution, store result into this.
     * @param other is other Solution
     * @return this Solution
     */

    public Solution subtract(Solution other) {
        this.numerator=this.numerator*other.denominator - other.numerator*this.denominator;
        this.denominator=this.denominator * other.denominator;
        return this.reduce();
    }
    /**
     * Multiply two Solution, store result into this.
     * @param other is other Solution
     * @return this Solution
     */
    public Solution multiply(Solution other) {
        this.numerator *= other.numerator;
        this.denominator *= other.denominator;
        return reduce();
    }
    /**
     * Divide two Solution, store into this.
     * @param other is other Solution
     * @return this Solution
     */
    public Solution divide(Solution other) {
        if (other.numerator != 0) {
            this.numerator *= other.denominator;
            this.denominator *= other.numerator;
            reduce();
        }
        return this;
    }

    /**
     * Overriding equals method.
     * @param obj the reference object with which to compare.
     * @return true or false
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Solution) {
            Solution other= (Solution) obj;
            this.reduce();
            other.reduce();
            return this.numerator==other.numerator && this.denominator==other.denominator;
        }
        return false;
    }
}
