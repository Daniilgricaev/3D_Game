package com.mycompany;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class App{
    private long window;
    private ShaderProgram shaderProgram;
    private final float[] triangle = {
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.0f, 0.5f, 0.0f

    };
    TriangleRender triangleRender = new TriangleRender();
    public float[] getTriangle(){
        return triangle;
    }

    public void run(){
        System.out.println("Hello LWJGL" + Version.getVersion());

        init();
        loop();
        delete();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
    private void init(){
        GLFWErrorCallback.createPrint(System.err).set();

        if(!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");
        if(triangleRender != null){
            System.out.println("Triangle wos created");
        }else{
            System.out.println("Error: triangle don't created");
        }
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE,GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE,GLFW_TRUE);

        window = glfwCreateWindow(800,600,"MY 3D game with OpenGL",NULL, NULL);
        if (window == NULL){
            throw new RuntimeException("Failed to create window");
        }
        try(MemoryStack stack = stackPush()){
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(window,pWidth,pHeight);
            int realWidth = pWidth.get(0);
            int realHeight = pHeight.get(0);
            System.out.println("Window size :"+realWidth+"*"+realHeight);
            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowPos(
                    window,
                    (vidMode.width() - pWidth.get(0)) / 2,
                    (vidMode.height() - pHeight.get(0)) / 2
            );
        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);

        GL.createCapabilities();
        shaderProgram = new ShaderProgram("vertex.glsl", "fragment.glsl");
    }
    private void loop(){
        GL.createCapabilities();
        glClearColor(0.0f, 0.3f, 0.6f, 0.0f);
        while(!glfwWindowShouldClose(window)){
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            shaderProgram.bind();
            triangleRender.render();
            shaderProgram.unbind();

            glfwSwapBuffers(window);

            glfwPollEvents();
            if(glfwGetKey(window, GLFW_KEY_ESCAPE)==GLFW_PRESS){
                glfwSetWindowShouldClose(window, true);
            }
            if (glfwGetKey(window,GLFW_KEY_SPACE)==GLFW_PRESS) {
                System.out.println("Space");
            }
            if(glfwGetKey(window, GLFW_KEY_F1)==GLFW_PRESS){
                float red = (float) Math.random();
                float green = (float) Math.random();
                float blue = (float) Math.random();

                glClearColor(red, green, blue, 1.0f);
            }
            if(glfwGetKey(window, GLFW_KEY_R)==GLFW_PRESS){
                glClearColor(0.0f,0.3f, 0.6f, 0.0f);
                System.out.println("The colors were set to the original");
            }

        }
    }
    private void delete(){
        if(shaderProgram != null){
            shaderProgram.delete();
        }

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public static void main(String[] args) {
        new App().run();
    }
}
