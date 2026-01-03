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
    private TriangleRender triangleRender;
    private CubeRender cubeRender;

    private final float[] cube = {//cube vertices
            -0.5f, -0.5f, 0.0f,
             0.5f, -0.5f, 0.0f,
             0.5f,  0.5f, 0.0f,
            -0.5f,  0.5f, 0.0f,

            -0.5f, -0.5f, -1.0f,
             0.5f, -0.5f, -1.0f,
             0.5f,  0.5f, -1.0f,
            -0.5f,  0.5f, -1.0f
    };
    private final int[] index = {
            0,1,2,  2,3,0,//front side
            4,5,6,  6,7,4,//back side
            3,2,6,  6,7,3,//top side
            1,0,4,  4,5,1,//lower side
            4,0,3,  3,7,4,//left side
            1,5,6,  6,2,1//right side
    };

    private final float[] triangle = {
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.0f, 0.5f, 0.0f

    };
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
        System.out.println("Window in process...");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE,GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE,GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);

        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

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

        glEnable(GL_DEPTH_TEST);//Depth test
        glDepthFunc(GL_LESS);

        String vertexShader = """
            #version 330 core
            layout(location = 0) in vec3 aPos;
            void main() {
                gl_Position = vec4(aPos, 1.0);
            }
            """;
        String fragmentShader = """
            #version 330 core
            out vec4 FragColor;
            void main() {
                FragColor = vec4(1.0, 0.5, 0.2, 1.0);
            }
            """;
        shaderProgram = new ShaderProgram(vertexShader,fragmentShader);
        triangleRender = new TriangleRender(triangle);//triangle include
        cubeRender = new CubeRender(cube,index);//cube include
    }
    private void loop(){
        glClearColor(0.0f, 0.3f, 0.6f, 1.0f);//obj color
        while(!glfwWindowShouldClose(window)){
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            shaderProgram.bind();
            //triangleRender.render();
            cubeRender.render();//cube render
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
                glClearColor(0.0f,0.3f, 0.6f, 1.0f);
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
