package com.dpk.hammoq.Fragments.cameraUtils;

public class Camera {
    private String id;
    private String name;
    private boolean front;
    private boolean rear;
    private boolean external;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isExternal() {
        return external;
    }

    public void setExternal(boolean external) {
        this.external = external;
    }

    public boolean isRear() {
        return rear;
    }

    public void setRear(boolean rear) {
        this.rear = rear;
    }

    public boolean isFront() {
        return front;
    }

    public void setFront(boolean front) {
        this.front = front;
    }
}
