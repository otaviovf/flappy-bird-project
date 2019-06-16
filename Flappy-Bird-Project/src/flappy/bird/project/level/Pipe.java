package flappy.bird.project.level;

import flappy.bird.project.graphics.Texture;
import flappy.bird.project.graphics.VertexArray;
import flappy.bird.project.math.Matrix4f;
import flappy.bird.project.math.Vector3f;

public class Pipe {
    private Vector3f position = new Vector3f();
    private static float width = 1.5f, height = 8.0f;
        
    private Matrix4f ml_matrix;    
    private static Texture texture;
    private static VertexArray mesh;

    public static void create(){
        float[] vertices = new float[]{
            0.0f, 0.0f, 0.1f,
            0.0f, height, 0.1f,
            width, height, 0.1f,
            width, 0.0f, 0.1f,
                       
        };
        byte[] indices = new byte[]{
            0, 1, 2,
            2, 3, 0
        };
        
        float[] tcs = new float[]{
            0, 1,
            0, 0,
            1, 0,
            1, 1
        };
        
        mesh = new VertexArray(vertices, indices, tcs);
        texture = new Texture("src/flappy/bird/project/resources/pipe.png");              
    }
    
    public Pipe(float x, float y){
        position.x = x;
        position.y = y;
        ml_matrix = Matrix4f.translate(position);
    }
    
    public float getX(){
        return position.x;
    }
    
    public float getY(){
        return position.y;
    }
    
    public Matrix4f getModelMatrix(){
        return ml_matrix;
    }
    public static VertexArray getMesh(){
        return mesh;
    }
    
    public static Texture getTexture(){
        return texture;
    }
    
    public static float getWidth(){
        return width;
    }
    
    public static float getHeight(){
        return height;
    }
}
