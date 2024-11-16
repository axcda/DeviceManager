package zjc.devicemanage.model;

import java.util.List;

public class DeviceClassList {
    private List<DeviceClass> result;

    public List<DeviceClass> getResult() {
        return result;
    }

    public void setResult(List<DeviceClass> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "DeviceClassList{" +
                "result=" + result +
                '}';
    }
}