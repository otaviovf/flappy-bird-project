package flappy.bird.project.level;

import flappy.bird.project.graphics.Shader;
import flappy.bird.project.graphics.Texture;
import flappy.bird.project.graphics.VertexArray;
import flappy.bird.project.input.Input;
import flappy.bird.project.math.Matrix4f;
import flappy.bird.project.math.Vector3f;
import java.util.Random;
import static org.lwjgl.glfw.GLFW.*;

public class Level {

    private final VertexArray background, fade;
    private final Texture bgTexture;
    
    private int xScroll = 0;
    private int map = 0;
    private int index = 0;
    private int OFFSET = 5;
    private boolean control = true, reset = false;
    
    private Bird bird;
    
    private Pipe[] pipes = new Pipe[5 * 2];
    private Random random = new Random();
    private float time = 0.0f;
    
    public Level(){

        float[] vertices = new float[]{
            -10.0f, -10.0f * 9.0f / 16.0f, 0.0f,
            -10.0f,  10.0f * 9.0f / 16.0f, 0.0f,
              0.0f,  10.0f * 9.0f / 16.0f, 0.0f,
              0.0f, -10.0f * 9.0f / 16.0f, 0.0f,
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
        
        background = new VertexArray(vertices, indices, tcs);
        bgTexture = new Texture("src/flappy/bird/project/resources/bg.jpeg");
       
        fade = new VertexArray(6);
        bird = new Bird();
        createPipes();
    }
    
    private void createPipes(){
        Pipe.create();
        for(int i = 0 ; i < 5 * 2; i += 2) {
            pipes[i] = new Pipe(OFFSET + index * 3.0f, random.nextFloat() * (11.0f - 7.0f)); //11 max 7 min
            pipes[i + 1] = new Pipe(pipes[i].getX(), pipes[i].getY() - 12.0f);
            index +=2;
        }
    }
    private void updatePipes(){
        //for (int i = 0; i < 5*2; i += 2) {
            pipes[index % 10] = new Pipe(OFFSET + index * 3.0f, random.nextFloat() * (11.0f - 7.0f));
            pipes[(index + 1)% 10] = new Pipe(pipes[index % 10].getX(), pipes[index % 10].getY() - 12.0f); 
            index += 2;
        //}
    }
    
    public void update(){
        if (control){
        xScroll--;        
        if (-xScroll % 335 == 0) map++;                
        if (-xScroll > 255 && -xScroll % 120 == 0) updatePipes();                
        }
        bird.update();
        
        if (control && collision()){
            bird.fall();
            control = false;
        }
        
        if (!control && Input.isKeyDown(GLFW_KEY_SPACE))
            reset = true;
            
        
        time += 0.01f;
    }
    
    private void renderPipes(){
        Shader.PIPE.enable();
        Shader.PIPE.setUniform2f("bird", time, time);
        Shader.PIPE.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(xScroll * 0.05f, 0.0f, 0.0f)));
        Pipe.getTexture().bind();
        Pipe.getMesh().bind();
                
        for (int i = 0; i < 5*2; i++) {
            Shader.PIPE.setUniformMat4f("ml_matrix", pipes[i].getModelMatrix());
            Shader.PIPE.setUniform1i("top", i % 2 == 0 ? 1 : 0);
            
            Pipe.getMesh().draw();
        }
        Pipe.getMesh().unbind();
        Pipe.getTexture().unbind();
    }
    
    public boolean isGameOver(){
        return reset;
    }
    
    private boolean collision(){
        for (int i = 0; i < 5*2 ; i++) {
            float bx = -xScroll * 0.05f;
            float by = bird.getY();
            float px = pipes[i].getX();
            float py = pipes[i].getY();
            
            float bx0 = bx - bird.getSize() / 2.0f;
            float bx1 = bx + bird.getSize() / 2.0f;
            float by0 = by - bird.getSize() / 2.0f;
            float by1 = by + bird.getSize() / 2.0f;
        
            float px0 = px;
            float px1 = px + Pipe.getWidth();       
            float py0 = py;
            float py1 = py + Pipe.getHeight();
        
            if (bx1 > px0 && bx0 < px1){
                if (by1 > py0 && by0 < py1){
                    return true;
                }
            }            
        }
        return false;
    }
    public void render(){
        bgTexture.bind();
        Shader.BG.enable();
        Shader.BG.setUniform2f("bird", 0, bird.getY());
        background.bind();
        for (int i = map; i < map + 3; i++) {
            Shader.BG.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(i * 10 + xScroll * 0.03f, 0.0f, 0.0f)));
            background.draw();
        }
        Shader.BG.disable();
        bgTexture.unbind();
        
        renderPipes();
        bird.render();
        
        Shader.FADE.enable();
        Shader.FADE.setUniform1f("time", time);
        fade.render();
        Shader.FADE.disable();
    }
}
