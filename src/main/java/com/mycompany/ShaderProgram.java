package com.mycompany;

import org.lwjgl.BufferUtils;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class ShaderProgram {
    private int programID;

    public ShaderProgram(String vertex, String fragment){
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader,vertex);
        glCompileShader(vertexShader);

        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader,fragment);
        glCompileShader(fragmentShader);

        programID = glCreateProgram();

        glAttachShader(programID,vertexShader);
        glAttachShader(programID,fragmentShader);

        glLinkProgram(programID);

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    }
    public  int getProgramID(){
        return programID ;
    }
    public void bind(){
        glUseProgram(programID);
    }
    public void unbind(){
        glUseProgram(0);
    }
    public void delete(){
        unbind();
        if(programID != 0){
            glDeleteProgram(programID);
        }
    }

}
