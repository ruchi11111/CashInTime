package friendlyitsolution.com.cit.adpter;

import java.util.Map;

public class Userobject {

    public Map<String,Object> data;
    public String key;

    public Userobject(Map<String,Object> data,String key)
    {
        this.key=key;
        this.data=data;
    }

}
