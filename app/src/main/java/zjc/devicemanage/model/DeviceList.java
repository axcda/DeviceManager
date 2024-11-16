package zjc.devicemanage.model;

import java.util.List;

public class DeviceList {
    private List<Device> result;

    public List<Device> getResult() {
        return result;
    }

    public void setResult(List<Device> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "DeviceList{" +
                "result=" + result +
                '}';
    }
}