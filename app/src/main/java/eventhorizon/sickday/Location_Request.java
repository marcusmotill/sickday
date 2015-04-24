package eventhorizon.sickday;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by marcusmotill on 4/23/15.
 */
@ParseClassName("Location_Request")
public class Location_Request extends ParseObject {

    public Location_Request(){

    }

    public void setZipCode(String zipCode){
        put("zipCode", zipCode);
    }
}
