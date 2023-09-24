package org.example;

import java.lang.annotation.*;


public class myClass {
        private String name;
        public String hello;

        @InitMethod
        public void init(){
                System.out.println("The method is initialized!");
        }

}
