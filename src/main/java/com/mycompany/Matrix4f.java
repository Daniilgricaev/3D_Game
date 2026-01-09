package com.mycompany;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

public class Matrix4f {
    private float[] matrix;

    public Matrix4f(){
        matrix = new float[16];
        identity();
    }
    public void identity(){
        for(int i = 0;i<matrix.length;i++){
            matrix[i] = 0.0f;
            if(i % 5 == 0){
                matrix[i] = 1.0f;
            }
        }
    }
    public float getElem(int row,int col){
        return matrix[row * 4 + col];//formula for quick access to an element : O(1)
    }
    public void setElem(int row, int col, float element){
        matrix[row * 4 + col] = element;
    }
    public FloatBuffer toFloatBuffer(){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        for(int col = 0;col < 4;col++){
            for(int row = 0;row < 4;row++ ){
                buffer.put(matrix[row * 4 + col]);
            }
        }
        buffer.flip();
        return buffer;
    }
    public void multiply(Matrix4f other){
        float[] result = new float[16];
        for(int row = 0;row<4;row++){
            for(int col = 0;col<4;col++){
                float sum = 0.0f;
                for(int k = 0;k<4;k++){
                    sum += this.getElem(row,k)*other.getElem(k,col);
                }
                result[row * 4 + col] = sum;
            }
        }
        this.matrix = result;
    }
    public Matrix4f translate(float x , float y , float z){
        Matrix4f translation = new Matrix4f();
        translation.identity();
        translation.setElem(0,3,x);
        translation.setElem(1,3,y);
        translation.setElem(2,3,z);
        this.multiply(translation);
        return this;
    }
    public Matrix4f rotateY(float angle){
        Matrix4f rotatingY = new Matrix4f();
        rotatingY.identity();
        float Cos = (float)Math.cos(angle);
        float Sin = (float)Math.sin(angle);
        rotatingY.setElem(0,0,Cos);
        rotatingY.setElem(0,2,Sin);
        rotatingY.setElem(2,0,-Sin);
        rotatingY.setElem(2,2,Cos);
        this.multiply(rotatingY);
        return this;

    }
    public Matrix4f rotateX(float angle){
        Matrix4f rotatingX = new Matrix4f();
        rotatingX.identity();
        float Cos = (float)Math.cos(angle);
        float Sin = (float)Math.sin(angle);
        rotatingX.setElem(1,1,Cos);
        rotatingX.setElem(1,2,-Sin);
        rotatingX.setElem(2,1,Sin);
        rotatingX.setElem(2,2,Cos);
        this.multiply(rotatingX);
        return this;
    }
    public Matrix4f setPerspective(float fov, float aspect, float near, float far){
        this.identity();
        this.identity();
        float f = 1.0f/(float)Math.tan(Math.toRadians(fov)/2);
        this.setElem(0,0,f/aspect);
        this.setElem(1,1,f);
        this.setElem(2,2,(far + near)/(near - far));
        this.setElem(2,3,(2 * far * near) / (near - far));
        this.setElem(3,2,-1.0f);
        this.setElem(3,3,0.0f);
        return this;
    }

}
