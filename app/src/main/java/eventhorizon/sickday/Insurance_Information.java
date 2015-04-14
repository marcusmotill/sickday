package eventhorizon.sickday;

/**
 * Created by marcusmotill on 2/26/15.
 */
import com.parse.*;

@ParseClassName("Insurance")
public class Insurance_Information extends ParseObject {
    public Insurance_Information() {
        // A default constructor is required.
    }

    public String getUserName() {
        return getString("username");
    }
    public void setUserName(String displayName) {
        put("username", displayName);
    }

    public String getInsuranceName(){
        return getString("insuranceName");
    }

    public void setInsuranceName(String insuranceName){
        put("insuranceName", insuranceName);
    }

    public void setUser(ParseUser user){
        put("user", user);
    }
}