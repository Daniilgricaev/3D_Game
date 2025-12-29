package com.mycompany;

import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;
import static org.lwjgl.opengl.GL30.*;

public class TriangleRender {
    private int idVBO;
    private int idVAO;
    private boolean initialized = false;

    public TriangleRender(float[] vertices){
        setupBuff(vertices);
    }
    private void setupBuff(float[] vertices){
        idVAO = glGenVertexArrays();
        glBindVertexArray(idVAO);
        System.out.println("VAO created");

        idVBO = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,idVBO);

        FloatBuffer buff = BufferUtils.createFloatBuffer(vertices.length);
        buff.put(vertices).flip();
        glBufferData(GL_ARRAY_BUFFER, buff, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);

        glBindVertexArray(0);
        initialized = true;
    }
    public void render(){
        if(!initialized){
            return;
        }
        glBindVertexArray(idVAO);
        glDrawArrays(GL_TRIANGLES,0 ,3);
        glBindVertexArray(0);
    }
    public void clean(){
        glDeleteBuffers(idVBO);
        glDeleteVertexArrays(idVAO);
        System.out.println("Triangle deleted");
    }

}
