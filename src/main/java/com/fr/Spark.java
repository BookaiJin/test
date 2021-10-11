package com.fr;

/**
 * @author bokai
 * @version 10.0
 * Created by bokai on 2021/9/2
 */
interface A{

}

class A1 implements A{

}

class A2 implements A{

}

public class Spark {

    public static void main(String... args){
        A a = new A1();
        Spark spark = new Spark();
        spark.change(a);
        System.out.println(a instanceof A1);
        System.out.println(a instanceof A2);
    }

    void change(A a){
        a = new A2();
    }
}