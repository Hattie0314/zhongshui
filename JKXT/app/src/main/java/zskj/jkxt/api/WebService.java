package zskj.jkxt.api;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import zskj.jkxt.JKXTApplication;
import zskj.jkxt.util.ConnectionChangeReceiver;

/**
 * Created by WYY on 2017/2/24.
 */

public class WebService {
    static public String namespace = "http://tempuri.org/";
    static public String serviceUrl = "http://192.168.117.58:8085/H9000Service.asmx";
    static final String ERRORMSG = "获取数据失败";
    static WebService service = null;

    // static public String namespace = "http://WebXml.com.cn/";
    // static public String serviceUrl =
    // "http://webservice.webxml.com.cn/WebServices/MobileCodeWS.asmx";
    private WebService() {
    }

    public static WebService getInstance() {
        if (service == null) {
            service = new WebService();
        }
        return service;
    }

    public String isUser(String userName, String password) {
        if (JKXTApplication.NETWORK_FLAG == ConnectionChangeReceiver.NET_NONE) {
            return ERRORMSG;
        }
        String methodName = "isUser";
        SoapObject request = new SoapObject(namespace, methodName);
        request.addProperty("username", userName);
        request.addProperty("password", password);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht = new HttpTransportSE(serviceUrl);
        try {
            ht.call(namespace + methodName, envelope);
            SoapObject object = (SoapObject) envelope.bodyIn;
            if (object != null && object.getProperty(0) != null) {
                return object.getProperty(0).toString();
            } else
                return ERRORMSG;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    public String GetUserInfo() {
        if (JKXTApplication.NETWORK_FLAG == ConnectionChangeReceiver.NET_NONE) {
            return ERRORMSG;
        }
        String methodName = "getUserInfo";
        SoapObject request = new SoapObject(namespace, methodName);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht = new HttpTransportSE(serviceUrl);
        try {
            ht.call(namespace + methodName, envelope);
            SoapObject object = (SoapObject) envelope.bodyIn;
            if (object != null && object.getProperty(0) != null) {
                return object.getProperty(0).toString();
            } else
                return ERRORMSG;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    public String addUser(String userName,String password,String rights,String range,String level) {
        if (JKXTApplication.NETWORK_FLAG == ConnectionChangeReceiver.NET_NONE) {
            return ERRORMSG;
        }
        String methodName = "addUser";
        SoapObject request = new SoapObject(namespace, methodName);
        request.addProperty("username", userName);
        request.addProperty("password", password);
        request.addProperty("rights", rights);
        request.addProperty("range", range);
        request.addProperty("level", level);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht = new HttpTransportSE(serviceUrl);
        try {
            ht.call(namespace + methodName, envelope);
            SoapObject object = (SoapObject) envelope.bodyIn;
            if (object != null && object.getProperty(0) != null) {
                return object.getProperty(0).toString();
            } else
                return ERRORMSG;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    public String updateUser(String userName,String password,String rights,String range,String level) {
        if (JKXTApplication.NETWORK_FLAG == ConnectionChangeReceiver.NET_NONE) {
            return ERRORMSG;
        }
        String methodName = "updateUser";
        SoapObject request = new SoapObject(namespace, methodName);
        request.addProperty("username", userName);
        request.addProperty("password", password);
        request.addProperty("rights", rights);
        request.addProperty("range", range);
        request.addProperty("level", level);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht = new HttpTransportSE(serviceUrl);
        try {
            ht.call(namespace + methodName, envelope);
            SoapObject object = (SoapObject) envelope.bodyIn;
            if (object != null && object.getProperty(0) != null) {
                return object.getProperty(0).toString();
            } else
                return ERRORMSG;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String deleteUser(String userName) {
        if (JKXTApplication.NETWORK_FLAG == ConnectionChangeReceiver.NET_NONE) {
            return ERRORMSG;
        }
        String methodName = "deleteUser";
        SoapObject request = new SoapObject(namespace, methodName);
        request.addProperty("username", userName);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht = new HttpTransportSE(serviceUrl);
        try {
            ht.call(namespace + methodName, envelope);
            SoapObject object = (SoapObject) envelope.bodyIn;
            if (object != null && object.getProperty(0) != null) {
                return object.getProperty(0).toString();
            } else
                return ERRORMSG;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String GetStationInfo(String Str_StationCode) {
        if (JKXTApplication.NETWORK_FLAG == ConnectionChangeReceiver.NET_NONE) {
            return ERRORMSG;
        }
        String methodName = "GetStationInfo";
        SoapObject request = new SoapObject(namespace, methodName);
        request.addProperty("Str_StationCode", Str_StationCode);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht = new HttpTransportSE(serviceUrl);
        try {
            ht.call(namespace + methodName, envelope);
            SoapObject object = (SoapObject) envelope.bodyIn;
            if (object != null && object.getProperty(0) != null) {
                return object.getProperty(0).toString();
            } else
                return ERRORMSG;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public void GetStationP(String sdate, String pcode, String forecase_pcode, RequestCallback callback) {
        if (JKXTApplication.NETWORK_FLAG == ConnectionChangeReceiver.NET_NONE) {
            callback.onFail(ERRORMSG);
            return;
        }
        String methodName = "GetStationP";
        SoapObject request = new SoapObject(namespace, methodName);
        request.addProperty("sdate", sdate);
        request.addProperty("pcode", pcode);
        request.addProperty("forecase_pcode", forecase_pcode);
        Log.e("result----------->", sdate + "---------------" + pcode);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht = new HttpTransportSE(serviceUrl, 1000000);
        try {
            ht.call(namespace + methodName, envelope);
            SoapObject object = (SoapObject) envelope.bodyIn;
            if (object != null && object.getProperty(0) != null) {
                callback.onSuccess(object.getProperty(0).toString());
                //Log.e("result----------->",object.getProperty(0).toString());
            } else
                callback.onFail(ERRORMSG);
        } catch (Exception e) {
            callback.onFail(e.getMessage());
        }
    }

    public String GetStationName(String ranges) {
        if (JKXTApplication.NETWORK_FLAG == ConnectionChangeReceiver.NET_NONE) {
            return ERRORMSG;
        }
        String methodName = "GetStationName";
        SoapObject request = new SoapObject(namespace, methodName);
        request.addProperty("ranges", ranges);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht = new HttpTransportSE(serviceUrl, 1000000);
        try {
            ht.call(namespace + methodName, envelope);
            SoapObject object = (SoapObject) envelope.bodyIn;
            if (object != null && object.getProperty(0) != null) {
                return object.getProperty(0).toString();
            } else
                return ERRORMSG;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public void GetAlarmDataTop10(RequestCallback callback) {
        if (JKXTApplication.NETWORK_FLAG == ConnectionChangeReceiver.NET_NONE) {
            callback.onFail(ERRORMSG);
            return;
        }
        String methodName = "GetAlarmDataTop10";
        SoapObject request = new SoapObject(namespace, methodName);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht = new HttpTransportSE(serviceUrl);
        try {
            ht.call(namespace + methodName, envelope);
            SoapObject object = (SoapObject) envelope.bodyIn;
            if (object != null && object.getProperty(0) != null) {
                callback.onSuccess(object.getProperty(0).toString());
            } else
                callback.onFail(ERRORMSG);
        } catch (Exception e) {
            callback.onFail(e.getMessage());
        }
    }

    public String GetAlarmData(String sdate, String stime, String ranges, String level) {
        if (JKXTApplication.NETWORK_FLAG == ConnectionChangeReceiver.NET_NONE) {
            return ERRORMSG;
        }
        String methodName = "GetAlarmData";
        SoapObject request = new SoapObject(namespace, methodName);
        request.addProperty("sdate", sdate);
        request.addProperty("stime", stime);
        request.addProperty("ranges", ranges);
        request.addProperty("level", level);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht = new HttpTransportSE(serviceUrl);
        try {
            ht.call(namespace + methodName, envelope);
            SoapObject object = (SoapObject) envelope.bodyIn;
            if (object != null && object.getProperty(0) != null) {
                return object.getProperty(0).toString();
            } else
                return ERRORMSG;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String GetStationP2(String sdate, String station_names) {

        if (JKXTApplication.NETWORK_FLAG == ConnectionChangeReceiver.NET_NONE) {
            return ERRORMSG;
        }
        String methodName = "GetStationP2";
        SoapObject request = new SoapObject(namespace, methodName);
        request.addProperty("sdate", sdate);
        request.addProperty("station_name", station_names);
        Log.e("result----------->", sdate + "---------------" + station_names);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht = new HttpTransportSE(serviceUrl, 1000000);
        try {
            ht.call(namespace + methodName, envelope);
            SoapObject object = (SoapObject) envelope.bodyIn;
            if (object != null && object.getProperty(0) != null) {
                 return  object.getProperty(0).toString();
                //Log.e("result----------->",object.getProperty(0).toString());
            } else
                return ERRORMSG;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public void GetStationP3(String sdate, String time, String station_names, RequestCallback callback) {
        if (JKXTApplication.NETWORK_FLAG == ConnectionChangeReceiver.NET_NONE) {
            callback.onFail(ERRORMSG);
            return;
        }
        String methodName = "GetStationP3";
        SoapObject request = new SoapObject(namespace, methodName);
        request.addProperty("sdate", sdate);
        request.addProperty("time", time);
        request.addProperty("station_name", station_names);
        Log.e("result----------->", sdate + "---------------" + station_names);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht = new HttpTransportSE(serviceUrl, 1000000);
        try {
            ht.call(namespace + methodName, envelope);
            SoapObject object = (SoapObject) envelope.bodyIn;
            if (object != null && object.getProperty(0) != null) {
                callback.onSuccess(object.getProperty(0).toString());
                Log.e("result-----sss------>", object.getProperty(0).toString());
            } else
                callback.onFail(ERRORMSG);
        } catch (Exception e) {
            callback.onFail(e.getMessage());
        }
    }
}
