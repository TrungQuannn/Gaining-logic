import java.util.Arrays;

public class SpinningCube {
    static double A = 0.0;
    static double B = 0.0;
    static double C = 0.0;

    static double x = 0.0;
    static double y = 0.0;
    static double z = 0.0;

    static int cubeSize = 40;
    static int width = 160;
    static int height = 44;

    static int K1 = 20;

    static double[] zBuffer = new double[160 * 44];
    static char[] buffer = new char[160 * 44];

    static char background = ' ';

    static double incrementSpeed = 1;

    public static double CalculateX(double i, double j, double k){
        return j * Math.sin(A) * Math.sin(B) * Math.cos(C) - k * Math.cos(A) * Math.sin(B) * Math.cos(C) + j * Math.cos(A) * Math.sin(C) + k* Math.sin(A) * Math.sin(C) + i* Math.cos(B)*Math.cos(C);
    }

    public static double CalculateY(double i, double j, double k){
        return j * Math.cos(A) * Math.cos(C) + k* Math.sin(A) * Math.cos(C) - j* Math.sin(A)*Math.sin(B)*Math.sin(C) + k* Math.cos(A)*Math.sin(B)*Math.sin(C) - i*Math.cos(B)* Math.sin(C);
    }

    public static double CalculateZ(double i, double j, double k){
        return k*Math.cos(A) * Math.cos(B) - j * Math.sin(A) * Math.cos(B) + i * Math.sin(B);
    }

    public static void CalculateSurface(double cubeX, double cubeY, double cubeZ, char c) {
        x = CalculateX(cubeX, cubeY, cubeZ);
        y = CalculateY(cubeX, cubeY, cubeZ);
        z = CalculateZ(cubeX, cubeY, cubeZ) + 100;

        double ooz = 1/z;

        int xp = (int)(width / 2 + ooz * K1 * x * 2);
        int yp = (int)(height / 2 + ooz * K1 * y);
        
        int index = (int)xp + yp*width;
        if(index >= 0 && index < width*height){
            if(ooz > zBuffer[index]){
                zBuffer[index] = ooz;
                buffer[index] = c;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.print("\033[2J");
        while (true) {
            Arrays.fill(buffer, background);
            Arrays.fill(zBuffer, Double.NEGATIVE_INFINITY);

            for (double cubeX = -cubeSize; cubeX < cubeSize; cubeX += incrementSpeed) {
                for (double cubeY = -cubeSize; cubeY < cubeSize; cubeY += incrementSpeed) {
                    CalculateSurface(cubeX, cubeY, -cubeSize, '#');
                    CalculateSurface(cubeSize, cubeY, cubeX, '@');
                    CalculateSurface(-cubeSize, cubeY, -cubeX, '%');
                    CalculateSurface(-cubeX, cubeY, cubeSize, '&');
                }
            }

            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < width * height; j++) {
                sb.append(buffer[j]);
                if (j % width == width - 1) sb.append('\n');
            }

            System.out.print("\033[H");
            System.out.print(sb);

            A += 0.01;
            B += 0.01;
            //C += 0.05;
            Thread.sleep(16);
        }
    }
}
