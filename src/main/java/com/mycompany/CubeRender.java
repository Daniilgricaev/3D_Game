package com.mycompany;

import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL30.*;

public class CubeRender {
    private int idVAO;
    private int idVBO;
    private int idEBO;
    private boolean initialized = false;

    public CubeRender(float[] vertices, int[] index){
        setupBuff(vertices,index);
    }
    private void setupBuff(float[] vertices,int[] index){
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
        System.out.println("VBO created");

        idEBO = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,idEBO);
        IntBuffer indexBuffer = BufferUtils.createIntBuffer(index.length);
        indexBuffer.put(index).flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);
        System.out.println("EBO created");


        glBindVertexArray(0);
        initialized = true;
    }
    public void render(){
        if(!initialized){
            return;
        }
        glBindVertexArray(idVAO);
        glDrawElements(GL_TRIANGLES,36 ,GL_UNSIGNED_INT,0);
        glBindVertexArray(0);
    }
    public void clean(){
        glDeleteBuffers(idVBO);
        glDeleteVertexArrays(idVAO);
        glDeleteBuffers(idEBO);
        System.out.println("Cube deleted");
    }
}
