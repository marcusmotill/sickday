package eventhorizon.sickday;

/**
 * Created by marcusmotill on 2/27/15.
 */
import com.parse.*;

@ParseClassName("Sickday_Requests")
public class Sickday_Request extends ParseObject{

    public Sickday_Request(){
    }

    public String getUserName() {
        return getString("username");
    }

    public void setUserName(String displayName) {
        put("username", displayName);
    }

    public boolean getPendingRequest(){
        return getBoolean("pending_request");
    }

    public void setPendingRequest(boolean pendingRequest){
        put("pending_request", pendingRequest);
    }

    public void setUser(ParseUser user){
        put("user", user);
    }

    public void setInsurance(ParseUser user){
        put("insurance", user.get("insurance"));
    }

    public void setAddress(String address){
        put("Address", address);
    }
}
