package flappy.bird.project.level;

import flappy.bird.project.graphics.Shader;
import flappy.bird.project.graphics.Texture;
import flappy.bird.project.graphics.VertexArray;
import flappy.bird.project.input.Input;
import flappy.bird.project.math.Matrix4f;
import flappy.bird.project.math.Vector3f;
import org.lwjgl.glfw.GLFW;
import static org.lwjgl.glfw.GLFW.*;

public class Bird {
    private float SIZE = 1.0f;
    private VertexArray mesh;
    private Texture texture;
    
    private Vector3f position = new Vector3f();
    private float rot;
    private float delta = 0.0f;
    
    public Bird(){
        float[] vertices = new float[]{
            -SIZE / 2.0f, -SIZE / 2.0f, 0.2f,
            -SIZE / 2.0f,  SIZE / 2.0f, 0.2f,
             SIZE / 2.0f,  SIZE / 2.0f, 0.2f,
             SIZE / 2.0f, -SIZE / 2.0f, 0.2f,            
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
        texture = new Texture("src/flappy/bird/project/resources/bird.png");
        
    }
    
    public void update(){
        position.y -= delta;
        if (Input.isKeyDown(GLFW_KEY_SPACE)){
            delta = -0.15f;
        } else {
            delta += 0.01f;
        }
        
        rot = -delta * 90.0f; 
        /*if (Input.keys[GLFW.GLFW_KEY_UP]) position.y += 0.1f;
        if (Input.keys[GLFW.GLFW_KEY_DOWN]) position.y -= 0.1f;
        if (Input.keys[GLFW.GLFW_KEY_LEFT]) position.x -= 0.1f;
        if (Input.keys[GLFW.GLFW_KEY_RIGHT]) position.x += 0.1f;*/
    }
    
    void fall(){
        delta = -0.15f;
    }
    
    public void render(){
        Shader.BIRD.enable();
        Shader.BIRD.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.rotate(rot)));
        texture.bind();
        mesh.render();
        Shader.BIRD.disable();
    }

    float getY() {
        return position.y;
    }

    float getSize() {
        return SIZE;
    }
}
