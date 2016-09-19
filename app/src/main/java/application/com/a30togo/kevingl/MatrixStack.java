package application.com.a30togo.kevingl;

/**
 * Created by jeromehuang on 9/19/16.
 */
import java.util.Stack;
import android.opengl.Matrix;

public class MatrixStack {

    private static Stack<float[]> matrixStack = new Stack<float[]>();

    private static float[] MVMatrix = new float[16];
    private static float[] PMatrix = new float[16];
    private static float[] OMatrix = new float[16];
    private static float[] MVPMatrix = new float[16];

    protected static void initStack(){
        float[] basisMatrix = new float[16];
        Matrix.setIdentityM(basisMatrix, 0);
        matrixStack.push(basisMatrix);
        MVMatrix = basisMatrix;
        Matrix.setIdentityM(PMatrix, 0);
        Matrix.setIdentityM(OMatrix, 0);
    }

    public static void mvPushMatrix() {
        float[] neueMatrix = MVMatrix.clone();
        matrixStack.push(neueMatrix);
        MVMatrix = neueMatrix ;
    }

    public static void mvPopMatrix() {
        matrixStack.pop();
        MVMatrix = matrixStack.peek();
    }


    public static float[] getMVMatrix(){
        return MVMatrix;
    }

    public static float[] getPMatrix(){
        return PMatrix;
    }

    public static float[] getOMatrix(){
        return OMatrix;
    }

    public static float[] getMVPMatrix(){
        return MVPMatrix;
    }

    public static void glLoadMatrix(float[] matrix){
        MVMatrix = matrix;
    }


}
