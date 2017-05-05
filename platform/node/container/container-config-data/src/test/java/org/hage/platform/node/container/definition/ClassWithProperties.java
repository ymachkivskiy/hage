package org.hage.platform.node.container.definition;


import java.util.List;
import java.util.Map;
import java.util.Set;


public class ClassWithProperties {

    private String firstConstructorArgument;
    private int secondConstructorArgument;
    private int a;
    private float b;
    private List<Object> list;
    private Map<Object, Object> map;
    private Set<Object> set;

    public ClassWithProperties() {
    }

    public ClassWithProperties(final String first, final Integer second) {
        firstConstructorArgument = first;
        secondConstructorArgument = second;
    }

    public String getFirstConstructorArgument() {
        return firstConstructorArgument;
    }

    public int getSecondConstructorArgument() {
        return secondConstructorArgument;
    }

    public int getA() {
        return a;
    }

    public float getB() {
        return b;
    }

    public List<Object> getList() {
        return list;
    }

    public Map<Object, Object> getMap() {
        return map;
    }

    public Set<Object> getSet() {
        return set;
    }

    public void setA(final int a) {
        this.a = a;
    }

    public void setB(final float b) {
        this.b = b;
    }

    public void setList(final List<Object> list) {
        this.list = list;
    }

    public void setMap(final Map<Object, Object> map) {
        this.map = map;
    }

    public void setSet(final Set<Object> set) {
        this.set = set;
    }
}