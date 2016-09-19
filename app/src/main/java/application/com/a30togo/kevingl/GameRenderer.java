package application.com.a30togo.kevingl;

/**
 * Created by jeromehuang on 9/19/16.
 */
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

public class GameRenderer implements GLSurfaceView.Renderer {

    private final String vertexShaderCode =
            "uniform mat4 uMVPMatrix;   \n" +
                    "attribute vec4 vPosition;  \n" +
                    "void main(){               \n" +
                    " gl_Position = uMVPMatrix * vPosition; \n" +
                    "}  \n";

    private final String fragmentShaderCode =
            "void main(){              			\n" +
                    " gl_FragColor = vec4(1.0, 1.0, 1.0, 0.3); \n" +
                    "}                         			\n";

    private int loadShader(int type, String shaderCode){
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    // END OF SHADER STUFF

    private int mProgram;
    private int maPositionHandle;
    private int muMVPMatrixHandle;

    private FloatBuffer triangleVB;

    public GameRenderer() {
    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES20.glClearColor(0,0,0,0);
        initShapes();

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);

        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glUseProgram(mProgram);
        MatrixStack.initStack();
    }

    public void onDrawFrame(GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        drawGround();
    }


    private void drawGround() {
        GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false, 0, triangleVB);
        GLES20.glEnableVertexAttribArray(maPositionHandle);
        Matrix.multiplyMM(MatrixStack.getMVPMatrix(), 0, MatrixStack.getMVMatrix(), 0, MatrixStack.getMVMatrix(), 0);
        Matrix.multiplyMM(MatrixStack.getMVPMatrix(), 0, MatrixStack.getPMatrix(), 0, MatrixStack.getMVPMatrix(), 0);
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixStack.getMVPMatrix(), 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        GLES20.glDisableVertexAttribArray(maPositionHandle);
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;
        Matrix.frustumM(MatrixStack.getPMatrix(), 0, -ratio, ratio, -1, 1, 1, 1000);

        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        Matrix.setLookAtM(MatrixStack.getMVMatrix(), 0,
                0, 0,-1,
                0, 0, 0,
                0, 1, 0);

    }

    private void initShapes(){
        float triangleCoords[] = {
                -15f, -5f,  15f,
                15f, -5f,  15f,
                -15f, -5f, -15f,
                15f, -5f, -15f
        };
        ByteBuffer vbb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        triangleVB = vbb.asFloatBuffer();
        triangleVB.put(triangleCoords);
        triangleVB.position(0);
    }

}
