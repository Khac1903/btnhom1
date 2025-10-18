//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.util.Scanner;
public class Main {

        // Type your main code here
        /**
         * tra ve so fibonacci th n.
         * @param n chi so so fibonacci can tra ve
         * @return so fibonacci thu n, -1 neu <0, Long.MAX_VALUE neu ket qua vuot qua gia tri long
         */
        public static long fibonacci(long n) {
            if (n<0) {
                return -1; // kiem tra dieu kien
            }
            if (n==0) {
                return 0; // so dau tien
            }
            if (n==1) {
                return 1; // so thu hai
            }
            long a=0;
            long b=1;
            for (int i=2;i<=n;i++)
            {
                if (Long.MAX_VALUE - b < a) {
                    return Long.MAX_VALUE ; // kiem tra dieu kien tran so
                }
                long c = a + b;
                a = b;
                b = c;
            }
            return b ;
        }
        public static void main(String[] args)
        {
            Scanner sc = new Scanner(System.in);
            int n = sc.nextInt();
            System.out.println(fibonacci(n));
        }
    }